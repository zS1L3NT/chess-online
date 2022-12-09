package chess_client;

import chess_client.board.*;

import java.util.List;
import java.util.Scanner;

import org.json.JSONException;

public class ChessStarter extends Thread {
    public Board board;
    private Move move;
    private String opponentMove = null;
    private GameMaster game;
    public boolean isYourTurn;

    public ChessStarter(Team player) {
        this.board = new Board(player);
        this.isYourTurn = player.is_white();
        this.game = board.game;
    }

    /**
     * Start the thread to wait and run in the background
     */
    @Override
    public void run() {
        if (!this.isYourTurn) {
            print_visuals(true);
            waitForYourTurn();
        }

        for (;;) {
            print_visuals(true);
            game.test_if_will_end();

            game.set_current_selected(select_piece(board));

            print_visuals(true);

            move = select_move(board);
            if (move == null)
                continue;

            board.execute(move);
            this.isYourTurn = false;

            print_visuals(false);
            waitForYourTurn();

            wait(1000);

            setMoveNull();
            game.set_current_selected(null);
        }
    }

    public void executeOpponentMove(String json) {
        try {
            Move opponentMove = Move.fromJSON(json, this.board);
            this.opponentMove = opponentMove.toString();

            this.board.execute(opponentMove);
            this.isYourTurn = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Waits for this.isYourTurn to be true
     */
    private void waitForYourTurn() {
        System.out.println("Waiting for your turn...");
        while (true) {
            wait(1000);
            if (this.isYourTurn)
                break;
        }
    }

    public Move move() {
        return this.move;
    }

    public void setMoveNull() {
        this.move = null;
    }

    public void setYourTurn() {
        this.isYourTurn = true;
    }

    public void setEnemyTurn() {
        this.isYourTurn = false;
    }

    /**
     * Method to select a new piece from the board
     * 
     * @param board
     * @return Piece selected
     */
    private final Piece select_piece(Board board) {
        Piece piece;

        System.out.println("Type RESIGN to end the game\n");

        for (;;) {
            System.out.print("Select Piece to move: ");
            String code = input().nextLine();

            if (code.equals("RESIGN") || code.equals("resign")) {
                System.out.print(Color.RED);
                System.out.println("\nYou resigned");
                System.out.print(Color.RESET);
                System.out.println("Winner: " + game.player.enemy() + "!");
                System.exit(1);
            }

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

            if (piece.team() != game.player) {
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

    /**
     * Method to select move from board
     * 
     * @param board
     * @return Move selected
     */
    private Move select_move(Board board) {
        List<Move> safe_moves = game.current_selected.safe_moves(board);
        List<Integer> safe_positions = game.current_selected.safe_positions(board);
        int destination;

        System.out.println("Add brackets around tile to find out about the move");
        System.out.println("Type X to select another piece\n");

        for (;;) {
            System.out.print("Move to: ");
            String code = input().nextLine();

            if (code.equals("x") || code.equals("X")) {
                game.set_current_selected(null);
                return null;
            }

            if (code.startsWith("(") && code.endsWith(")")) {
                String cut = code.substring(1, code.length() - 1);
                if (BoardUtils.is_valid_board_code(cut)) {
                    destination = BoardUtils.to_position(cut);

                    if (safe_positions.contains(destination)) {
                        // Give move information
                        for (int i = 0; i < safe_moves.size(); i++) {
                            if (safe_moves.get(i).destination() == destination) {
                                System.out.println(safe_moves.get(i));
                            }
                        }
                        continue;
                    } else {
                        System.out.println("Invalid move!");
                        continue;
                    }

                } else {
                    System.out.println("Invalid board code!");
                    continue;
                }
            }

            if (!BoardUtils.is_valid_board_code(code)) {
                System.out.println("Invalid board code!");
                continue;
            }

            destination = BoardUtils.to_position(code);

            // Check if item in safe_positions
            if (safe_positions.contains(destination))
                break;
            else {
                System.out.println("Invalid move!");
                continue;
            }
        }

        // Find and return move in legal_moves
        for (

                int i = 0; i < safe_moves.size(); i++) {
            if (safe_moves.get(i).destination() == destination) {
                return safe_moves.get(i);
            }
        }

        return null;
    }

    /**
     * Method to print all the visuals including Turn, King Status, Moves and
     * Opponent's last move
     * 
     * @param color
     */
    private final void print_visuals(boolean color) {
        System.out.print("\033[H\033[2J");
        board.print(color);
        System.out.println("\nTurn: " + game.player);

        boolean king_safe = game.player.king_is_safe(board);
        System.out.print("King status: ");
        if (king_safe)
            System.out.print(Color.GREEN);
        else
            System.out.print(Color.RED);
        System.out.println((king_safe ? "Safe" : "Danger"));
        System.out.print(Color.RESET);

        if (game.current_selected == null)
            System.out.println("Total available moves: " + game.player.all_safe_moves(board).size());
        else
            System.out.println("Piece available moves: " + game.current_selected.safe_moves(board).size());

        if (this.opponentMove != null)
            System.out.println("Opponent's last move: " + this.opponentMove);

        System.out.println();
    }

    /**
     * Scanner class to get input
     * 
     * @return
     */
    private final Scanner input() {
        return new Scanner(System.in);
    }

    /**
     * Wait in milliseconds
     * 
     * @param ms
     */
    private final void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}
