package board;

public abstract class Tile {
    public int position;
    public Piece piece;

    public Tile(int position, Piece piece) {
        this.position = position;
        this.piece = piece;
    }

    public boolean is_empty() {
        return !is_occupied();
    }
    
    public abstract boolean is_occupied();
    public abstract String toString();
    public abstract String boardKey();

    public static class OccuTile extends Tile {

        public OccuTile(int position, Piece piece) {
            super(position, piece);
        }
    
        @Override
        public boolean is_occupied() {
            return true;
        }

        @Override
        public String toString() {
            return "Tile with " + piece;
        }

        public String boardKey() {
            return piece.boardKey();
        }

        
    }

    public static class EmptyTile extends Tile {

        public EmptyTile(int position, Piece piece) {
            super(position, piece);
        }
    
        @Override
        public boolean is_occupied() {
            return false;
        }

        @Override
        public String toString() {
            return "Empty Tile";
        }

        public String boardKey() {
            return " ";
        }
    
    }
    
}
