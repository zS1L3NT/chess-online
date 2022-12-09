package chess_client.pieces;

import java.util.ArrayList;
import java.util.List;

import chess_client.board.*;
import chess_client.board.Move.*;

public class Rook extends Piece {
    /**
     *
     */
    private static final long serialVersionUID = -5830989824031475015L;
    final int[] directions = { -8, 1, 8, -1 };

    public Rook(int position, Team team) {
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
        return BoardUtils.IN_FIRST_COL(position) && offset == -1;
    }

    private static boolean EighthColException(int position, int offset) {
        return BoardUtils.IN_EIGHTH_COL(position) && offset == 1;
    }

    @Override
    public String toString() {
        return "Rook(" + BoardUtils.to_board_code(this.position()) + ")";
    }

    public String boardKey() {
        if (this.team().is_black()) return "♖";
        return "♜";
    }

    public String typeKey() {
        return "Rook";
    }
    
}
