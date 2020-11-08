package board;

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

    public Team enemy() {
        return this == WHITE ? BLACK : WHITE;
    }

    @Override
    public abstract String toString();
}
