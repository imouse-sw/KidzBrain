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
    private TextView tvRegister, btnAuthNo, tvOlvidePassword;
    private CheckBox cbKeepSession;
    private ImageView ivInfoKeepSession, btnCloseAuth;
    private FrameLayout layoutInfoOverlay;
    private Button btnCloseInfo;
    private ApiService apiService;
    private FrameLayout layoutAuthOverlay;
    private Button btnAuthYes;

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
        cbKeepSession = findViewById(R.id.cbKeepSession);
        ivInfoKeepSession = findViewById(R.id.ivInfoKeepSession);
        layoutInfoOverlay = findViewById(R.id.layoutInfoOverlay);
        btnCloseInfo = findViewById(R.id.btnCloseInfo);
        layoutInfoOverlay = findViewById(R.id.layoutInfoOverlay);
        btnCloseInfo = findViewById(R.id.btnCloseInfo);
        layoutAuthOverlay = findViewById(R.id.layoutAuthOverlay);
        btnAuthYes = findViewById(R.id.btnAuthYes);
        btnAuthNo = findViewById(R.id.btnAuthNo);
        btnCloseAuth = findViewById(R.id.btnCloseAuth);
        tvOlvidePassword = findViewById(R.id.tvOlvidePassword);

        apiService = RetrofitClient.getApiService(this);

        aplicarAnimaciones();

        tvOlvidePassword.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecuperarPasswordActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> handleLogin());
        tvRegister.setOnClickListener(v -> manejarClicRegistro());

        btnAuthYes.setOnClickListener(v -> {
            guardarPreferenciaAutenticacion(true); // Guardamos que SÍ quiere seguridad
            ocultarOverlayAutenticacion();
            pedirAutenticacionDelDispositivo();
        });

        btnAuthNo.setOnClickListener(v -> {
            guardarPreferenciaAutenticacion(false); // Guardamos que NO quiere seguridad
            ocultarOverlayAutenticacion();
            abrirCrearCuenta();
        });

        if (btnCloseAuth != null) {
            btnCloseAuth.setOnClickListener(v -> ocultarOverlayAutenticacion());
        }

        // manejo de overlay del apartado de información de "mantener sesión"
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

        // se llama al endpoint "/login/" del backend
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

                    // manejo de la foto de perfil
                    if (usuario.getFotoUrl() != null && !usuario.getFotoUrl().isEmpty()) {
                        editor.putString("tipo_foto", "server");
                        editor.putString("url_foto_server", usuario.getFotoUrl());
                    } else {
                        editor.putString("tipo_foto", "ninguna");
                        editor.remove("url_foto_server");
                    }
                    editor.putString("userToken", usuario.getToken());

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

    private void mostrarOverlayAutenticacion() {
        if (layoutAuthOverlay != null) {
            layoutAuthOverlay.setVisibility(View.VISIBLE);
            layoutAuthOverlay.setAlpha(0f);
            layoutAuthOverlay.animate().alpha(1f).setDuration(300).start();
        }
    }

    private void ocultarOverlayAutenticacion() {
        if (layoutAuthOverlay != null) {
            layoutAuthOverlay.animate().alpha(0f).setDuration(300).withEndAction(() -> {
                layoutAuthOverlay.setVisibility(View.GONE);
            }).start();
        }
    }

    private void guardarPreferenciaAutenticacion(boolean requiereSeguridad) {
        // usamos un archivo de preferencias distinto al de sesión para mayor orden
        SharedPreferences prefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("require_auth_registro", requiereSeguridad);
        editor.putBoolean("overlay_auth_mostrado", true); // Marcamos que ya vimos la tarjeta
        editor.apply();
    }

    // decide a dónde llevar al usuario cuando presiona "Regístrate"
    private void manejarClicRegistro() {
        SharedPreferences prefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE);

        // Revisamos si ya le mostramos la tarjeta alguna vez en el pasado
        boolean yaSeMostroTarjeta = prefs.getBoolean("overlay_auth_mostrado", false);

        if (!yaSeMostroTarjeta) {
            // no se ha mostrado la tarjeta de preferencias así que se debe mostrar
            mostrarOverlayAutenticacion();
        } else {
            // se lee la decisión previa
            boolean requiereSeguridad = prefs.getBoolean("require_auth_registro", false);

            if (requiereSeguridad) {
                pedirAutenticacionDelDispositivo(); // Le pedimos la huella
            } else {
                abrirCrearCuenta(); // pasa sin huella
            }
        }
    }
}
