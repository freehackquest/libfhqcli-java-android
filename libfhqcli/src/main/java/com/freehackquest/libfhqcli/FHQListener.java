package com.freehackquest.libfhqcli;

import com.freehackquest.libfhqcli.responses.FHQChatMessage;
import com.freehackquest.libfhqcli.responses.FHQNotification;
import com.freehackquest.libfhqcli.responses.FHQServerInfo;

public interface FHQListener {
    void onServiceStarted();
    void onServiceStopped();
    void onConnected();
    void onDisconnected();
    void onChat(FHQChatMessage msg);
    void onNotify(FHQNotification notify);
    void onServerInfo(FHQServerInfo server);

}