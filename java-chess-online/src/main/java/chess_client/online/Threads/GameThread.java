package chess_client.online.Threads;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import chess_client.ChessStarter;
import chess_client.board.Move;
import chess_client.board.Team;
import chess_client.online.HTTP;

public class GameThread extends Thread {
    private String host, usnm;
    private String data = "{\"team\":\"\",\"turn\":\"\",\"moveToExec\":\"\"}";
    private boolean moveExecuted = false;

    public GameThread(String host, String usnm) {
        this.host = host;
        this.usnm = usnm;
    }

    @Override
    public void run() {
        HashMap<String, String> params = HTTP.MakeParamsWithUsnm(usnm);

        try {
            this.data = HTTP.GET(this.host + "/GetGameState", params);

            while (true) {
                this.data = HTTP.GET(this.host + "/UpdateOnGameState", params);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean dataIsEmpty() {
        return this.data.equals("{\"team\":\"\",\"turn\":\"\",\"moveToExec\":\"\"}");
    }

    /**
     * Gets team from this.data
     * 
     * @return Current team
     */
    public Team team() {
        try {
            JSONObject OBJ = new JSONObject(this.data);
            String team = OBJ.getString("team");

            if (team.equals("white"))
                return Team.WHITE;
            if (team.equals("black"))
                return Team.BLACK;

            return null;
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * Gets turn from this.data
     * 
     * @return Current turn
     */
    public Team turn() {
        try {
            JSONObject OBJ = new JSONObject(this.data);
            String turn = OBJ.getString("turn");

            if (turn.equals("white"))
                return Team.WHITE;
            if (turn.equals("black"))
                return Team.BLACK;

            return null;
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * Get's move from this.data
     * 
     * @return Move in JSON
     */
    public String moveStringToExec() {
        try {
            JSONObject OBJ = new JSONObject(this.data);
            return OBJ.getString("moveToExec");
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * Start a new Chess game using the ChessStarter class
     */
    public void startGame() {
        if (dataIsEmpty()) {
            wait(5000);
        }
        ChessStarter starter = new ChessStarter(team());
        starter.start();

        while (true) {
            wait(1000);
            if (dataIsEmpty()) {
                System.out.println("\nOpponent disconnected");
                System.exit(1);
            }
            if (team().equals(turn())) {
                // System.out.println(" Void A (team().equals(turn())\n");
                // Your turn to move

                // Starter hasn't registered your turn yet
                if (!starter.isYourTurn && !this.moveExecuted && moveStringToExec() != "") {
                    // System.out.println(" Void B (!starter.isYourTurn && moveStringToExec() !=
                    // \"\"\n");
                    starter.executeOpponentMove(moveStringToExec());
                    starter.setYourTurn();
                    this.moveExecuted = true;
                }

                if (starter.move() != null) {
                    // System.out.println(" Void C (starter.move() != null)\n");

                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("usnm", this.usnm);
                    params.put("team", team().is_white() ? "white" : "black");
                    params.put("move", Move.toJSON(starter.move()));

                    try {
                        HTTP.POST(this.host + "/SetMove", params);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    starter.setMoveNull();
                }
            } else {
                // Waiting for enemy's move
                // Hasn't sent move over HTTP yet
                // starter.setEnemyTurn();
                // System.out.println(" Void D (!team().equals(turn())\n");
                this.moveExecuted = false;
            }
        }
    }

    /**
     * Wait in milliseconds
     * 
     * @param ms
     */
    public final static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}
