package com.freehackquest.libfhqcli.responses;

import org.json.JSONObject;

public interface FHQResponse {
    void onDone(JSONObject o);
    void onError(FHQResponseError o);
}
