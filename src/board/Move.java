package board;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class Move implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -7188724240010690519L;
    private Board board;
    private int location;
    private int destination;
    private String name;

    public void set_name(String name) {
        this.name = name;
    }

    public Move(String name, Board board, int location, int destination) {
        set_name(name);
        this.board = board;
        this.location = location;
        this.destination = destination;
    }

    public Board simulate(Board board) {
        Board x;
        try {
            x = board.deep_clone();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return board;
        }
        x.execute(this);
        return x;
    }

    public String name() {
        return this.name;
    }

    public Board board() {
        return this.board;
    }

    public int location() {
        return this.location;
    }

    public int destination() {
        return this.destination;
    }

    public Move deep_clone() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(this);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);
        return (Move) in.readObject();
    }

    public static class EnPassant extends Move {

        /**
         *
         */
        private static final long serialVersionUID = 1L;
        private Piece prey;

        public EnPassant(String name, Board board, int location, int destination, Piece prey) {
            super(name, board, location, destination);
            this.prey = prey;
        }

        @Override
        public String toString() {
            return "En Passant by " + this.board().tile(this.location()) + " against " + prey;
        }

        public Piece prey() {
            return prey;
        }

    }

    public static class Attack extends Move {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public Attack(String name, Board board, int location, int destination) {
            super(name, board, location, destination);
        }

        @Override
        public String toString() {
            return "Attack by " + this.board().tile(this.location()).piece() + " against "
                    + this.board().tile(this.destination()).piece();
        }

    }

    public static class Major extends Move {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public Major(String name, Board board, int location, int destination) {
            super(name, board, location, destination);
        }

        @Override
        public String toString() {
            return "Major by " + this.board().tile(this.location()).piece() + " to " + this.destination();
        }

    }
}
