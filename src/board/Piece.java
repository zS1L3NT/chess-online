package board;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    public int position;
    public Team team;

    public Piece(int position, Team team) {
        this.position = position;
        this.team = team;
    }

    public abstract List<Move> legal_moves(Board board);

    public void set_position(int position) {
        this.position = position;
    }

    public List<Integer> legal_positions(Board board) {
        List<Integer> tiles = new ArrayList<Integer>();
        List<Move> legal_moves = legal_moves(board);
        for (int i = 0; i < legal_moves.size(); i++) {
            Move move = legal_moves.get(i);
            tiles.add(move.destination);
        }
        return tiles;
    }

    @Override
    public abstract String toString();
    
    
	public abstract String boardKey();
}
