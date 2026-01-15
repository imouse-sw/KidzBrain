package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.spring.ApiService;
import com.example.spring.RetrofitClient;
import com.example.spring.dto.UsuarioDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        verificarSesion();

        etEmail = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        apiService = RetrofitClient.getApiService();

        // --- ANIMACIONES ---
        aplicarAnimaciones();

        btnLogin.setOnClickListener(v -> handleLogin());
        tvRegister.setOnClickListener(v -> {
            pedirAutenticacionDelDispositivo();
        });

        etEmail.setText("");
        etPassword.setText("");
    }

    private void pedirAutenticacionDelDispositivo() {
        // 1. Verificar si el hardware lo soporta
        BiometricManager biometricManager = BiometricManager.from(this);
        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG |
                BiometricManager.Authenticators.DEVICE_CREDENTIAL) != BiometricManager.BIOMETRIC_SUCCESS) {
            // Si no hay seguridad configurada en el celular, abrimos directo (o muestras error)
            abrirCrearCuenta();
            return;
        }

        // 2. Preparar el ejecutor
        Executor executor = ContextCompat.getMainExecutor(this);

        // 3. Configurar el callback (qué pasa si tiene éxito o falla)
        BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // ¡ÉXITO! Aquí abres la actividad
                abrirCrearCuenta();
            }

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(MainActivity.this, "Error de autenticación: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                // Huella incorrecta, etc. (El sistema suele manejar los reintentos solo)
            }
        });

        // 4. Configurar el diseño del aviso
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación requerida")
                .setSubtitle("Usa tu huella o PIN para crear una cuenta")
                // Esta línea permite usar PIN/Patrón si no hay huella
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG |
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build();

        // 5. Mostrar el aviso
        biometricPrompt.authenticate(promptInfo);
    }

    private void abrirCrearCuenta() {
        Intent intent = new Intent(MainActivity.this, CrearCuentaActivity.class);
        startActivity(intent);
    }

    private void aplicarAnimaciones() {
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        // Vistas a animar
        ImageView ivBrainbot = findViewById(R.id.ivBrainbotFeliz);
        ImageView ivTitle = findViewById(R.id.ivTitle);
        TextView tvSubtitle = findViewById(R.id.tvSubtitle);
        LinearLayout loginCard = findViewById(R.id.login_card);

        // Aplicar animaciones
        ivBrainbot.startAnimation(fadeIn);
        ivTitle.startAnimation(fadeIn);
        tvSubtitle.startAnimation(fadeIn);
        loginCard.startAnimation(slideUp);
    }

    private void verificarSesion() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        if (userId != -1) {
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
                    if (password.equals(usuario.getPassword())) {
                        Toast.makeText(MainActivity.this, "¡Bienvenido, " + usuario.getNombre() + "!", Toast.LENGTH_SHORT).show();

                        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("userId", usuario.getUsuarioId());
                        editor.putString("userName", usuario.getNombre());
                        editor.putString("userEmail", usuario.getCorreo());
                        editor.apply();

                        Intent intent = new Intent(MainActivity.this, Inicio.class);
                        startActivity(intent);
                        finish();

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
