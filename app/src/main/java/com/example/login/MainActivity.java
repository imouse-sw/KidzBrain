package com.example.login;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.utilidades.Animaciones;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etUser, etPass;
    Button btnLogin;
    TextView tvRegister;
    ImageView ivBrainbotFeliz, ivTitle;

    private static final String CHANNEL_ID = "CANAL_KIDZBRAIN";
    private static final int REQ_NOTIS = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        ivTitle = findViewById(R.id.ivTitle);
        ivBrainbotFeliz = findViewById(R.id.ivBrainbotFeliz);

        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);

        ivBrainbotFeliz.setScaleX(0f);
        ivBrainbotFeliz.setScaleY(0f);
        ivBrainbotFeliz.animate()
                .scaleX(1f).scaleY(1f)
                .setDuration(800)
                .setInterpolator(new OvershootInterpolator())
                .start();

        Animaciones.mostrarConAnimacion(ivTitle, 300);
        Animaciones.mostrarConAnimacion(etUser, 600);
        Animaciones.mostrarConAnimacion(etPass, 900);
        Animaciones.mostrarConAnimacion(btnLogin, 1200);
        Animaciones.mostrarConAnimacion(tvRegister, 1500);

        crearCanalDeNotificaciones();
        pedirPermisoNotificaciones();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLogin) {
            startActivity(new Intent(MainActivity.this, Inicio.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    private void crearCanalDeNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Avisos KidzBrain",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notificaciones de aprendizaje y juego");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }
    }

    private void pedirPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        REQ_NOTIS
                );
            } else {
                lanzarNotificacionBienvenida();
            }
        } else {
            lanzarNotificacionBienvenida();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_NOTIS && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            lanzarNotificacionBienvenida();
        }
    }

    private void lanzarNotificacionBienvenida() {

        SharedPreferences prefs = getSharedPreferences("KidzBrainPrefs", MODE_PRIVATE);
        if (!prefs.getBoolean("notificaciones_activas", true)) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        String[] titulos = {
                "Brainbot te estaba esperando",
                "Hora de aprender algo nuevo",
                "Tu aventura comienza ahora"
        };

        String[] mensajes = {
                "Cinco minutos hoy hacen magia mañana",
                "Vamos a entrenar tu mente",
                "Cada día eres más inteligente"
        };

        int i = new Random().nextInt(titulos.length);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pi = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(titulos[i])
                .setContentText(mensajes[i])
                .setAutoCancel(true)
                .setContentIntent(pi);

        try {
            NotificationManagerCompat.from(this).notify(101, builder.build());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

}
