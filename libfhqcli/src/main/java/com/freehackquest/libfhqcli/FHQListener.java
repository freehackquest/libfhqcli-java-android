package com.freehackquest.libfhqcli;

public interface FHQListener {
    void onServiceStarted();
    void onServiceStopped();
    void onConnected();
    void onDisconnected();
    void onChat(FHQChatMessage msg);
    void onNotify(FHQNotification notify);
}