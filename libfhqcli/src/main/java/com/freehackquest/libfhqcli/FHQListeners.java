package com.freehackquest.libfhqcli;

import com.freehackquest.libfhqcli.responses.FHQChatMessage;
import com.freehackquest.libfhqcli.responses.FHQNotification;
import com.freehackquest.libfhqcli.responses.FHQServerInfo;

import java.util.ArrayList;

public class FHQListeners implements FHQListener {
    // listener
    private static ArrayList<FHQListener> listeners = new ArrayList<>();
    private static boolean bServiceStarted = false;
    private static boolean bConnected = false;
    private static FHQServerInfo serverInfo = null;
    public static void add(FHQListener l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
            if (bServiceStarted) {
                l.onServiceStarted();
            }
            if (bConnected) {
                l.onConnected();
            }
            if (serverInfo != null) {
                l.onServerInfo(serverInfo);
            }
        }
    }

    public static void remove(FHQListener l) {
        listeners.remove(l);
    }

    @Override
    public void onServiceStarted() {
        bServiceStarted = true;
        for (FHQListener l: listeners) {
            l.onServiceStarted();
        }
    }

    @Override
    public void onServiceStopped() {
        bServiceStarted = false;
        for (FHQListener l: listeners) {
            l.onServiceStopped();
        }
    }

    @Override
    public void onConnected() {
        bConnected = true;
        for (FHQListener l: listeners) {
            l.onConnected();
        }
    }

    @Override
    public void onDisconnected() {
        bConnected = false;
        serverInfo = null;
        for (FHQListener l: listeners) {
            l.onDisconnected();
        }
    }

    @Override
    public void onChat(FHQChatMessage msg) {
        for (FHQListener l: listeners) {
            l.onChat(msg);
        }
    }

    @Override
    public void onNotify(FHQNotification notify) {
        for (FHQListener l: listeners) {
            l.onNotify(notify);
        }
    }

    @Override
    public void onServerInfo(FHQServerInfo server) {
        serverInfo = server;
        for (FHQListener l: listeners) {
            l.onServerInfo(server);
        }
    }
}
