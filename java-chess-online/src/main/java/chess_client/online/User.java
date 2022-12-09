package chess_client.online;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private JSONObject OBJ;

    public User(String json) {
        this.OBJ = new JSONObject(json);
    }

    public String usnm() {
        return this.OBJ.getString("usnm");
    }

    public boolean inGame() {
        return this.OBJ.getBoolean("inGame");
    }

    public String request() {
        return this.OBJ.getString("request");
    }

    public static User FromJSON(String json) {
        try {
            return new User(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return usnm() + "<" + request() + ">";
    }
}
