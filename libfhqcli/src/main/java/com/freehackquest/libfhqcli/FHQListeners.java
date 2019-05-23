package com.freehackquest.libfhqcli;

import java.util.ArrayList;

public class FHQListeners implements FHQListener {
    // listener
    private static ArrayList<FHQListener> listeners = new ArrayList<>();

    public static void add(FHQListener l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
        }
    }

    public static void remove(FHQListener l) {
        listeners.remove(l);
    }

    @Override
    public void onServiceStarted() {
        for (FHQListener l: listeners) {
            l.onServiceStarted();
        }
    }

    @Override
    public void onServiceStopped() {
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
}
