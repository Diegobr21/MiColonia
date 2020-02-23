package com.micolonia.app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class LasHadas extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        crearCanalNotificaciones();
    }

    private void crearCanalNotificaciones(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel canal_1 = new NotificationChannel(
                    "Canal_1_ID",
                    "Recordatorios",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            canal_1.enableVibration(true);
            canal_1.setDescription("Recordatorios de uso para los usuarios");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if(manager!= null){
                manager.createNotificationChannel(canal_1);
            }


        }
    }
}
