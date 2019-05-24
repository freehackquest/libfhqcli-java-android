package com.freehackquest.libfhqcli;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.freehackquest.libfhqcli.requests.FHQRequestBase;
import com.freehackquest.libfhqcli.requests.FHQRequestLogin;
import com.freehackquest.libfhqcli.responses.FHQChatMessage;
import com.freehackquest.libfhqcli.responses.FHQNotification;
import com.freehackquest.libfhqcli.responses.FHQResponse;
import com.freehackquest.libfhqcli.responses.FHQServerInfo;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FHQClient extends Service {
    private static final String TAG = FHQClient.class.getSimpleName();
    private Integer mCounter = 1;
    private ArrayList<FHQWaiter> mWaiters = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int ret = super.onStartCommand(intent, flags, startId);
        Log.i(TAG, "onStartCommand " + ret);
        listeners().onServiceStarted();
        return ret;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listeners().onServiceStopped();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return null;
    }

    private static FHQListeners mListeners = null;

    public static FHQListeners listeners() {
        if (mListeners == null) {
            mListeners = new FHQListeners();
        }
        return mListeners;
    }

    public static void startService(Context ctx) {
        Log.i(TAG, "startService");
        ctx.startService(new Intent(ctx, FHQClient.class));
    }

    public static FHQClient api() {
        return mInstance;
    }

    // singletone
    private static FHQClient mInstance = null;

    private Context mContext = null;

    public FHQClient() {
        Log.i(TAG, "constructor");
        if (mInstance == null) {
            mInstance = this;
        } else {
            Log.e(TAG, "already created");
        }
    }

    public void setContext(Context ctx) {
        mContext = ctx;
    }

    private WebSocket mWebSocket = null;

    public boolean isConnected() {
        if (mWebSocket == null) {
            return false;
        }
        return mWebSocket.isOpen();
    }

    public void connect(String uri) {
        Log.i(TAG, "connect to " + uri);

        if (mWebSocket != null) {
            Log.e(TAG, "mWebSocket already created");
            return;
        }
        WebSocketFactory factory = new WebSocketFactory().setConnectionTimeout(5000);
        try {
            mWebSocket = factory.createSocket(uri, 5000);
            // mWebSocket.addProtocol("wss");
            mWebSocket.addListener(new FHQWebSocketAdapter(this));
            mWebSocket.setPingInterval(60 * 1000); // 60 seconds
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            return;
        }
        mWebSocket.connectAsynchronously();
    }

    public void onTextMessage(WebSocket websocket, String message) {
        Log.i(TAG, "Message: " + message);

        String m = "";
        String cmd = "";
        JSONObject jsonMessage = null;
        try {
            jsonMessage = new JSONObject(message);
            if (jsonMessage.has("m")) {
                m = jsonMessage.getString("m");
            }
            if (jsonMessage.has("cmd")) {
                cmd = jsonMessage.getString("cmd");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "" + e.getMessage());
        }

        if (cmd.equals("notify")) {
            listeners().onNotify(new FHQNotification(jsonMessage));
        } else if (cmd.equals("chat")) {
            listeners().onChat(new FHQChatMessage(jsonMessage));
        } else if (cmd.equals("server")) {
            listeners().onServerInfo(new FHQServerInfo(jsonMessage));
        } else if (!m.isEmpty()) {
            for (FHQWaiter w : mWaiters) {
                if (w.getM().equals(m)) {
                    mWaiters.remove(w);
                    w.handle(jsonMessage);
                    break;
                }
            }
        }
    }

    public void disconnect() {
        mWebSocket.disconnect();
        mWebSocket = null;
    }

    public void onConnected() {
        Log.i(TAG, "onConnected");
        listeners().onConnected();
    }

    public void onDisconnected() {
        Log.i(TAG, "onDisconnected");
        listeners().onDisconnected();
    }

    private void requestData(FHQRequestBase req, FHQResponse resp) {
        if (!isConnected()) {
            resp.onError(null);
            return;
        }
        String m = "m" + mCounter;
        req.setM(m);
        mWaiters.add(new FHQWaiter(req, resp));
        String request = req.toJson().toString();
        Log.i(TAG, "Send Message " + request);
        mWebSocket.sendText(request);
    }

    public void login(FHQRequestLogin req, FHQResponse resp) {
        requestData(req, resp);
    }
}
