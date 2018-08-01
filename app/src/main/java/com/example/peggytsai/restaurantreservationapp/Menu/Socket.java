package com.example.peggytsai.restaurantreservationapp.Menu;

import android.content.Context;
import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;


public class Socket {
    private final static String TAG = "Common";
    public static final String SERVER_URI =
            "ws://10.0.2.2:8080/RestaurantReservationApp_Web/updateStock/";
    public static ChatWebSocketClient SocketClient;

    // 建立WebSocket連線
    public static void connectServer(Context context, String member_id) {
        URI uri = null;
        try {
            uri = new URI(SERVER_URI + member_id+"");
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
        }
        if (SocketClient == null) {
            SocketClient = new ChatWebSocketClient(uri, context);
            SocketClient.connect();
        }else {
            SocketClient.close();
            SocketClient = null;

            SocketClient = new ChatWebSocketClient(uri, context);
            SocketClient.connect();

        }
    }

    // 中斷WebSocket連線
    public static void disconnectServer() {
        if (SocketClient != null) {
            SocketClient.close();
            SocketClient = null;
        }
    }

//    broadcastManager = LocalBroadcastManager.getInstance(this);
//    registerChatReceiver();

}
