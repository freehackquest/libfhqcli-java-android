package com.freehackquest.libfhqcli;

import android.util.Log;

import com.freehackquest.libfhqcli.requests.FHQRequestBase;
import com.freehackquest.libfhqcli.responses.FHQResponse;
import com.freehackquest.libfhqcli.responses.FHQResponseError;

import org.json.JSONException;
import org.json.JSONObject;

public class FHQWaiter {
    private static final String TAG = FHQClient.class.getSimpleName();

    private FHQRequestBase request;
    private FHQResponse response;
    private String m = "";

    FHQWaiter(FHQRequestBase req, FHQResponse resp) {
        // TODO datetime when response
        m = req.getM();
        request = req;
        response = resp;
    }

    String getM() {
        return m;
    }

    void handle(JSONObject o) {
        String result = "FAIL";
        try {
            result = o.getString("result");
        } catch(JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "JSON " + e.getMessage());
        }

        if (result.equals("FAIL")) {
            response.onError(new FHQResponseError(o));
        } else {
            response.onDone(o);
        }
    }
}
