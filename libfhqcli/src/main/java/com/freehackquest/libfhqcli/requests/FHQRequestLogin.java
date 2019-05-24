package com.freehackquest.libfhqcli.requests;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class FHQRequestLogin extends FHQRequestBase {
    private static final String TAG = FHQRequestLogin.class.getSimpleName();

    public String email;
    public String password;

    public FHQRequestLogin() {
        super("login");
    }

    public JSONObject toJson() {

        JSONObject ret = super.toJson();
        try {
            ret.put("email", email);
            ret.put("password", password);
        } catch(JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "JSONException " + e.getMessage());
        }
        return ret;
    }
}
