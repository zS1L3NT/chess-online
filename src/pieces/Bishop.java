package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Board;
import board.BoardUtils;
import board.Move;
import board.Move.Attack;
import board.Move.Major;
import board.Piece;
import board.Team;
import board.Tile;

public class Bishop extends Piece {
    /**
     *
     */
    private static final long serialVersionUID = -4216367299592571251L;
    final private int[] directions = { -9, -7, 7, 9 };

    public Bishop(int position, Team team) {
        super(position, team);
    }

    @Override
    public List<Move> legal_moves(Board board) {
        List<Move> moves = new ArrayList<Move>();
        for (int offset : directions) {
            int test_position = this.position();
            while (BoardUtils.is_valid_position(test_position)) {

                if (ColExceptions(test_position, offset))
                    break;

                test_position += offset;
                if (BoardUtils.is_valid_position(test_position)) {
                    final Tile test_tile = board.tile(test_position);

                    if (test_tile.is_occupied()) {
                        Piece piece_on_tile = board.tile(test_position).piece();
                        if (piece_on_tile.team() != this.team()) {
                            moves.add(new Attack("main", board, this.position(), piece_on_tile.position()));
                        }
                        break;
                    } else {
                        moves.add(new Major("main", board, this.position(), test_position));
                    }

                }

            }
        }
        return moves;
    }

    private boolean ColExceptions(int position, int offset) {
        return FirstColException(position, offset) || EighthColException(position, offset);
    }

    private static boolean FirstColException(int position, int offset) {
        int[] invalid_offsets = { -9, 7 };
        return BoardUtils.IN_FIRST_COL(position) && BoardUtils.array_contains(invalid_offsets, offset);
    }

    private static boolean EighthColException(int position, int offset) {
        int[] invalid_offsets = { 9, -7 };
        return BoardUtils.IN_EIGHTH_COL(position) && BoardUtils.array_contains(invalid_offsets, offset);
    }

    @Override
    public String toString() {
        return "Bishop(" + BoardUtils.to_board_code(this.position()) + ")";
    }

    public String boardKey() {
        if (this.team().is_black()) return "b";
        return "B";
    }

    @Override
    public String typeKey() {
        return "Bishop";
    }
}
