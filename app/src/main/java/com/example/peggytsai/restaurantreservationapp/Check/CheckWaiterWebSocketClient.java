package com.example.peggytsai.restaurantreservationapp.Check;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Locale;


public class CheckWaiterWebSocketClient extends WebSocketClient {
    private static final String TAG = "CheckWaiterWebSocketCLient";
    private LocalBroadcastManager broadcastManager;
    private Gson gson;

    public CheckWaiterWebSocketClient(URI serverUri, Context context) {
        // Draft_17是連接協議，就是標準的RFC 6455（JSR256）
        super(serverUri, new Draft_17());
        broadcastManager = LocalBroadcastManager.getInstance(context);
        gson = new Gson();

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        String text = String.format(Locale.getDefault(),
                "onOpen: Http status code = %d; status message = %s",
                handshakedata.getHttpStatus(),
                handshakedata.getHttpStatusMessage());
        Log.d(TAG, "onOpen: " + text);

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onMessage(String message) {
        JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
        // type: 訊息種類，有open(有user連線), close(有user離線), chat(其他user傳送來的聊天訊息)
        String type = jsonObject.get("type").getAsString();
        sendMessageBroadcast(type, message);
        Log.d(TAG, "onMessage: " + message);

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onClose(int code, String reason, boolean remote) {
        String text = String.format(Locale.getDefault(),
                "code = %d, reason = %s, remote = %b",
                code, reason, remote);
        Log.d(TAG, "onClose: " + text);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onError(Exception ex) {
        Log.d(TAG, "onError: exception = " + ex.toString());

    }
    private void sendMessageBroadcast(String messageType, String message) {
        Intent intent = new Intent(messageType); //設ＩＤ
        intent.putExtra("message", message);
        broadcastManager.sendBroadcast(intent);
    }
}
