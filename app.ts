import express from "express"
import http from "http"
import path from "path"
import Observable from "./observable"

const app = express()
const PORT = process.env.PORT || 5000

app.use(express.urlencoded({ extended: false }))
app.use(express.json())

const time = (ms: number) => new Promise(res => setTimeout(res, ms))

const ServerUsers = new Observable<User[]>([])
var ServerGames: Observable<Game>[] = []

interface User {
	usnm: string
	inGame: boolean
	request: string
	connections: Observable<number>
}

interface Game {
	white: string
	black: string
	turn: "white" | "black"
	whiteMove: string
	blackMove: string
}

app.get("/Start", (_req, res) => {
	return res.status(200).send()
})

/**
 * Callback to start connection with new Client
 */
app.get("/InitNewUser", (req, res) => {
	const { usnm }: { usnm: string } = req.query as any

	if (!usnm) return res.status(400).send("params/usnm-required")

	for (let i = 0; i < ServerUsers.get().length; i++) {
		const user = ServerUsers.get()[i]
		if (user.usnm === usnm) {
			return res.status(400).send("init/user-exists")
		}
	}

	const User: User = {
		usnm,
		inGame: false,
		request: "",
		connections: new Observable(0)
	}

	ServerUsers.set([...ServerUsers.get(), User])

	return res.status(200).send()
	// Now client MUST send: "AwaitData" and "Connected" continuously
})

app.get("/CheckUserExists", (req, res) => {
	const { usnm }: { usnm: string } = req.query as any

	if (!usnm) return res.status(400).send("params/usnm-required")

	for (let i = 0; i < ServerUsers.get().length; i++) {
		const user = ServerUsers.get()[i]
		if (user.usnm === usnm) {
			return res.status(200).send(false)
		}
	}

	return res.status(200).send(true)
})

/**
 * Connected function to keep connection between clients and server
 * Client sends 'Connected' to server
 * Client will respond with a new 'Connected' after receiving the value from previous
 * Client will send it within 3 seconds if not, registered as disconnected
 */
app.get("/Connected", (req, res) => {
	const { usnm }: { usnm: string } = req.query as any

	if (!usnm) return res.status(400).send("params/usnm-required")

	const User = getUser(usnm)
	var reset = true

	if (User) {
		User.connections.set(User.connections.get() + 0.25)
		time(10000).then(() => {
			res.status(200).send()

			User.connections.waitForChange().then(() => {
				reset = false
			})

			time(3000).then(() => {
				if (reset) {
					// Remove user
					const newUsers = ServerUsers.get().filter(
						usr => usr.usnm !== User.usnm
					)
					ServerUsers.set(newUsers)

					// Shut game down if user disconnects
					const Game = getGame(usnm)
					if (Game) {
						ServerGames = ServerGames.filter(game => game !== Game)
					}
				}
			})
		})
	} else {
		return res.status(400).send("auth/user-not-found")
	}
})

/**
 * Callbacks to send users on request
 */
app.get("/GetServerOpponents", (req, res) => {
	const { usnm }: { usnm: string } = req.query as any

	if (!usnm) return res.status(400).send("params/usnm-required")

	const Sender = getUser(usnm)
	if (!Sender) return res.status(400).send("auth/user-not-found")

	res.status(200).send(ServerUsers.get())
})

app.get("/UpdateOnServerOpponents", (req, res) => {
	const { usnm }: { usnm: string } = req.query as any

	if (!usnm) return res.status(400).send("params/usnm-required")

	const Sender = getUser(usnm)
	if (!Sender) return res.status(400).send("auth/user-not-found")

	let sent = false
	ServerUsers.waitForChange().then(users => {
		if (!sent) {
			res.status(200).send(users)
			sent = true
		}
	})

	time(5000).then(() => {
		if (!sent) {
			res.status(200).send(ServerUsers.get())
			sent = true
		}
	})
})

/**
 * Callback to set a request to play with a player
 */
app.get("/SetRequest", (req, res) => {
	const {
		usnm,
		request
	}: { usnm: string; request: string } = req.query as any

	if (!usnm) return res.status(400).send("params/usnm-required")
	if (!request) return res.status(400).send("params/request-required")

	const User = getUser(usnm)
	const Requested = getUser(request)

	if (User && Requested) {
		User.request = request
		const newUsers = ServerUsers.get().filter(usr => usr.usnm !== User.usnm)
		newUsers.push(User)

		if (Requested.request === User.usnm) {
			// Set players busy
			User.inGame = true
			Requested.inGame = true

			const newGame: Game = {
				white: Requested.usnm,
				black: User.usnm,
				turn: "white",
				whiteMove: "",
				blackMove: ""
			}

			ServerGames = [...ServerGames, new Observable(newGame)]
		}

		res.status(200).send()
		ServerUsers.set(newUsers)
	} else if (User && request === "") {
		User.request = ""
		const newUsers = ServerUsers.get().filter(usr => usr.usnm !== User.usnm)
		newUsers.push(User)
		ServerUsers.set(newUsers)

		res.status(200).send()
	} else {
		res.status(400).send("auth/user-not-found")
	}
})

