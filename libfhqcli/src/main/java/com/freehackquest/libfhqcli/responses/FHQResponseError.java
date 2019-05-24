package com.freehackquest.libfhqcli.responses;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class FHQResponseError {
    private static final String TAG = FHQResponseError.class.getSimpleName();
    private int code = 0;
    private String error = "";

    public FHQResponseError(JSONObject jsonMessage) {
        try {
            if (jsonMessage.has("error")) {
                error = jsonMessage.getString("error");
            }

            if (jsonMessage.has("code")) {
                code = jsonMessage.getInt("code");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public String getError() {
        return error;
    }

    public int getCode()  {
        return code;
    }
}
