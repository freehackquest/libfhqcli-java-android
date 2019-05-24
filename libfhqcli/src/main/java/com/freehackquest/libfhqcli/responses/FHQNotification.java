package com.freehackquest.libfhqcli.responses;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class FHQNotification {
    private static final String TAG = FHQNotification.class.getSimpleName();
    private String section = "";
    private String type = "";
    private String message = "";

    public FHQNotification(JSONObject jsonMessage) {
        // {"cmd":"notify","m":"s0","message":"Income chat message привет","section":"chat","type":"info"}
        try {
            if (jsonMessage.has("message")) {
                message = jsonMessage.getString("message");
            }

            if (jsonMessage.has("section")) {
                section = jsonMessage.getString("section");
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

    public String getSection()  {
        return section;
    }

    public String getType()  {
        return type;
    }
}
