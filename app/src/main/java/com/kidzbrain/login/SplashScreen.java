package com.kidzbrain.login;

import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    SoundPool soundPool;
    Handler handler;
    Runnable runnableNavegacion;
    int sonidoLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        ImageView logoImouse = findViewById(R.id.img_logo_imouse);

        handler = new Handler(Looper.getMainLooper());

        soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        sonidoLogo = soundPool.load(this, R.raw.sfx_click, 1);

        logoImouse.setAlpha(0f);
        logoImouse.animate().alpha(1f).setDuration(2000).start();
        logoImouse.setAlpha(1f);

        handler.postDelayed(() -> {
            if (soundPool != null) {
                soundPool.play(sonidoLogo, 1, 1, 0, 0, 1);
            }
        }, 1000);

        runnableNavegacion = () -> {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        };
        handler.postDelayed(runnableNavegacion, 2500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}