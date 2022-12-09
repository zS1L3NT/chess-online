package chess_client.online.Threads;

import java.io.IOException;
import java.util.HashMap;

import chess_client.online.HTTP;

public class ConnectionThread extends Thread {
    private String host, usnm;

    public ConnectionThread(String host, String usnm) {
        this.host = host;
        this.usnm = usnm;
    }

    @Override
    public void run() {
        HashMap<String, String> params = HTTP.MakeParamsWithUsnm(usnm);

        while (true) {
            try {
                HTTP.GET(this.host + "/Connected", params);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}