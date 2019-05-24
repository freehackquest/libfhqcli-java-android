package com.freehackquest.libfhqcli.responses;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FHQDeveloper {
    private static final String TAG = FHQDeveloper.class.getSimpleName();
    private String email = "";
    private String name = "";
    private ArrayList<String> developers = new ArrayList<>();

    public FHQDeveloper(JSONObject jsonMessage) {
        try {
            if (jsonMessage.has("email")) {
                email = jsonMessage.getString("email");
            }

            if (jsonMessage.has("name")) {
                name = jsonMessage.getString("name");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public String getEmail() {
        return email;
    }

    public String getName()  {
        return name;
    }
}
