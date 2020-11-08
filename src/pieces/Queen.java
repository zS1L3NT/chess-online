package pieces;

import java.util.ArrayList;
import java.util.List;

import board.*;
import board.Move.*;

public class Queen extends Piece {
    final private int[] directions = { -9, -8, -7, -1, 1, 7, 8, 9 };

    public Queen(int position, Team team) {
        super(position, team);
    }

    @Override
    public List<Move> legal_moves(Board board) {
        List<Move> moves = new ArrayList<Move>();
        for (int offset : directions) {
            int test_position = this.position;
            while (BoardUtils.is_valid_position(test_position)) {

                if (ColExceptions(test_position, offset))
                    break;

                test_position += offset;
                if (BoardUtils.is_valid_position(test_position)) {
                    final Tile test_tile = board.getTile(test_position);

                    if (test_tile.is_occupied()) {
                        Piece piece_on_tile = board.getTile(test_position).piece;
                        if (piece_on_tile.team != this.team) {
                            moves.add(new AttackMove(board, this, piece_on_tile));
                        }
                        break;
                    } else {
                        moves.add(new MajorMove(board, this, test_position));
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
        int[] invalid_offsets = { -9, 7, -1 };
        return BoardUtils.IN_FIRST_COL(position) && BoardUtils.array_contains(invalid_offsets, offset);
    }

    private static boolean EighthColException(int position, int offset) {
        int[] invalid_offsets = { 9, -7, 1 };
        return BoardUtils.IN_EIGHTH_COL(position) && BoardUtils.array_contains(invalid_offsets, offset);
    }

    @Override
    public String toString() {
        return "Queen(" + BoardUtils.to_board_code(this.position) + ")";
    }

    public String boardKey() {
        if (this.team.is_black()) return "♕";
        return "♛";
    }
}
