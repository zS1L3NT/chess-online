package board;

import java.util.ArrayList;
import java.util.List;

import board.Tile.*;
import pieces.*;

public class Board {
    private Tile[] board = new Tile[64];
    public GameMaster game;

    public Board() {
        game = new GameMaster();
        for (int i = 0; i < 64; i++) empty_tile(i);

        set_tile(new Rook(0, Team.BLACK), 0);
        set_tile(new Knight(1, Team.BLACK), 1);
        set_tile(new Bishop(2, Team.BLACK), 2);
        set_tile(new Queen(3, Team.BLACK), 3);
        set_tile(new King(4, Team.BLACK), 4);
        set_tile(new Bishop(5, Team.BLACK), 5);
        set_tile(new Knight(6, Team.BLACK), 6);
        set_tile(new Rook(7, Team.BLACK), 7);
        for (int i = 8; i < 16; i++) {
            set_tile(new Pawn(i, Team.BLACK), i);
        }
        for (int i = 48; i < 56; i++) {
            set_tile(new Pawn(i, Team.WHITE), i);
        }
        set_tile(new Rook(56, Team.WHITE), 56);
        set_tile(new Knight(57, Team.WHITE), 57);
        set_tile(new Bishop(58, Team.WHITE), 58);
        set_tile(new Queen(59, Team.WHITE), 59);
        set_tile(new King(60, Team.WHITE), 60);
        set_tile(new Bishop(61, Team.WHITE), 61);
        set_tile(new Knight(62, Team.WHITE), 62);
        set_tile(new Rook(63, Team.WHITE), 63);
    }

    public List<Move> all_legal_moves(Team team) {
        List<Move> moves = new ArrayList<Move>();
        for (int i = 0; i < 64; i++) {
            Tile tile = getTile(i);
            if (tile.is_empty()) continue;
            if (tile.piece.team != team) continue;
            List<Move> legal_moves = tile.piece.legal_moves(this);
            if (legal_moves.size() == 0) continue;
            for (int j = 0; j < legal_moves.size(); j++) {
                moves.add(legal_moves.get(j));
            }
        }
        return moves;
    }

    public void print() {
        System.out.println("  +----+----+----+----+----+----+----+----+");

        for (int i = 0; i < 8; i++) {
            int col = i * 8;

            // Reverse numbers for each team
            System.out.print((game.move_maker.is_white() ? 8 - i : i + 1) + " ");

            for (int j = 0; j < 8; j++) {
                int position = col + j;

                // Reverse tile print for each team
                Tile tile = this.board[game.move_maker.is_white() ? position : 63 - position];

                System.out.print("| ");

                // During player turn highlight moves
                if (game.current_selected != null) {
                    if (game.current_selected.legal_positions(this).contains(tile.position)) {
                        if (tile.is_occupied())
                            System.out.print(Color.RED_BACKGROUND);
                        else
                            System.out.print(Color.GREEN_BACKGROUND);
                    }
                }

                // Highlight available pieces to move
                if (tile.piece != null && game.current_selected == null && game.move_maker == tile.piece.team) {
                    if (tile.piece.legal_moves(this).size() > 0) {
                        System.out.print(Color.GREEN_BACKGROUND);
                    }
                }

                if (tile.is_occupied())
                    System.out.print(tile.boardKey() + " ");
                else
                    System.out.print("  ");
                System.out.print(Color.RESET);
                System.out.print(" ");

            }
            System.out.println("|");
            System.out.println("  +----+----+----+----+----+----+----+----+");
        }
        if (game.move_maker.is_white())
            System.out.println("    A    B    C    D    E    F    G    H");
        else
            System.out.println("    H    G    F    E    D    C    B    A ");
    }

    public void set_tile(Piece piece, int position) {
        this.board[position] = new OccuTile(position, piece);
    }

    public void empty_tile(int position) {
        this.board[position] = new EmptyTile(position, null);
    }

    public Tile getTile(int position) {
        return this.board[position];
    }
}
