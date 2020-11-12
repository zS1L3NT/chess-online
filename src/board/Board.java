package board;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Scanner;

import pieces.*;

public class Board implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 392369079414138857L;
    private Tile[] board = new Tile[64];
    public GameMaster game;
    public String name;

    private void set_name(String name) {
        this.name = name;
    }

    public Board(String name, Tile[]... tiles) {
        set_name(name);
        game = new GameMaster();
        for (int i = 0; i < 64; i++)
            this.board[i] = new Tile(i, null);
        if (tiles.length == 1)
            board = tiles[0];
        else
            game.set_up(this);
    }

    public void print(boolean color) {
        System.out.println("  +----+----+----+----+----+----+----+----+");
        List<Move> safe_moves = game.move_maker.all_safe_moves(this);
        List<Move> piece_safe_moves = null;
        if (game.current_selected != null)
            piece_safe_moves = game.current_selected.safe_moves(this);

        for (int i = 0; i < 8; i++) {
            int col = i * 8;

            // Reverse numbers for each team
            System.out.print((game.move_maker.is_white() ? 8 - i : i + 1) + " ");

            for (int j = 0; j < 8; j++) {
                int position = col + j;

                // Reverse tile print for each team
                Tile tile = this.board[game.move_maker.is_white() ? position : 63 - position];

                System.out.print("| ");

                if (color) {
                    if (game.current_selected == null) {
                        for (int k = 0; k < safe_moves.size(); k++) {
                            Move move = safe_moves.get(k);
                            if (tile.position() == move.location())
                                System.out.print(Color.GREEN_BACKGROUND);
                        }
                    } else {
                        for (int k = 0; k < piece_safe_moves.size(); k++) {
                            Move move = piece_safe_moves.get(k);
                            if (move.destination() == tile.position()) {
                                if (tile.is_occupied())
                                    System.out.print(Color.RED_BACKGROUND);
                                else
                                    System.out.print(Color.GREEN_BACKGROUND);
                            }
                        }
                    }
                }

                if (tile.is_occupied())
                    System.out.print(tile.boardKey() + " ");
                else
                    System.out.print("  ");
                System.out.print(Color.RESET);

                System.out.print(" ");

            }
            System.out.println("|");
            System.out.println("  +----+----+----+----+----+----+----+----+");
        }
        if (game.move_maker.is_white())
            System.out.println("    A    B    C    D    E    F    G    H");
        else
            System.out.println("    H    G    F    E    D    C    B    A ");
        System.out.println("\n");
    }

    public Tile[] copy() {
        return board;
    }

    public void set_tile(Piece piece, int position) {
        this.board[position] = new Tile(position, piece);
    }

    public Tile tile(int position) {
        return this.board[position];
    }

    public void execute(Move move) {
        Piece predator = tile(move.location()).piece();

        if (predator instanceof Pawn) {
            Pawn pawn = (Pawn) predator;
            if (pawn.promotion_exception(pawn.position()) && move.name().equals("main")) {
                Tile tile = promote(pawn);
                this.board[pawn.position()] = tile;
                this.game.current_selected = predator = tile.piece();
            }
        }

        this.board[move.location()] = new Tile(move.location(), null);
        this.set_tile(predator, move.destination());
        predator.set_position(move.destination());
        predator.add_move_count();
    }

    private Tile promote(Piece pawn) {
        System.out.println("\nPromote Pawn to:");
        System.out.println("Queen/Rook/Bishop/Knight");
        for (;;) {
            System.out.print("Type piece name: ");
            String name = input().nextLine();
            try {
                name = name.toLowerCase();
            } catch (Exception e) {
                System.out.println("Invalid name");
                continue;
            }
            if (name.equals("queen")) {
                return new Tile(pawn.position(), new Queen(pawn.position(), pawn.team()));
            } else if (name.equals("rook")) {
                return new Tile(pawn.position(), new Rook(pawn.position(), pawn.team()));
            } else if (name.equals("bishop")) {
                return new Tile(pawn.position(), new Bishop(pawn.position(), pawn.team()));
            } else if (name.equals("knight")) {
                return new Tile(pawn.position(), new Knight(pawn.position(), pawn.team()));
            } else {
                System.out.println("Invalid name");
                continue;
            }
        }
    }

    private static Scanner input() {
        return new Scanner(System.in);
    }

    public Board deep_clone() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(this);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);
        return (Board) in.readObject();
    }
}
