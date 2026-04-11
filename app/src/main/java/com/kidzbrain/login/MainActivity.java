package com.kidzbrain.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.kidzbrain.spring.ApiService;
import com.kidzbrain.spring.RetrofitClient;
import com.kidzbrain.spring.dto.LoginRequest;
import com.kidzbrain.spring.dto.UsuarioDto;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private CheckBox cbKeepSession;
    private ImageView ivInfoKeepSession;
    private FrameLayout layoutInfoOverlay;
    private Button btnCloseInfo;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        verificarSesion();

        // Inicializar Vistas
        etEmail = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        cbKeepSession = findViewById(R.id.cbKeepSession);
        ivInfoKeepSession = findViewById(R.id.ivInfoKeepSession);
        layoutInfoOverlay = findViewById(R.id.layoutInfoOverlay);
        btnCloseInfo = findViewById(R.id.btnCloseInfo);

        apiService = RetrofitClient.getApiService();

        aplicarAnimaciones();

        // Listeners
        btnLogin.setOnClickListener(v -> handleLogin());
        tvRegister.setOnClickListener(v -> pedirAutenticacionDelDispositivo());

        // Manejo del Overlay de información
        ivInfoKeepSession.setOnClickListener(v -> {
            layoutInfoOverlay.setVisibility(View.VISIBLE);
            layoutInfoOverlay.setAlpha(0f);
            layoutInfoOverlay.animate().alpha(1f).setDuration(300).start();
        });

        btnCloseInfo.setOnClickListener(v -> {
            layoutInfoOverlay.animate().alpha(0f).setDuration(300).withEndAction(() -> {
                layoutInfoOverlay.setVisibility(View.GONE);
            }).start();
        });

        etEmail.setText("");
        etPassword.setText("");
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa correo y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest(email, password);

        // Llamamos al nuevo endpoint seguro
        apiService.loginUsuario(request).enqueue(new Callback<UsuarioDto>() {
            @Override
            public void onResponse(@NonNull Call<UsuarioDto> call, @NonNull Response<UsuarioDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UsuarioDto usuario = response.body();

                    Toast.makeText(MainActivity.this, "¡Bienvenido, " + usuario.getNombre() + "!", Toast.LENGTH_SHORT).show();

                    SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putInt("userId", usuario.getUsuarioId());
                    editor.putString("userName", usuario.getNombre());
                    editor.putString("userEmail", usuario.getCorreo());
                    editor.putBoolean("keepSession", cbKeepSession.isChecked());

                    // Manejo de la foto de perfil
                    if (usuario.getFotoUrl() != null && !usuario.getFotoUrl().isEmpty()) {
                        editor.putString("tipo_foto", "server");
                        editor.putString("url_foto_server", usuario.getFotoUrl());
                    } else {
                        editor.putString("tipo_foto", "ninguna");
                        editor.remove("url_foto_server");
                    }

                    editor.apply();

                    Intent intent = new Intent(MainActivity.this, Inicio.class);
                    startActivity(intent);
                    finish();

                } else if (response.code() == 401) {
                    Toast.makeText(MainActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(MainActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UsuarioDto> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pedirAutenticacionDelDispositivo() {
        BiometricManager biometricManager = BiometricManager.from(this);
        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG |
                BiometricManager.Authenticators.DEVICE_CREDENTIAL) != BiometricManager.BIOMETRIC_SUCCESS) {
            abrirCrearCuenta();
            return;
        }

        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
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
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación requerida")
                .setSubtitle("Usa tu huella o PIN para crear una cuenta")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG |
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private void abrirCrearCuenta() {
        Intent intent = new Intent(MainActivity.this, CrearCuentaActivity.class);
        startActivity(intent);
    }

    private void aplicarAnimaciones() {
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        ImageView ivBrainbot = findViewById(R.id.ivBrainbotFeliz);
        ImageView ivTitle = findViewById(R.id.ivTitle);
        TextView tvSubtitle = findViewById(R.id.tvSubtitle);
        LinearLayout loginCard = findViewById(R.id.login_card);

        if(ivBrainbot != null) ivBrainbot.startAnimation(fadeIn);
        if(ivTitle != null) ivTitle.startAnimation(fadeIn);
        if(tvSubtitle != null) tvSubtitle.startAnimation(fadeIn);
        if(loginCard != null) loginCard.startAnimation(slideUp);
    }

    private void verificarSesion() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        boolean keepSession = prefs.getBoolean("keepSession", false);

        if (userId != -1) {
            if (keepSession) {
                Intent intent = new Intent(MainActivity.this, Inicio.class);
                startActivity(intent);
                finish();
            } else {
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();
            }
        }
    }
}