/**
 * Callbacks to get updates on game state
 */
app.get("/GetGameState", (req, res) => {
	const { usnm }: { usnm: string } = req.query as any

	if (!usnm) return res.status(400).send("params/usnm-required")

	const Sender = getUser(usnm)
	if (!Sender) return res.status(400).send("auth/user-not-found")

	const Game = getGame(usnm)
	if (Game) {
		res.status(200).send(getUserGame(Game.get(), usnm))
	} else {
		res.status(200).send({
			team: "",
			turn: "",
			moveToExec: ""
		})
	}
})

app.get("/UpdateOnGameState", (req, res) => {
	const { usnm }: { usnm: string } = req.query as any

	if (!usnm) return res.status(400).send("params/usnm-required")

	const Sender = getUser(usnm)
	if (!Sender) return res.status(400).send("auth/user-not-found")

	const Game = getGame(usnm)

	let sent = false
	if (Game) {
		Game.waitForChange().then(newGame => {
			if (!sent) {
				res.status(200).send(getUserGame(newGame, usnm))
				sent = true
			}
		})
		time(5000).then(() => {
			const GameAfter5s = getGame(usnm)
			if (!sent) {
				if (GameAfter5s) {
					res.status(200).send(getUserGame(GameAfter5s.get(), usnm))
					sent = true
				} else {
					res.status(200).send({
						team: "",
						turn: "",
						moveToExec: ""
					})
					sent = true
				}
			}
		})
	} else {
		const Every5s = setInterval(() => {
			const GameAfter500ms = getGame(usnm)
			if (GameAfter500ms && !sent) {
				res.status(200).send(getUserGame(GameAfter500ms.get(), usnm))
				sent = true
			}
		}, 500)
		time(5000).then(() => {
			if (!sent) {
				res.status(200).send({
					team: "",
					turn: "",
					moveToExec: ""
				})
				sent = true
			}
			clearInterval(Every5s)
		})
	}
})

app.post("/SetMove", (req, res) => {
	const {
		usnm,
		team,
		move
	}: { usnm: string; team: "white" | "black"; move: string } = req.body

	if (!usnm) return res.status(400).send("params/usnm-required")
	if (!team) return res.status(400).send("params/team-required")
	if (!move) return res.status(400).send("params/move-required")

	const Sender = getUser(usnm)
	const Game = getGame(usnm)

	if (Sender && Game) {
		/**
		 * Set current team move to move in params
		 * Empty opponent's move since it has already been executed
		 * Change the turn object to the opponent to allow them to continue
		 */
		if (team === "white") {
			Game.set({
				...Game.get(),
				whiteMove: move,
				blackMove: "",
				turn: "black"
			})
			return res.status(200).send()
		}
		if (team === "black") {
			Game.set({
				...Game.get(),
				blackMove: move,
				whiteMove: "",
				turn: "white"
			})
			return res.status(200).send()
		}
	} else {
		res.status(400).send("auth/user-or-game-not-found")
	}
})

// * Start server
const server = http.createServer(app)
const io = require("socket.io")(server)

io.on('connection', (socket: any) => {
	const interval = setInterval(() => {
		io.emit("users", JSON.stringify(ServerUsers.get(), null, 4))
		io.emit("games", JSON.stringify(ServerGames, null, 4))
	}, 1000)

	socket.on("disconnect", () => {
		clearInterval(interval)
	})
})

app.get("/users", (_req, res) => {
	res.sendFile(path.join(__dirname + '/users.html'));
})
app.get("/games", (_req, res) => {
	res.sendFile(path.join(__dirname + '/games.html'));
})

server.listen(PORT, () => console.log(`Sever runnnig on ${PORT}`))

/**
 * Function to get User object from ServerUsers by username
 * @param usnm Username
 */
const getUser = (usnm: String): User | null => {
	for (let i = 0; i < ServerUsers.get().length; i++) {
		const usr = ServerUsers.get()[i]
		if (usr.usnm == usnm) {
			return usr
		}
	}

	return null
}

/**
 * Function to get a Game Object from ServerGames by username
 * @param usnm Username
 */
const getGame = (usnm: String): Observable<Game> | null => {
	for (let i = 0; i < ServerGames.length; i++) {
		const Game = ServerGames[i].get()
		if (Game.black == usnm) return ServerGames[i]
		if (Game.white == usnm) return ServerGames[i]
	}

	return null
}

/**
 * Function to return details related to the specific user's username
 * @param game Game object which gets extracted in this function
 */
const getUserGame = (game: Game, usnm: string) => {
	const team = game.black == usnm ? "black" : "white"
	const moveToExec = team == "white" ? game.blackMove : game.whiteMove

	return {
		team,
		turn: game.turn,
		moveToExec
	}
}