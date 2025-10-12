package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText editTextUsuario, editTextPassword;
    Button btnIniciar, btnRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnIniciar = findViewById(R.id.btnIniciar);
        btnRegistro = findViewById(R.id.btnRegistro);

        // eto es pa que los botoncitos salgan retasaditos
        mostrarConAnimacion(editTextUsuario, 500);
        mostrarConAnimacion(editTextPassword, 1000);
        mostrarConAnimacion(btnIniciar, 1500);
        mostrarConAnimacion(btnRegistro, 2000);

        btnRegistro.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CrearCuenta.class);
            startActivity(intent);
        });
        
        btnIniciar.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Inicio.class);
            startActivity(intent);
        });
    }

    private void mostrarConAnimacion(final View view, int delayMillis) {
        new Handler().postDelayed(() -> {
            view.setVisibility(View.VISIBLE);
            AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
            fadeIn.setDuration(500);
            view.startAnimation(fadeIn);
        }, delayMillis);
    }
}
