package com.kidzbrain.login.menuLateral;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.widget.ImageView; // Importante
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.kidzbrain.login.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class AjustesActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "KidzBrainPrefs";
    private static final String KEY_NOTIFICACIONES = "notificaciones_activas";
    private static final String KEY_VIBRACION = "vibracion_activa";
    private static final String KEY_VOLUMEN = "volumen_nivel";

    private SwitchMaterial switchNotificaciones, switchVibracion;
    private SeekBar seekbarVolumen;
    private ImageView btnMenu; // 1. Declaramos la variable del botón
    private SharedPreferences prefs;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ajustes);

        // 2. Enlazamos las vistas con el XML
        switchNotificaciones = findViewById(R.id.switch_notificaciones);
        switchVibracion = findViewById(R.id.switch_vibracion);
        seekbarVolumen = findViewById(R.id.seekbar_volumen);
        btnMenu = findViewById(R.id.btnMenu); // <--- Aquí enlazamos el botón

        // 3. Lógica del botón Menú (Para regresar)
        btnMenu.setOnClickListener(v -> {
            finish(); // Cierra la actividad
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // --- El resto de tu código sigue igual ---
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        boolean notifActivas = prefs.getBoolean(KEY_NOTIFICACIONES, true);
        boolean vibActiva = prefs.getBoolean(KEY_VIBRACION, true);

        // Control de errores por si audioManager es null (buena práctica)
        if (audioManager != null) {
            int volumenActual = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            seekbarVolumen.setMax(maxVol);
            seekbarVolumen.setProgress(volumenActual);
        }

        switchNotificaciones.setChecked(notifActivas);
        switchVibracion.setChecked(vibActiva);

        switchNotificaciones.setOnCheckedChangeListener((btn, isChecked) -> {
            prefs.edit().putBoolean(KEY_NOTIFICACIONES, isChecked).apply();

            if (!isChecked) {
                Toast.makeText(this,
                        "Para desactivar notificaciones por completo, ve a la configuración de Android",
                        Toast.LENGTH_LONG).show();

                // Abrir configuración de la app en Android
                try {
                    Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                    startActivity(intent);
                } catch (Exception e) {
                    // Si falla (algunos Android viejos), no hacemos nada o mostramos otro mensaje
                }
            }
        });

        switchVibracion.setOnCheckedChangeListener((btn, isChecked) -> {
            prefs.edit().putBoolean(KEY_VIBRACION, isChecked).apply();
            if (isChecked) vibrarTelefono(120);
        });

        seekbarVolumen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar sb, int progress, boolean fromUser) {
                if (audioManager != null) {
                    audioManager.setStreamVolume(
                            AudioManager.STREAM_MUSIC,
                            progress,
                            0 // Quité la bandera de UI para que no salga la barra del sistema encima de la tuya
                    );
                }
                prefs.edit().putInt(KEY_VOLUMEN, progress).apply();
            }

            public void onStartTrackingTouch(SeekBar sb) {}
            public void onStopTrackingTouch(SeekBar sb) {}
        });
    }

    private void vibrarTelefono(int ms) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (v == null || !v.hasVibrator()) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(ms);
        }
    }
}