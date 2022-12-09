package chess_client;

import java.io.IOException;
import java.util.HashMap;

import chess_client.online.HTTP;
import chess_client.online.Menus.UsnmSelectMenu;

public class App {
    public static String host = "http://chess-http.bubblejs.com";

    public static void main(String[] args) throws IOException {
        System.out.println("Starting up the server...");
        System.out.println("May take between 1 and 10 seconds...");
        HTTP.GET(host + "/Start", new HashMap<String, String>());

        System.out.print("\033[H\033[2J");
        System.out.println("Go to the menu that just popped up in your computer");

        new UsnmSelectMenu(host);
    }
}
