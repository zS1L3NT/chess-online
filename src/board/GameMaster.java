package board;

public class GameMaster {
    public Team move_maker;
    public Piece current_selected;
    public Team winner;

    public GameMaster() {
        move_maker = Team.WHITE;
    }

    public void set_current_selected(Piece piece) {
        this.current_selected = piece;
    }

    public void set_winner(Team team) {
        this.winner = team;
    }

    public void change_move_maker() {
        this.move_maker = this.move_maker.is_white() ? Team.BLACK : Team.WHITE;
    }
}
