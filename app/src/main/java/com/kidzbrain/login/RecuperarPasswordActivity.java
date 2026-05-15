package com.kidzbrain.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kidzbrain.spring.ApiService;
import com.kidzbrain.spring.RetrofitClient;
import com.kidzbrain.spring.dto.RestablecerPasswordDto;
import com.kidzbrain.spring.dto.SolicitudRecuperacionDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecuperarPasswordActivity extends AppCompatActivity {

    private LinearLayout layoutPaso1, layoutPaso2;
    private EditText etCorreo, etCodigo, etNuevaPassword, etConfirmarPassword;
    private Button btnEnviarCodigo, btnRestablecerPassword;
    private ApiService apiService;
    private TextView tvVolverLogin1, tvVolverLogin2;
    private String correoGuardado = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_password);

        apiService = RetrofitClient.getApiService(this);

        layoutPaso1 = findViewById(R.id.layoutPaso1);
        layoutPaso2 = findViewById(R.id.layoutPaso2);
        etCorreo = findViewById(R.id.etCorreoRecuperacion);
        etCodigo = findViewById(R.id.etCodigo);
        etNuevaPassword = findViewById(R.id.etNuevaPassword);
        etConfirmarPassword = findViewById(R.id.etConfirmarPassword);
        btnEnviarCodigo = findViewById(R.id.btnEnviarCodigo);
        btnRestablecerPassword = findViewById(R.id.btnRestablecerPassword);
        tvVolverLogin1 = findViewById(R.id.tvVolverLogin1);
        tvVolverLogin2 = findViewById(R.id.tvVolverLogin2);

        tvVolverLogin1.setOnClickListener(v -> finish());
        tvVolverLogin2.setOnClickListener(v -> finish());

        btnEnviarCodigo.setOnClickListener(v -> solicitarCodigo());
        btnRestablecerPassword.setOnClickListener(v -> cambiarPassword());
    }

    private void solicitarCodigo() {
        String correo = etCorreo.getText().toString().trim();
        if (correo.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa tu correo", Toast.LENGTH_SHORT).show();
            return;
        }

        btnEnviarCodigo.setEnabled(false);
        btnEnviarCodigo.setText("Enviando...");

        SolicitudRecuperacionDto dto = new SolicitudRecuperacionDto(correo);

        apiService.solicitarRecuperacion(dto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    correoGuardado = correo;
                    layoutPaso1.setVisibility(View.GONE);
                    layoutPaso2.setVisibility(View.VISIBLE);
                    Toast.makeText(RecuperarPasswordActivity.this, "Código enviado a tu correo electrónico!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RecuperarPasswordActivity.this, "Error: Correo no encontrado.", Toast.LENGTH_SHORT).show();
                    btnEnviarCodigo.setEnabled(true);
                    btnEnviarCodigo.setText("Enviar código");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RecuperarPasswordActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                btnEnviarCodigo.setEnabled(true);
                btnEnviarCodigo.setText("Enviar código");
            }
        });
    }

    private void cambiarPassword() {
        String codigo = etCodigo.getText().toString().trim();
        String nuevaPass = etNuevaPassword.getText().toString().trim();
        String confirmarPass = etConfirmarPassword.getText().toString().trim();

        if (codigo.length() < 6 || nuevaPass.isEmpty() || confirmarPass.isEmpty()) {
            Toast.makeText(this, "Por favor, llena todos los campos correctamente", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!nuevaPass.equals(confirmarPass)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        btnRestablecerPassword.setEnabled(false);

        RestablecerPasswordDto dto = new RestablecerPasswordDto(correoGuardado, codigo, nuevaPass);

        apiService.restablecerPassword(dto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RecuperarPasswordActivity.this, "¡Contraseña cambiada con éxito!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(RecuperarPasswordActivity.this, "Código incorrecto o expirado.", Toast.LENGTH_SHORT).show();
                    btnRestablecerPassword.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RecuperarPasswordActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                btnRestablecerPassword.setEnabled(true);
            }
        });
    }
}