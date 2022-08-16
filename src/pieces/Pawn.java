package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Board;
import board.BoardUtils;
import board.Move;
import board.Move.Attack;
import board.Move.EnPassant;
import board.Move.Major;
import board.Piece;
import board.Team;
import board.Tile;

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
                moves.add(new Major("main", board, this.position(), test_position));
            } else if (offset == 16 && StartingException(position())) {
                // * Double forward
                // Check if the pawn has a clear jump path
                Tile inbetween = board.tile(this.position() + offset * this.team().direction());
                if (inbetween.is_empty() && test_tile.is_empty()) {
                    moves.add(new Major("main", board, this.position(), test_position));
                }
            } else if (offset == 7 && !SevenException(offset)) {
                // * Sideways Attack move
                if (test_tile.is_occupied() && test_tile.piece().team() != this.team()) {
                    moves.add(new Attack("main", board, this.position(), test_tile.piece().position()));
                }
            } else if (offset == 9 && !NineException(offset)) {
                // * Sideways Attack move
                if (test_tile.is_occupied() && test_tile.piece().team() != this.team()) {
                    moves.add(new Attack("main", board, this.position(), test_tile.piece().position()));
                }
            }
            if (predator_en_passant_exception(this.position())) {
                // In row ready for en passant
                Tile left_tile = board.tile(this.position() + (1 * this.team().direction()));
                Tile right_tile = board.tile(this.position() - (1 * this.team().direction()));

                if (!NineException(this.position()) && left_tile.is_occupied() && left_tile.piece().move_count() == 1) {
                    Piece left = left_tile.piece();
                    int dest = this.position() + (9 * this.team().direction());
                    moves.add(new EnPassant("main", board, this.position(), dest, left));
                }
                if (!SevenException(this.position()) && right_tile.is_occupied() && right_tile.piece().move_count() == 1) {
                    Piece right = right_tile.piece();
                    int dest = this.position() + (7 * this.team().direction());
                    moves.add(new EnPassant("main", board, this.position(), dest, right));
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

    public boolean prey_en_passant_exception(int position) {
        return (this.team().is_white() && BoardUtils.IN_FIFTH_ROW(position))
                || (this.team().is_black() && BoardUtils.IN_FOURTH_ROW(position));
    }

    public boolean predator_en_passant_exception(int position) {
        return (this.team().is_black() && BoardUtils.IN_FIFTH_ROW(position))
                || (this.team().is_white() && BoardUtils.IN_FOURTH_ROW(position));
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
        if (this.team().is_black())
            return "p";
        return "P";
    }

    public String typeKey() {
        return "Pawn";
    }

}
