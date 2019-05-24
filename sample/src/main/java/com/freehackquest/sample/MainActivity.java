package com.freehackquest.sample;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.freehackquest.libfhqcli.responses.FHQChatMessage;
import com.freehackquest.libfhqcli.FHQClient;
import com.freehackquest.libfhqcli.FHQListener;
import com.freehackquest.libfhqcli.responses.FHQNotification;
import com.freehackquest.libfhqcli.responses.FHQResponseError;
import com.freehackquest.libfhqcli.responses.FHQServerInfo;
import com.freehackquest.libfhqcli.requests.FHQRequestLogin;
import com.freehackquest.libfhqcli.responses.FHQResponse;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements FHQListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SharedPreferences mSettings = null;

    private static final String PREF_SERVER = "pref_server";
    private static String PREF_LOGIN = "pref_login";
    private static String PREF_PASSWORD = "pref_password";

    private EditText edt_server = null;
    private Button btn_connect = null;
    private EditText edt_login = null;
    private EditText edt_password = null;
    private Button btn_login = null;
    private LinearLayout login_form = null;
    private RecyclerView list_log = null;
    private LogAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSettings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        edt_server = findViewById(R.id.edt_server);
        btn_connect = findViewById(R.id.btn_connect);

        login_form = findViewById(R.id.login_form);
        edt_login = findViewById(R.id.edt_login);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);


        edt_server.setText(getPref(PREF_SERVER, "wss://freehackquest.com/api-wss/"));
        edt_login.setText(getPref(PREF_LOGIN, "admin"));
        edt_password.setText(getPref(PREF_PASSWORD, "admin"));

        list_log = findViewById(R.id.list_log);
        list_log.setLayoutManager(layoutManager);
        list_log.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        list_log.setLayoutManager(layoutManager);
        mAdapter = new LogAdapter();
        list_log.setAdapter(mAdapter);

        FHQClient.listeners().add(this);
        initButtons();
    }

    @Override
    protected void onPause() {
        super.onPause();
        FHQClient.listeners().remove(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FHQClient.listeners().add(this);
    }

    @Override
    protected void onDestroy() {
        FHQClient.listeners().remove(this);
        super.onDestroy();
    }

    public String getPref(String name, String defValue) {
        if (mSettings == null) return defValue;
        return mSettings.getString(name, defValue);
    }

    public void setPref(String name, String val) {
        if (mSettings == null) return;
        mSettings.edit().putString(name, val).apply();
    }

    private void initButtons() {

        /*if (!FHQClient.api().isConnected()) {
            login_form.setEnabled(false);
        }*/

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String server = edt_server.getText().toString();
                btn_connect.setEnabled(false);
                if (FHQClient.api().isConnected()) {
                    FHQClient.api().disconnect();
                } else {
                    FHQClient.api().connect(server);
                }
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FHQRequestLogin req = new FHQRequestLogin();
                req.email = edt_login.getText().toString();
                req.password = edt_password.getText().toString();
                FHQClient.api().login(req, new FHQResponse() {
                    @Override
                    public void onDone(JSONObject o) {
                        addLog("Login success: " + o.toString());
                    }

                    @Override
                    public void onError(FHQResponseError err) {
                        addLog("Login failed: " + err.getError());
                    }
                });
            }
        });
    }

    private void updateStatuses() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!FHQClient.api().isConnected()) {
                    edt_server.setEnabled(true);
                    btn_connect.setEnabled(true);
                    btn_connect.setText("Connect");
                    login_form.setEnabled(false);
                } else {
                    edt_server.setEnabled(false);
                    btn_connect.setEnabled(true);
                    btn_connect.setText("Disconnect");
                    login_form.setEnabled(true);
                }
            }
        });
    }

    private void addLog(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.addItem(msg);
            }
        });
    }

    private void showNotification(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, SampleApp.CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("SampleApp")
                        .setContentText(text)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(text))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                Notification notification = builder.build();

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(1, notification);
            }
        });
    }

    @Override
    public void onServiceStarted() {
        Log.i(TAG, "onServiceStarted");
        updateStatuses();
        showNotification("ServiceStarted");
    }

    @Override
    public void onServiceStopped() {
        Log.i(TAG, "onServiceStopped");
        updateStatuses();
    }

    @Override
    public void onConnected() {
        Log.i(TAG, "onConnected");
        updateStatuses();
        showNotification("Connected");
    }

    @Override
    public void onDisconnected() {
        Log.i(TAG, "onDisconnected");
        updateStatuses();
    }

    @Override
    public void onChat(FHQChatMessage msg) {
        String s = "Chat: [" + msg.getDateTime() + "] " + msg.getUser() + ": " + msg.getMessage();
        addLog(s);
        showNotification(s);
    }

    @Override
    public void onNotify(FHQNotification notify) {
        String s = "Notification: [" + notify.getType() + ", " + notify.getSection() + "] " + notify.getMessage();
        addLog(s);
        showNotification(s);
    }

    @Override
    public void onServerInfo(FHQServerInfo server) {
        addLog(server.getApp() + ":" + server.getVersion());
    }
}
