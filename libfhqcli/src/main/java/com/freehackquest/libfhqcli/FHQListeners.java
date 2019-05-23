package com.freehackquest.libfhqcli;

import java.util.ArrayList;

public class FHQListeners implements FHQListener {
    // listener
    private static ArrayList<FHQListener> listeners = new ArrayList<>();
    private static boolean bServiceStarted = false;
    public static void add(FHQListener l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
            if (bServiceStarted) {
                l.onServiceStarted();
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
        for (FHQListener l: listeners) {
            l.onConnected();
        }
    }

    @Override
    public void onDisconnected() {
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
}
