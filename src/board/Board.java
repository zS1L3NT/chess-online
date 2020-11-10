package board;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

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
            empty_tile(i);
        if (tiles.length == 1)
            board = tiles[0];
        else
            game.test(this);
    }

    public void print() {
        System.out.println("  " + name + " board");
        System.out.println("  +----+----+----+----+----+----+----+----+");

        for (int i = 0; i < 8; i++) {
            int col = i * 8;

            // Reverse numbers for each team
            System.out.print((game.move_maker.is_white() ? 8 - i : i + 1) + " ");

            for (int j = 0; j < 8; j++) {
                int position = col + j;

                // Reverse tile print for each team
                Tile tile = this.board[game.move_maker.is_white() ? position : 63 - position];

                System.out.print("| ");


                
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

    public void empty_tile(int position) {
        this.board[position] = new Tile(position, null);
    }

    public Tile tile(int position) {
        return this.board[position];
    }

    public void execute(Move move) {
        Piece predator = tile(move.location()).piece();
        this.empty_tile(move.location());
        this.set_tile(predator, move.destination());
        predator.set_position(move.destination());
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
