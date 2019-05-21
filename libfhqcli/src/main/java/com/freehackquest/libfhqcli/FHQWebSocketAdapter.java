package com.freehackquest.libfhqcli;

import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.util.List;
import java.util.Map;

public class FHQWebSocketAdapter extends WebSocketAdapter {
    private static final String TAG = FHQWebSocketAdapter.class.getSimpleName();
    private FHQClient mClient = null;

    public FHQWebSocketAdapter(FHQClient cli) {
        mClient = cli;
    }

    // WebSocketAdapter
    @Override
    public void onTextMessage(WebSocket websocket, String message) {
        mClient.onTextMessage(websocket, message);
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
        Log.i(TAG, "onConnected");
        mClient.onConnected();
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
        Log.i(TAG, "onConnectError " + exception.getError().toString());
    }


    @Override
    public void onDisconnected(WebSocket websocket,
                               WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
                               boolean closedByServer) throws Exception {
        mClient.onDisconnected();
        Log.i(TAG, "onDisconnected");
    }

    @Override
    public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
        Log.i(TAG, "onError");
    }

    @Override
    public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) throws Exception {
        Log.i(TAG, "onTextMessageError");
    }


    @Override
    public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {
        Log.i(TAG, "onSendError");
    }


    @Override
    public void onUnexpectedError(WebSocket websocket, WebSocketException cause) throws Exception {
        Log.i(TAG, "onUnexpectedError");
    }
}
