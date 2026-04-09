package com.kidzbrain.notificaciones;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class RecordatorioScheduler {

    public static final int UNA_VEZ_AL_DIA = 1;
    public static final int DOS_VECES_AL_DIA = 2;

    public static void programarRecordatorios(Context context, int frecuenciaDiaria) {
        cancelarRecordatorios(context);

        programarAlarmaDiaria(context, 1001, 16, 0); // 4:00 pm

        if (frecuenciaDiaria == DOS_VECES_AL_DIA) {
            programarAlarmaDiaria(context, 1002, 20, 0); // 8:00 pm
        }
    }

    public static void cancelarRecordatorios(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        PendingIntent pi1 = crearPendingIntent(context, 1001);
        PendingIntent pi2 = crearPendingIntent(context, 1002);

        alarmManager.cancel(pi1);
        alarmManager.cancel(pi2);
    }

    private static void programarAlarmaDiaria(Context context, int requestCode, int hora, int minuto) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.set(Calendar.MINUTE, minuto);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        PendingIntent pendingIntent = crearPendingIntent(context, requestCode);

        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );
    }

    private static PendingIntent crearPendingIntent(Context context, int requestCode) {
        Intent intent = new Intent(context, RecordatorioReceiver.class);

        return PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }
}
