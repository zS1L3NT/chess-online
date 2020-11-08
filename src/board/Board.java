package board;

import board.Move.AttackMove;
import board.Move.MajorMove;
import board.Tile.*;

public class Board {
    private Tile[] board = new Tile[64];
    public GameMaster game;

    public Board() {
        game = new GameMaster();
        for (int i = 0; i < 64; i++) empty_tile(i);
        game.set_up(this);
    }
    public void print() {
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

                // During player turn highlight moves
                if (game.current_selected != null) {
                    if (game.current_selected.legal_positions(this).contains(tile.position)) {
                        if (tile.is_occupied())
                            System.out.print(Color.RED_BACKGROUND);
                        else
                            System.out.print(Color.GREEN_BACKGROUND);
                    }
                }

                // Highlight available pieces to move
                if (tile.piece != null && game.current_selected == null && game.move_maker == tile.piece.team) {
                    if (tile.piece.legal_moves(this).size() > 0) {
                        System.out.print(Color.GREEN_BACKGROUND);
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

    public int execute(Move move) {
        if (move instanceof MajorMove) return execute((MajorMove) move);
        if (move instanceof AttackMove) return execute((AttackMove) move);
        return 0;
    }

    private int execute(MajorMove move) {
        int old_position = move.predator.position;
        empty_tile(old_position);
        set_tile(move.predator, move.destination);
        move.predator.set_position(move.destination);
        return old_position;
    }

    private int execute(AttackMove move) {
        int old_position = move.predator.position;
        empty_tile(old_position);
        set_tile(move.predator, move.prey.position);
        move.predator.set_position(move.prey.position);
        return old_position;
    }
}
