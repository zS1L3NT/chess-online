import board.*;

import java.util.List;
import java.util.Scanner;

public class App {
    private static Board board;
    private static Move move;
    private static GameMaster game;

    public static void main(String[] args) {
        board = new Board("main");
        game = board.game;

        for (;;) {
            print_visuals();

            game.set_current_selected(select_piece(board));

            print_visuals();

            move = select_move(board);
            if (move == null) continue;

            for (int i = 5; i > 0; i--) {
                System.out.println("Flipping board in " + i + " seconds");
                wait(1000);
            }

            board.execute(move);
            game.change_move_maker();
            game.set_current_selected(null);
        }

    }

    public final static Piece select_piece(Board board) {
        Piece piece;

        for (;;) {
            System.out.print("Select Piece to move: ");
            String code = input().nextLine();

            if (!BoardUtils.is_valid_board_code(code)) {
                System.out.println("Invalid board code!");
                continue;
            }

            int position = BoardUtils.to_position(code);
            piece = board.tile(position).piece();

            if (piece == null) {
                System.out.println("No piece on selected tile!");
                continue;
            }

            if (piece.team() != game.move_maker) {
                System.out.println("Piece not on your team!");
                continue;
            }

            if (piece.legal_moves(board).size() == 0) {
                System.out.println("Piece has no possible moves!");
                continue;
            }

            if (piece.safe_moves(board).size() == 0) {
                System.out.println("King in Danger! Selected piece has no moves which keeps the King safe!");
                continue;
            }

            break;
        }

        return piece;
    }

    public static Move select_move(Board board) {
        List<Move> safe_moves = game.current_selected.safe_moves(board);
        List<Integer> safe_positions = game.current_selected.safe_positions(board);
        int destination;

        System.out.println("Type X to select another piece\n");

        for (;;) {
            System.out.print("Move to: ");
            String code = input().nextLine();

            if (code.equals("x") || code.equals("X")) {
                game.set_current_selected(null);
                return null;
            }

            if (!BoardUtils.is_valid_board_code(code)) {
                System.out.println("Invalid board code!");
                continue;
            }

            destination = BoardUtils.to_position(code);

            // Check if item in safe_positions
            if (safe_positions.contains(destination))
                break;
            else
                System.out.println("Invalid move!");
        }

        // Find and return move in legal_moves
        for (int i = 0; i < safe_moves.size(); i++) {
            if (safe_moves.get(i).destination() == destination) {
                return safe_moves.get(i);
            }
        }

        return null;
    }

    private final static Scanner input() {
        return new Scanner(System.in);
    }

    private final static void print_visuals() {
        System.out.print("\033[H\033[2J");
        board.print();
        System.out.println("\nTurn: " + game.move_maker);

        boolean king_safe = game.move_maker.is_safe(board, game.move_maker.king_position(board));
        System.out.print("King status: ");

        if (king_safe) System.out.print(Color.GREEN);
        else System.out.print(Color.RED);

        System.out.println((king_safe ? "Safe" : "Danger") + "\n");
        System.out.print(Color.RESET);
    }

    public final static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
