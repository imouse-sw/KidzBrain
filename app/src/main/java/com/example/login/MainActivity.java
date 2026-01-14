package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spring.ApiService;
import com.example.spring.RetrofitClient;
import com.example.spring.dto.UsuarioDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Comprobar si ya hay una sesión activa
        verificarSesion();

        etEmail = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        apiService = RetrofitClient.getApiService();

        btnLogin.setOnClickListener(v -> handleLogin());
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CrearCuentaActivity.class);
            startActivity(intent);
        });

        etEmail.setText("");
        etPassword.setText("");
    }

    private void verificarSesion() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        if (userId != -1) {
            // Si ya hay un ID de usuario, ir directamente a la pantalla de Inicio
            Intent intent = new Intent(MainActivity.this, Inicio.class);
            startActivity(intent);
            finish();
        }
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa correo y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.getUsuarioPorCorreo(email).enqueue(new Callback<UsuarioDto>() {
            @Override
            public void onResponse(Call<UsuarioDto> call, Response<UsuarioDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UsuarioDto usuario = response.body();
                    // Comparamos la contraseña en el lado del cliente
                    if (password.equals(usuario.getPassword())) {
                        // ¡Login exitoso!
                        Toast.makeText(MainActivity.this, "¡Bienvenido, " + usuario.getNombre() + "!", Toast.LENGTH_SHORT).show();

                        // Guardar datos del usuario
                        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("userId", usuario.getUsuarioId());
                        editor.putString("userName", usuario.getNombre());
                        editor.putString("userEmail", usuario.getCorreo()); // <-- AQUÍ ESTÁ LA LÍNEA QUE FALTABA
                        editor.apply();

                        // Navegar a la pantalla de inicio
                        Intent intent = new Intent(MainActivity.this, Inicio.class);
                        startActivity(intent);
                        finish(); // Cierra la actividad de login

                    } else {
                        Toast.makeText(MainActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsuarioDto> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
