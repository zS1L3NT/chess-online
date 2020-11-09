package board;

import board.Move.AttackMove;
import board.Move.MajorMove;
import board.Tile.*;

public class Board {
    private Tile[] board = new Tile[64];
    public GameMaster game;
    public String name;

    private void set_name(String name) {
        this.name = name;
    }

    public Board(String name, Tile[]... tiles) {
        set_name(name);
        game = new GameMaster();
        for (int i = 0; i < 64; i++) empty_tile(i);
        if (tiles.length == 1) board = tiles[0];
        else game.test(this);
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
        this.board[position] = new OccuTile(position, piece);
    }

    public void empty_tile(int position) {
        this.board[position] = new EmptyTile(position, null);
    }

    public Tile getTile(int position) {
        return this.board[position];
    }

    public void execute(Move move) {
        if (move instanceof MajorMove) execute((MajorMove) move);
        if (move instanceof AttackMove) execute((AttackMove) move);
    }

    private void execute(MajorMove copy_me) {
        MajorMove move = new MajorMove(copy_me.board, copy_me.predator, copy_me.destination);
        this.empty_tile(move.predator.position);
        this.set_tile(move.predator, move.destination);
    }

    private void execute(AttackMove copy_me) {
        AttackMove move = new AttackMove(copy_me.board, copy_me.predator, copy_me.prey);
        this.empty_tile(move.predator.position);
        this.set_tile(move.predator, move.prey.position);
    }
}
