package chess_client.board;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

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
            return "Major by " + this.board().tile(this.location()).piece() + " to "
                    + BoardUtils.to_board_code(this.destination());
        }

        /**
         * Used in converting executable move selected to json  
         */
        public static String getJSONFromMove(Major move) {
            JSONObject json = new JSONObject();
            try {
                json.put("type", "Major");
                json.put("location", move.location());
                json.put("destination", move.destination());
                return json.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Used in converting JSON from server to executable move
         */
        public static Major getMoveFromJSON(JSONObject OBJ, Board board) {
            try {
                int location = OBJ.getInt("location");
                int destination = OBJ.getInt("destination");
                return new Major("main", board, location, destination);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
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

        /**
         * Used in converting executable move selected to json  
         */
        public static String getJSONFromMove(Attack move) {
            JSONObject json = new JSONObject();
            try {
                json.put("type", "Attack");
                json.put("location", move.location());
                json.put("destination", move.destination());
                return json.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Used in converting JSON from server to executable move
         */
        public static Attack getMoveFromJSON(JSONObject OBJ, Board board) {
            try {
                int location = OBJ.getInt("location");
                int destination = OBJ.getInt("destination");
                return new Attack("main", board, location, destination);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    public static class Castle extends Move {

        /**
         *
         */
        private static final long serialVersionUID = 1L;
        private Piece rook;
        private int rook_dest;

        public Castle(String name, Board board, int location, int destination, Piece rook, int rook_dest) {
            super(name, board, location, destination);
            this.rook = rook;
            this.rook_dest = rook_dest;
        }

        public Piece rook() {
            return this.rook;
        }

        public int rook_dest() {
            return this.rook_dest;
        }

        @Override
        public String toString() {
            String direction = this.rook.position() < this.location() ? "Left" : "Right";
            return direction + " Castle by " + this.board().tile(this.location()).piece() + " and " + this.rook();
        }

        /**
         * Used in converting executable move selected to json  
         */
        public static String getJSONFromMove(Castle move) {
            JSONObject json = new JSONObject();
            try {
                json.put("type", "Castle");
                json.put("location", move.location());
                json.put("destination", move.destination());
                json.put("rook_pos", move.rook().position());
                json.put("rook_dest", move.rook_dest());
                return json.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Used in converting JSON from server to executable move
         */
        public static Castle getMoveFromJSON(JSONObject OBJ, Board board) {
            try {
                int location = OBJ.getInt("location");
                int destination = OBJ.getInt("destination");
                Piece rook = board.tile(OBJ.getInt("rook_pos")).piece();
                int rook_dest = OBJ.getInt("rook_dest");
                return new Castle("main", board, location, destination, rook, rook_dest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

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

        /**
         * Used in converting executable move selected to json  
         */
        public static String getJSONFromMove(EnPassant move) {
            JSONObject json = new JSONObject();
            try {
                json.put("type", "EnPassant");
                json.put("location", move.location());
                json.put("destination", move.destination());
                json.put("prey_pos", move.prey().position());
                return json.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Used in converting JSON from server to executable move
         */
        public static EnPassant getMoveFromJSON(JSONObject OBJ, Board board) {
            try {
                int location = OBJ.getInt("location");
                int destination = OBJ.getInt("destination");
                Piece prey = board.tile(OBJ.getInt("prey_pos")).piece();
                return new EnPassant("main", board, location, destination, prey);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    public static String toJSON(Move move) {
        if (move instanceof Major) return Major.getJSONFromMove((Major) move);
        if (move instanceof Attack) return Attack.getJSONFromMove((Attack) move);
        if (move instanceof Castle) return Castle.getJSONFromMove((Castle) move);
        if (move instanceof EnPassant) return EnPassant.getJSONFromMove((EnPassant) move);
        return null;
    }

    public static Move fromJSON(String json, Board board) throws JSONException {
        JSONObject OBJ = new JSONObject(json);
        String type = OBJ.getString("type");
        if (type.equals("Major")) return Major.getMoveFromJSON(OBJ, board);
        if (type.equals("Attack")) return Attack.getMoveFromJSON(OBJ, board);
        if (type.equals("Castle")) return Castle.getMoveFromJSON(OBJ, board);
        if (type.equals("EnPassant")) return EnPassant.getMoveFromJSON(OBJ, board);
        return null;
    }

}
