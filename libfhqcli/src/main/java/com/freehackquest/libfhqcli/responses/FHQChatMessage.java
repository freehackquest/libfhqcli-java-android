package com.freehackquest.libfhqcli.responses;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class FHQChatMessage {
    private static final String TAG = FHQChatMessage.class.getSimpleName();
    private String dt = "";
    private String user = "";
    private String type = "";
    private String message = "";

    public FHQChatMessage(JSONObject jsonMessage) {
        // {"cmd":"chat","dt":"2019-05-23T10:13:29","message":"привет","type":"chat","user":"admin1"}
        try {
            if (jsonMessage.has("message")) {
                message = jsonMessage.getString("message");
            }

            if (jsonMessage.has("user")) {
                user = jsonMessage.getString("user");
            }

            if (jsonMessage.has("dt")) {
                dt = jsonMessage.getString("dt");
            }

            if (jsonMessage.has("type")) {
                type = jsonMessage.getString("type");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public String getMessage() {
        return message;
    }

    public String getUser()  {
        return user;
    }

    public String getType()  {
        return type;
    }

    public String getDateTime()  {
        return dt;
    }
}
