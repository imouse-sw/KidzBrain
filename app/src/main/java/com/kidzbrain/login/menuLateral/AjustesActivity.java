package com.kidzbrain.login.menuLateral;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.kidzbrain.login.R;
import com.kidzbrain.notificaciones.NotificationHelper;
import com.kidzbrain.notificaciones.RecordatorioScheduler;

public class AjustesActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "KidzBrainPrefs";
    private static final String KEY_NOTIFICACIONES = "notificaciones_activas";
    private static final String KEY_VIBRACION = "vibracion_activa";
    private static final String KEY_VOLUMEN = "volumen_nivel";

    private SwitchMaterial switchNotificaciones, switchVibracion;
    private SeekBar seekbarVolumen;
    private ImageView btnMenu;
    private SharedPreferences prefs;
    private AudioManager audioManager;

    private final ActivityResultLauncher<String> notificacionesPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    prefs.edit().putBoolean(KEY_NOTIFICACIONES, true).apply();
                    NotificationHelper.crearCanal(this);
                    RecordatorioScheduler.programarRecordatorios(this, RecordatorioScheduler.DOS_VECES_AL_DIA);
                    Toast.makeText(this, "Notificaciones activadas", Toast.LENGTH_SHORT).show();
                } else {
                    prefs.edit().putBoolean(KEY_NOTIFICACIONES, false).apply();
                    switchNotificaciones.setChecked(false);
                    Toast.makeText(this, "No se concedió el permiso de notificaciones", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ajustes);

        switchNotificaciones = findViewById(R.id.switch_notificaciones);
        switchVibracion = findViewById(R.id.switch_vibracion);
        seekbarVolumen = findViewById(R.id.seekbar_volumen);
        btnMenu = findViewById(R.id.btnMenu);

        btnMenu.setOnClickListener(v -> {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        boolean notifActivas = prefs.getBoolean(KEY_NOTIFICACIONES, true);
        boolean vibActiva = prefs.getBoolean(KEY_VIBRACION, true);

        if (audioManager != null) {
            int volumenGuardado = prefs.getInt(KEY_VOLUMEN, audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            seekbarVolumen.setMax(maxVol);
            seekbarVolumen.setProgress(volumenGuardado);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumenGuardado, 0);
        }

        switchNotificaciones.setChecked(notifActivas);
        switchVibracion.setChecked(vibActiva);

        NotificationHelper.crearCanal(this);

        if (notifActivas) {
            RecordatorioScheduler.programarRecordatorios(this, RecordatorioScheduler.DOS_VECES_AL_DIA);
        }

        switchNotificaciones.setOnCheckedChangeListener((btn, isChecked) -> {
            if (isChecked) {
                activarNotificaciones();
            } else {
                prefs.edit().putBoolean(KEY_NOTIFICACIONES, false).apply();
                RecordatorioScheduler.cancelarRecordatorios(this);

                Toast.makeText(this, "Notificaciones desactivadas", Toast.LENGTH_SHORT).show();

                try {
                    Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                    startActivity(intent);
                } catch (Exception ignored) {
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
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
                prefs.edit().putInt(KEY_VOLUMEN, progress).apply();
            }

            public void onStartTrackingTouch(SeekBar sb) {}
            public void onStopTrackingTouch(SeekBar sb) {}
        });
    }

    private void activarNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED) {

                prefs.edit().putBoolean(KEY_NOTIFICACIONES, true).apply();
                NotificationHelper.crearCanal(this);
                RecordatorioScheduler.programarRecordatorios(this, RecordatorioScheduler.DOS_VECES_AL_DIA);

                Toast.makeText(this, "Notificaciones activadas", Toast.LENGTH_SHORT).show();
            } else {
                notificacionesPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        } else {
            prefs.edit().putBoolean(KEY_NOTIFICACIONES, true).apply();
            NotificationHelper.crearCanal(this);
            RecordatorioScheduler.programarRecordatorios(this, RecordatorioScheduler.DOS_VECES_AL_DIA);

            Toast.makeText(this, "Notificaciones activadas", Toast.LENGTH_SHORT).show();
        }
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