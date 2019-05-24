package com.freehackquest.libfhqcli.requests;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class FHQRequestBase {
    private static final String TAG = FHQRequestBase.class.getSimpleName();

    private String m = "m1";
    private String mCmd;

    public FHQRequestBase(String cmd) {
        this.mCmd = cmd;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public JSONObject toJson() {
        JSONObject ret = new JSONObject();
        try {
            ret.put("cmd", mCmd);
            ret.put("m", m);
        } catch(JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "JSONException " + e.getMessage());
        }
        return ret;
    }

}
