package board;

public abstract class Move {
    public Board board;
    public Piece predator;
    public int destination;

    public Move(Board board, Piece predator, int destination) {
        this.board = board;
        this.predator = predator;
        this.destination = destination;
    }

    public abstract void execute();
    @Override
    public abstract String toString();

    public static class MajorMove extends Move {

        public MajorMove(Board board, Piece predator, int destination) {
            super(board, predator, destination);
        }

        public void execute() {
            this.board.empty_tile(predator.position);
            this.board.set_tile(predator, this.destination);
            predator.set_position(this.destination);
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

        public void execute() {
            this.board.empty_tile(predator.position);
            this.board.set_tile(predator, prey.position);
            predator.set_position(prey.position);
        }

        @Override
        public String toString() {
            return "Attack Move by " + predator + " against " + prey;
        }

    }
}
