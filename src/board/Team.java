package board;

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
            Tile tile = board.getTile(i);
            if (tile.is_empty())
                continue;

            Piece piece = tile.piece;
            if (piece.team != this)
                continue;

            if (piece instanceof King)
                return piece.position;
        }
        return -1;
    }

    public Team enemy() {
        return this == WHITE ? BLACK : WHITE;
    }

    @Override
    public abstract String toString();
}
