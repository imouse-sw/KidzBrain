package com.kidzbrain.notificaciones;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.kidzbrain.login.MainActivity;
import com.kidzbrain.login.R;

public class RecordatorioReceiver extends BroadcastReceiver {

    private static final String PREFS_NAME = "KidzBrainPrefs";
    private static final String KEY_NOTIFICACIONES = "notificaciones_activas";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean notificacionesActivas = prefs.getBoolean(KEY_NOTIFICACIONES, true);

        if (!notificacionesActivas) return;

        NotificationHelper.crearCanal(context);

        Intent abrirApp = new Intent(context, MainActivity.class);
        abrirApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                200,
                abrirApp,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String[] mensajes = {
                "Toca volver a estudiar un ratito en KidzBrain",
                "Tu siguiente lección te está esperando",
                "No abandones tu racha de aprendizaje",
                "Entra a KidzBrain y sigue avanzando",
                "Unos minutos de estudio hoy sí cuentan"
        };

        int indice = (int) (System.currentTimeMillis() % mensajes.length);
        String mensaje = mensajes[indice];

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("KidzBrain")
                .setContentText(mensaje)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(mensaje))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        NotificationManagerCompat.from(context).notify((int) System.currentTimeMillis(), builder.build());
    }
}
