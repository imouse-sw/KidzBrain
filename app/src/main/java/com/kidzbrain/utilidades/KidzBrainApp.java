package com.kidzbrain.utilidades;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.kidzbrain.notificaciones.NotificationHelper;

import java.util.Calendar;

public class KidzBrainApp extends Application implements Application.ActivityLifecycleCallbacks {

    private static final String PREFS_NAME = "KidzBrainStats";
    private static final String KEY_MINUTOS_TOTALES = "minutos_totales";
    private static final String KEY_ULTIMO_DIA = "ultimo_dia";
    private static final String KEY_RACHA = "racha";

    private static long tiempoInicioApp = 0L;

    private int activitiesAbiertas = 0;
    private boolean cambioDeConfiguracion = false;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        NotificationHelper.crearCanal(this);
    }

    public static long getTiempoInicioApp() {
        return tiempoInicioApp;
    }

    public static boolean estaAppActiva() {
        return tiempoInicioApp > 0;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (activitiesAbiertas == 0 && !cambioDeConfiguracion) {
            tiempoInicioApp = SystemClock.elapsedRealtime();
            actualizarRachaGlobal();
            Log.d("APP_TIEMPO", "La app entró al frente");
        }
        activitiesAbiertas++;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        cambioDeConfiguracion = activity.isChangingConfigurations();
        activitiesAbiertas--;

        if (activitiesAbiertas == 0 && !cambioDeConfiguracion) {
            guardarTiempoGlobal();
            tiempoInicioApp = 0L;
            Log.d("APP_TIEMPO", "La app se fue al fondo");
        }
    }

    private void guardarTiempoGlobal() {
        if (tiempoInicioApp == 0L) return;

        long tiempoActual = SystemClock.elapsedRealtime();
        long milisegundosTranscurridos = tiempoActual - tiempoInicioApp;

        int minutosNuevos = (int) (milisegundosTranscurridos / 60000);

        if (minutosNuevos > 0) {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            int minutosTotales = prefs.getInt(KEY_MINUTOS_TOTALES, 0);
            minutosTotales += minutosNuevos;

            prefs.edit().putInt(KEY_MINUTOS_TOTALES, minutosTotales).apply();

            Log.d("APP_TIEMPO", "Se guardaron " + minutosNuevos + " minuto(s). Total: " + minutosTotales);
        }
    }

    private void actualizarRachaGlobal() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        long hoy = obtenerInicioDelDia(System.currentTimeMillis());
        long ultimoDia = prefs.getLong(KEY_ULTIMO_DIA, 0);
        int racha = prefs.getInt(KEY_RACHA, 0);

        long UN_DIA = 24L * 60 * 60 * 1000;

        if (ultimoDia == 0) {
            racha = 1;
        } else if (hoy == ultimoDia) {
            // Ya entró hoy, no cambia
        } else if (hoy - ultimoDia == UN_DIA) {
            racha++;
        } else {
            racha = 1;
        }

        prefs.edit()
                .putLong(KEY_ULTIMO_DIA, hoy)
                .putInt(KEY_RACHA, racha)
                .apply();

        Log.d("APP_RACHA", "Racha actual: " + racha + " día(s)");
    }

    private long obtenerInicioDelDia(long tiempoActual) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(tiempoActual);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}