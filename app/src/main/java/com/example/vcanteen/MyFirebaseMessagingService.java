package com.example.vcanteen;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String CHANNEL_ID = "Orders";
    private static final String CHANNEL_NAME = "vCanteen Noti";
    private static final String CHANNEL_DESC = "vCanteen Notification";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.i("testFCM", "startOnMessageReceived");

        if (remoteMessage.getData() != null) {
            sendNotification(remoteMessage);
        }
    }


    private void sendNotification(RemoteMessage remoteMessage) {

        Intent intent = new Intent(this, OrderListActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                100,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );

        Map<String, String> data = remoteMessage.getData();


        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
//        String title = data.get("title");
//        String body = data.get("body");
        Log.i("testFCM", "if remotemessage.getdata() not null");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel);
            notificationManager.createNotificationChannel(channel);
        }

//        NotificationHelper.displayNotification(getApplicationContext(), title, body);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
//                .setContentTitle(data.get("title"))
//                .setContentText(data.get("body"))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);


        notificationManager.notify(1, notificationBuilder.build());


//        if (remoteMessage.getData().size() > 0) {
////            String title = remoteMessage.getNotification().getTitle();
////            String body = remoteMessage.getNotification().getBody();
//            Intent intent2 = new Intent("com.example.vcanteen_FCM-MESSAGE");
//
//            intent2.putExtra("title", title);
//            intent2.putExtra("body", body);
//            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
//            localBroadcastManager.sendBroadcast(intent2);
//
//        }
    }

    @Override
    public void onNewToken(String mToken) {
        super.onNewToken(mToken);
        Log.e("TOKEN", mToken);
    }
}