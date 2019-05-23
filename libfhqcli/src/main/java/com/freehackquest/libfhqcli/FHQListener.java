package com.freehackquest.libfhqcli;

public interface FHQListener {
    void onServiceStarted();
    void onServiceStopped();
    void onConnected();
    void onDisconnected();

}
