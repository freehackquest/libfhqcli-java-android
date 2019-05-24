package com.freehackquest.libfhqcli;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FHQServerInfo {
    private static final String TAG = FHQServerInfo.class.getSimpleName();
    private String app = "";
    private String version = "";
    private ArrayList<FHQDeveloper> developers = new ArrayList<>();

    public FHQServerInfo(JSONObject jsonMessage) {
        // {"app":"fhq-server","cmd":"server","developers":[{"email":"mrseakg@gmail.com","name":"Evgenii Sopov"},{"email":"?","name":"Igor Polyakov"},{"email":"sergo.moreno@gmail.com","name":"Sergey Ushev"},{"email":"shikamaru740@gmail.com","name":"Danil Dudkin"}],"m":"s0","version":"0.2.21"}
        try {
            if (jsonMessage.has("app")) {
                app = jsonMessage.getString("app");
            }

            if (jsonMessage.has("version")) {
                version = jsonMessage.getString("version");
            }

            if (jsonMessage.has("developers")) {
                JSONArray jsonDevelopers = jsonMessage.getJSONArray("developers");
                for (int i = 0; i < jsonDevelopers.length(); i++) {
                    JSONObject d = jsonDevelopers.getJSONObject(i);
                    developers.add(new FHQDeveloper(d));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "" + e.getMessage());
        }
    }

    public String getApp() {
        return app;
    }

    public String getVersion()  {
        return version;
    }

    public ArrayList<FHQDeveloper> getDevelopers()  {
        return developers;
    }

}
