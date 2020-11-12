package pieces;

import java.util.ArrayList;
import java.util.List;

import board.*;
import board.Move;

public class King extends Piece {
    /**
     *
     */
    private static final long serialVersionUID = -5148296735193417356L;
    final private int[] directions = { -9, -8, -7, -1, 1, 7, 8, 9 };

    public King(int position, Team team) {
        super(position, team);
    }

    @Override
    public List<Move> legal_moves(Board board) {
        List<Move> moves = new ArrayList<Move>();
        for (int offset : directions) {
            int test_position = this.position() + offset;

            if (ColExceptions(position(), offset)) continue;

            if (BoardUtils.is_valid_position(test_position)) {
                final Tile test_tile = board.tile(test_position);
                
                if (test_tile.is_occupied()) {
                    Piece piece_on_tile = board.tile(test_position).piece();
                    if (piece_on_tile.team() != this.team()) {
                        moves.add(new Move("main", board, this.position(), piece_on_tile.position()));
                    }
                } else {
                    moves.add(new Move("main", board, this.position(), test_position));
                }
            }
        }

        return moves;
    }

    private static boolean ColExceptions(int position, int offset) {
        return FirstColException(position, offset) || EighthColException(position, offset);
    }

    private static boolean FirstColException(int position, int offset) {
        int[] invalid_offsets = { 7, -1, -9 };
        return BoardUtils.IN_FIRST_COL(position) && BoardUtils.array_contains(invalid_offsets, offset);
    }

    private static boolean EighthColException(int position, int offset) {
        int[] invalid_offsets = { -7, 1, 9 };
        return BoardUtils.IN_EIGHTH_COL(position) && BoardUtils.array_contains(invalid_offsets, offset);
    }

    @Override
    public String toString() {
        return "King(" + BoardUtils.to_board_code(this.position()) + ")";
    }

    public String boardKey() {
        if (this.team().is_black()) return "♔";
        return "♚";
    }

}
