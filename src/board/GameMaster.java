package board;

import java.io.Serializable;

import pieces.*;

public class GameMaster implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6376908339572270453L;
    public Team move_maker;
    public Piece current_selected;

    public GameMaster() {
        move_maker = Team.WHITE;
    }

    public void set_current_selected(Piece piece) {
        this.current_selected = piece;
    }

    public void change_move_maker() {
        this.move_maker = this.move_maker.is_white() ? Team.BLACK : Team.WHITE;
    }

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

    // * Test
    public void test(Board board) {
        board.set_tile(new King(4, Team.BLACK), 4);
        board.set_tile(new King(60, Team.WHITE), 60);
        board.set_tile(new Pawn(11, Team.WHITE), 11);
    }
}
