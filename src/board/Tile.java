package board;

import java.io.Serializable;

public class Tile implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 2400831564997387196L;
    private int position;
    private Piece piece;

    public Tile(int position, Piece piece) {
        this.position = position;
        this.piece = piece;
        if (is_occupied()) this.piece.set_position(position);
    }
    
    public boolean is_occupied() {
        return this.piece != null;
    }

    public boolean is_empty() {
        return this.piece == null;
    }

    @Override
    public String toString() {
        if (is_empty()) return "Empty Tile";
        return "Tile with " + this.piece;
    }

    public String boardKey() {
        if (is_empty()) return " ";
        return this.piece.boardKey();
    }

    public int position() {
        return this.position;
    }

    public Piece piece() {
        return this.piece;
    }

    // public Tile deep_clone() throws IOException, ClassNotFoundException {
    //     ByteArrayOutputStream bos = new ByteArrayOutputStream();
    //     ObjectOutputStream out = new ObjectOutputStream(bos);
    //     out.writeObject(this);

    //     ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    //     ObjectInputStream in = new ObjectInputStream(bis);
    //     return (Tile) in.readObject();
    // }
    
}
