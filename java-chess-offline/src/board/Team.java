package board;

import java.util.ArrayList;
import java.util.List;

import pieces.King;

public enum Team {
    BLACK {
        @Override
        public int direction() {
            return 1;
        }

        @Override
        public boolean is_white() {
            return false;
        }

        @Override
        public String toString() {
            return "BLACK";
        };
    },
    WHITE {
        @Override
        public int direction() {
            return -1;
        }

        @Override
        public boolean is_white() {
            return true;
        }

        @Override
        public String toString() {
            return "WHITE";
        };
    };

    public abstract int direction();

    public abstract boolean is_white();

    public boolean is_black() {
        return !is_white();
    }

    public int king_position(Board board) {
        for (int i = 0; i < 64; i++) {
            Tile tile = board.tile(i);
            if (tile.is_empty())
                continue;

            Piece piece = tile.piece();
            if (piece.team() != this)
                continue;

            if (piece instanceof King)
                return piece.position();
        }
        return -1;
    }

    public List<Move> all_legal_moves(Board board) {
        List<Move> moves = new ArrayList<Move>();
        for (int i = 0; i < 64; i++) {
            Tile tile = board.tile(i);

            // Skip if tile is empty
            if (tile.is_empty())
                continue;

            // Skip if tile isnt on your team
            if (tile.piece().team() != this)
                continue;

            List<Move> legal_moves = tile.piece().legal_moves(board);
            if (legal_moves.size() == 0)
                continue;
            for (int j = 0; j < legal_moves.size(); j++)
                moves.add(legal_moves.get(j));
        }

        return moves;
    }

    public List<Move> all_safe_moves(Board board) {
        List<Move> moves = new ArrayList<Move>();
        for (int i = 0; i < 64; i++) {
            Tile tile = board.tile(i);

            // Skip if tile is empty
            if (tile.is_empty())
                continue;

            // Skip if tile isnt on your team
            if (tile.piece().team() != this)
                continue;

            List<Move> safe_moves = tile.piece().safe_moves(board);
            if (safe_moves.size() == 0)
                continue;
            for (int j = 0; j < safe_moves.size(); j++)
                moves.add(safe_moves.get(j));
        }

        return moves;
    }

    public boolean king_is_safe(Board board) {
        return king_predators(board).size() == 0;
    }

    public List<Piece> king_predators(Board board) {
        List<Move> enemy_moves = enemy().all_legal_moves(board);
        List<Piece> predators = new ArrayList<Piece>();

        for (int i = 0; i < enemy_moves.size(); i++) {
            Move move = enemy_moves.get(i);
            if (move.destination() == king_position(board))
                predators.add(board.tile(move.location()).piece());
        }

        return predators;
    }

    public Team enemy() {
        return this == WHITE ? BLACK : WHITE;
    }

    public boolean can_checkmate(Board board) {
        List<String> types = new ArrayList<String>();

        for (int i = 0; i < 64; i++) {
            Tile tile = board.tile(i);

            if (tile.is_occupied()) {
                Piece piece = tile.piece();
                if (piece.team() == this) {
                    types.add(piece.typeKey());
                }
            }
        }

        // King alone
        if (types.size() == 1) return false;

        // King and 2 Knights
        if (types.size() == 3 && frequency(types, "Knight") == 2) return false;

        // King and 1 Knight
        if (types.size() == 2 & frequency(types, "Knight") == 1) return false;

        // King and 1 Bishop
        if (types.size() == 2 & frequency(types, "Bishop") == 1) return false;

        return true;
    }

    private <T> int frequency(List<T> list, T value) {
        int count = 0;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == value) count++;
        }

        return count;
    }

    @Override
    public abstract String toString();
}
