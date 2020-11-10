package board;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Move implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -7188724240010690519L;
    private Board board;
    private int location;
    private int destination;

    public Move(Board board, int location, int destination) {
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

    @Override
    public String toString() {
        if (this.board.tile(destination()).is_occupied())
            return "Attack by " + this.board.tile(location()).piece() + " against " + this.board.tile(destination()).piece();
        else
            return "Move by " + this.board.tile(location()).piece() + " to " + BoardUtils.to_board_code(destination());
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
}
