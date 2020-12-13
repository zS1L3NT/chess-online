package chess_client.board;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Piece implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -886455757238864816L;
    private int position;
    private Team team;
    private int move_count;

    public Piece(int position, Team team) {
        this.position = position;
        this.team = team;
    }

    public abstract List<Move> legal_moves(Board board);

    public List<Move> safe_moves(Board board) {
        List<Move> legal_moves = legal_moves(board);
        List<Move> moves = new ArrayList<Move>();

        for (int i = 0; i < legal_moves.size(); i++) {
            Move move = legal_moves.get(i);
            Move copy;
            try {
                copy = move.deep_clone();
                copy.set_name("test");
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
                return null;
            }

            Board simulation = copy.simulate(board);
            if (this.team.king_is_safe(simulation))
                moves.add(move);
        }
        return moves;
    }

    public List<Integer> safe_positions(Board board) {
        List<Integer> tiles = new ArrayList<Integer>();
        List<Move> safe_moves = safe_moves(board);
        for (int i = 0; i < safe_moves.size(); i++) {
            Move move = safe_moves.get(i);
            tiles.add(move.destination());
        }
        return tiles;
    }

    public void set_position(int position) {
        this.position = position;
    }

    public int move_count() {
        return this.move_count;
    }

    public void add_move_count() {
        this.move_count++;
    }

    @Override
    public abstract String toString();

    public abstract String boardKey();

    public abstract String typeKey();

    public int position() {
        return this.position;
    }

    public Team team() {
        return this.team;
    }
}
