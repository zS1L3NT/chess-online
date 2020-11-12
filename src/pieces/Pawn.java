package pieces;

import java.util.ArrayList;
import java.util.List;

import board.*;
import board.Move;

public class Pawn extends Piece {
    /**
     *
     */
    private static final long serialVersionUID = 6248169582197274181L;
    final int[] directions = { 7, 8, 9, 16 };

    public Pawn(int position, Team team) {
        super(position, team);
    }

    @Override
    public List<Move> legal_moves(Board board) {
        List<Move> moves = new ArrayList<Move>();
        for (int offset : directions) {
            int test_position = this.position() + offset * this.team().direction();
            if (!BoardUtils.is_valid_position(test_position))
                continue;
            Tile test_tile = board.tile(test_position);
            if (offset == 8 && test_tile.is_empty()) {
                // * Single forward
                moves.add(new Move("main", board, this.position(), test_position));
            }
            else if (offset == 16 && StartingException(position())) {
                // * Double forward
                // Check if the pawn has a clear jump path
                Tile inbetween = board.tile(this.position() + offset * this.team().direction());
                if (inbetween.is_empty() && test_tile.is_empty()) {
                    moves.add(new Move("main", board, this.position(), test_position));
                }
            }
            else if (offset == 7 && !SevenException(offset)) {
                // * Sideways Attack move
                if (test_tile.is_occupied() && test_tile.piece().team() != this.team()) {
                    moves.add(new Move("main", board, this.position(), test_tile.piece().position()));
                }
            }
            else if (offset == 9 && !NineException(offset)) {
                // * Sideways Attack move
                if (test_tile.is_occupied() && test_tile.piece().team() != this.team()) {
                    moves.add(new Move("main", board, this.position(), test_tile.piece().position()));
                }
            }
        }
        return moves;
    }

    private boolean StartingException(int position) {
        return (this.team().is_black() && BoardUtils.IN_SECOND_ROW(position))
                || (this.team().is_white() && BoardUtils.IN_SEVENTH_ROW(position));
    }

    public boolean promotion_exception(int position) {
        return (this.team().is_black() && BoardUtils.IN_SEVENTH_ROW(position))
                || (this.team().is_white() && BoardUtils.IN_SECOND_ROW(position));
    }

    private boolean SevenException(int offset) {
        return (BoardUtils.IN_EIGHTH_COL(this.position()) && this.team().is_white())
            || (BoardUtils.IN_FIRST_COL(this.position()) && this.team().is_black());
    }

    private boolean NineException(int offset) {
        return (BoardUtils.IN_FIRST_COL(this.position()) && this.team().is_white())
            || (BoardUtils.IN_EIGHTH_COL(this.position()) && this.team().is_black());
    }

    @Override
    public String toString() {
        return "Pawn(" + BoardUtils.to_board_code(this.position()) + ")";
    }

    public String boardKey() {
        if (this.team().is_black()) return "♙";
        return "♟";
    }

}
