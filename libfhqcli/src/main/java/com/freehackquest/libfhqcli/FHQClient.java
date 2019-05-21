package com.freehackquest.libfhqcli;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.neovisionaries.ws.client.HostnameUnverifiedException;
import com.neovisionaries.ws.client.OpeningHandshakeException;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.json.JSONException;
import org.json.JSONObject;

public class FHQClient extends Service {
    private static final String TAG = FHQClient.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        // connect("wss://freehackquest.com/wss-api/");
        connect("wss://freehackquest.com/api-wss/");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return null;
    }

    // listener
    private FHQListener mListener = null;
    public void setListener(FHQListener listener) {
        mListener = listener;
    }

    public void unsetListener() {
        mListener = null;
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
    }

    public void disconnect() {
        mWebSocket.disconnect();
        mWebSocket = null;
    }

    public void onConnected() {
        Log.i(TAG, "onConnected");
        if (mListener != null) {
            mListener.onConnected();
        }
    }

    public void onDisconnected() {
        Log.i(TAG, "onDisconnected");
        if (mListener != null) {
            mListener.onDisconnected();
        }
    }

    public void login(String email, String password) {
        if (!isConnected()) {
            return;
        }
        String request = "{}";
        try {
            JSONObject json = new JSONObject();
            json.put("cmd", "login");
            json.put("m", "m1");
            json.put("email", email);
            json.put("password", password);
            request = json.toString();
        } catch(JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "JSONException " + e.getMessage());
            return;
        }
        Log.i(TAG, "Send Message " + request);
        mWebSocket.sendText(request);
    }
}
