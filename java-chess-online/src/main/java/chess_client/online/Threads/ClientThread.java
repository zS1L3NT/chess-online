package chess_client.online.Threads;

import chess_client.online.Client;

public class ClientThread extends Thread {
    private String host, usnm;

    public ClientThread(String host, String usnm) {
        this.host = host;
        this.usnm = usnm;
    }

    @Override
    public void run() {
        new Client(host, usnm);
    }

}