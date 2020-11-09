package board;

public class Move {
    public Board board;
    public Piece predator;
    public int destination;

    public Move(Board board, Piece predator, int destination) {
        this.board = board;
        this.predator = predator;
        this.destination = destination;
    }

    // @Override
    // public abstract String toString();

    public Board simulate(Board board) {
        Board x = new Board("test", board.copy());
        x.execute(this);
        return x;
    }

    public static class MajorMove extends Move {

        public MajorMove(Board board, Piece predator, int destination) {
            super(board, predator, destination);
        }

        @Override
        public String toString() {
            return "Major Move by " + predator + " to " + BoardUtils.to_board_code(destination);
        }

    }

    public static class AttackMove extends Move {
        public Piece prey;

        public AttackMove(Board board, Piece predator, Piece prey) {
            super(board, predator, prey.position);
            this.prey = prey;
        }

        @Override
        public String toString() {
            return "Attack Move by " + predator + " against " + prey;
        }

    }
}
