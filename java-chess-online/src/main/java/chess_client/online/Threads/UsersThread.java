package chess_client.online.Threads;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import chess_client.online.HTTP;
import chess_client.online.User;

public class UsersThread extends Thread {
    private String host, usnm;
    private String data = "";

    public UsersThread(String host, String usnm) {
        this.host = host;
        this.usnm = usnm;
    }

    @Override
    public void run() {
        HashMap<String, String> params = HTTP.MakeParamsWithUsnm(usnm);

        try {
            this.data = HTTP.GET(this.host + "/GetServerOpponents", params);

            while (true) {
                this.data = HTTP.GET(this.host + "/UpdateOnServerOpponents", params);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String rawJSON() {
        return this.data;
    }

    /**
     * Method to get a List of Users from the JSON data the server returned
     * 
     * @return List of users
     */
    public List<User> getUsers() {
        if (this.data == "")
            return new ArrayList<User>();
        if (this.data == "auth/user-not-found") {
            System.out.println("Ping more than 3 seconds, disconnecting...");
            System.exit(0);
        }
        try {
            // Gets current array of online users from server
            JSONArray arr = new JSONArray(this.data);

            // Create array of users
            List<User> users = new ArrayList<User>();

            // Adds each user from a json string to User object
            for (int i = 0; i < arr.length(); i++) {
                String json = arr.getJSONObject(i).toString();
                users.add(User.FromJSON(json));
            }

            return users;
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<User>();
        }
    }

}