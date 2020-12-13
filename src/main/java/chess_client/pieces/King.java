package chess_client.pieces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chess_client.board.*;
import chess_client.board.Move.*;

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

            if (ColExceptions(position(), offset))
                continue;

            if (BoardUtils.is_valid_position(test_position)) {
                final Tile test_tile = board.tile(test_position);

                if (test_tile.is_occupied()) {
                    Piece piece_on_tile = board.tile(test_position).piece();
                    if (piece_on_tile.team() != this.team()) {
                        moves.add(new Attack("main", board, this.position(), piece_on_tile.position()));
                    }
                } else {
                    moves.add(new Major("main", board, this.position(), test_position));
                }
            }
        }

        if (this.move_count() == 0) {
            Tile left_tile = board.tile(this.position() - 4);
            Tile right_tile = board.tile(this.position() + 3);
            if (left_tile.is_occupied() && left_tile.piece().move_count() == 0 && left_tile.piece() instanceof Rook) {
                Piece left = left_tile.piece();
                boolean piece_between = false;
                for (int i = -3; i < 0; i++) {
                    Tile tile = board.tile(this.position() + i);
                    if (tile.is_occupied())
                        piece_between = true;
                }

                if (!piece_between) {
                    try {
                        Move move_1 = (new Major("test", board, this.position(), this.position() - 1)).deep_clone();
                        Move move_2 = (new Major("test", board, this.position(), this.position() - 2)).deep_clone();
                        Board test_1= move_1.simulate(board);
                        Board test_2 = move_2.simulate(board);
                        if (this.team().king_is_safe(test_1) && this.team().king_is_safe(test_2)) {
                            moves.add(new Castle("main", board, this.position(), this.position() - 2, left, left.position() + 3));
                        }
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (right_tile.is_occupied() && right_tile.piece().move_count() == 0
                    && right_tile.piece() instanceof Rook) {
                Piece right = right_tile.piece();
                boolean piece_between = false;
                for (int i = 1; i < 3; i++) {
                    Tile tile = board.tile(this.position() + i);
                    if (tile.is_occupied())
                        piece_between = true;
                }

                if (!piece_between) {
                    try {
                        Move move_1 = (new Major("test", board, this.position(), this.position() + 1)).deep_clone();
                        Move move_2 = (new Major("test", board, this.position(), this.position() + 2)).deep_clone();
                        Board test_1= move_1.simulate(board);
                        Board test_2 = move_2.simulate(board);
                        if (this.team().king_is_safe(test_1) && this.team().king_is_safe(test_2)) {
                            moves.add(new Castle("main", board, this.position(), this.position() + 2, right, right.position() -2));
                        }
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
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
        if (this.team().is_black())
            return "♔";
        return "♚";
    }

    public String typeKey() {
        return "King";
    }

}
