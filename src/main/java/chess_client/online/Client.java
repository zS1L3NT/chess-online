package chess_client.online;

import java.io.IOException;
import java.util.HashMap;

import chess_client.online.Threads.*;
import chess_client.online.Menus.*;

public class Client {
    private String host;
    private ConnectionThread ConnectionThread;
    private UsersThread UsersThread;
    private GameThread GameThread;
    private String usnm;

    public static void main(String[] args) throws IOException, InterruptedException {
        new Client("http://localhost:5000", "Jieun");
    }

    public Client(String host, String usnm)  {
        this.host = host;
        this.usnm = usnm;
        ConnectionThread = new ConnectionThread(this.host, usnm);
        UsersThread = new UsersThread(this.host, usnm);
        GameThread = new GameThread(this.host, usnm);

        HashMap<String, String> params = HTTP.MakeParamsWithUsnm(usnm);

        try {
            if (HTTP.GET(host + "/InitNewUser", params).equals("init/user-exists")) {
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Start connections with server
        ConnectionThread.start();
        UsersThread.start();
        GameThread.start();

        OpponentSelectMenu();

        GameThread.startGame();
    }

    /**
     * Make a new menu if current users changes
     */
    private final void OpponentSelectMenu() {
        OpponentSelectMenu usm = new OpponentSelectMenu(this.host, this.usnm, UsersThread.getUsers());

        while (true) {
            wait(1000);
            if (usm.done)
                return;
            usm.updateUsers(UsersThread.getUsers());
        }
    }

    private final static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}