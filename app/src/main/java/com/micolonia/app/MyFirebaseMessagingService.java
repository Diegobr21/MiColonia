package com.micolonia.app;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

    }
        public void showNotification(String title, String message){
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, InicioSesion.class), 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Canal_1_ID")
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_stat_hadas)
                    .setContentIntent(pi)
                    .setAutoCancel(true);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            managerCompat.notify(999, builder.build());
        }




    private void scheduleJob() {
    }

    private void handleNow() {
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
    }
}
