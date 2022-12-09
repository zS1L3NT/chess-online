package chess_client.board;

import java.io.Serializable;

import chess_client.pieces.*;

public class GameMaster implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6376908339572270453L;
    public Team player;
    public Piece current_selected;
    public Board board;

    public GameMaster(Board board, Team player) {
        this.board = board;
        this.player = player;
    }

    public void set_current_selected(Piece piece) {
        this.current_selected = piece;
    }

    /**
     * Full method to check if game is going to end
     * Used in main App.java class as a test to end game
     * @return true if game should end
     */
    public boolean test_if_will_end() {
        if (this.player.all_safe_moves(this.board).size() == 0) {
            if (this.player.king_is_safe(this.board)) {
                System.out.print(Color.YELLOW);
                System.out.println("Stalemate!");
                System.out.print(Color.RESET);
                System.exit(1);
            } else {
                System.out.print(Color.RED);
                System.out.println("Checkmate!");
                System.out.print(Color.RESET);
                System.out.println("Winner: " + this.player.enemy() + "!");
                System.exit(1);
            }
        }

        if (!this.player.can_checkmate(this.board) && !this.player.enemy().can_checkmate(this.board)) {
            System.out.print(Color.BLUE);
            System.out.println("Draw!");
            System.out.print(Color.RESET);
            System.exit(1);
        }

        return false;
    }

    /**
     * Method to set up board properly
     * @param board
     */
    public void set_up(Board board) {
        board.set_tile(new Rook(0, Team.BLACK), 0);
        board.set_tile(new Knight(1, Team.BLACK), 1);
        board.set_tile(new Bishop(2, Team.BLACK), 2);
        board.set_tile(new Queen(3, Team.BLACK), 3);
        board.set_tile(new King(4, Team.BLACK), 4);
        board.set_tile(new Bishop(5, Team.BLACK), 5);
        board.set_tile(new Knight(6, Team.BLACK), 6);
        board.set_tile(new Rook(7, Team.BLACK), 7);
        for (int i = 8; i < 16; i++) {
            board.set_tile(new Pawn(i, Team.BLACK), i);
        }
        for (int i = 48; i < 56; i++) {
            board.set_tile(new Pawn(i, Team.WHITE), i);
        }
        board.set_tile(new Rook(56, Team.WHITE), 56);
        board.set_tile(new Knight(57, Team.WHITE), 57);
        board.set_tile(new Bishop(58, Team.WHITE), 58);
        board.set_tile(new Queen(59, Team.WHITE), 59);
        board.set_tile(new King(60, Team.WHITE), 60);
        board.set_tile(new Bishop(61, Team.WHITE), 61);
        board.set_tile(new Knight(62, Team.WHITE), 62);
        board.set_tile(new Rook(63, Team.WHITE), 63);
    }
}
