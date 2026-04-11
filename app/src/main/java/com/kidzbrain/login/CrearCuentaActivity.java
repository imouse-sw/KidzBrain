package com.kidzbrain.login;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kidzbrain.spring.ApiService;
import com.kidzbrain.spring.RetrofitClient;
import com.kidzbrain.spring.dto.UsuarioDto;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearCuentaActivity extends AppCompatActivity {

    private EditText etNombre, etEmail, etPassword;
    private TextView tvFechaNacimiento;
    private Button btnRegistrar;
    private TextView tvVolverLogin;
    private ApiService apiService;
    private Integer edadCalculada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        etNombre = findViewById(R.id.et_nombre_registro);
        etEmail = findViewById(R.id.et_email_registro);
        etPassword = findViewById(R.id.et_password_registro);
        tvFechaNacimiento = findViewById(R.id.tv_fecha_nacimiento);
        btnRegistrar = findViewById(R.id.btn_registrar);
        tvVolverLogin = findViewById(R.id.tv_volver_login);

        apiService = RetrofitClient.getApiService();

        tvFechaNacimiento.setOnClickListener(v -> mostrarSelectorFecha());
        btnRegistrar.setOnClickListener(v -> registrarUsuario());
        tvVolverLogin.setOnClickListener(v -> finish());
    }

    private void mostrarSelectorFecha() {
        final Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    String fechaSeleccionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                    tvFechaNacimiento.setText(fechaSeleccionada);
                    edadCalculada = calcularEdad(year, monthOfYear, dayOfMonth);
                }, anio, mes, dia);
        datePickerDialog.show();
    }

    private int calcularEdad(int anioNac, int mesNac, int diaNac) {
        Calendar fechaNac = Calendar.getInstance();
        fechaNac.set(anioNac, mesNac, diaNac);
        Calendar fechaActual = Calendar.getInstance();

        int edad = fechaActual.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR);
        if (fechaActual.get(Calendar.DAY_OF_YEAR) < fechaNac.get(Calendar.DAY_OF_YEAR)){
            edad--;
        }
        return edad;
    }


    private void registrarUsuario() {
        String nombre = etNombre.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() || edadCalculada == null) {
            Toast.makeText(this, "Por favor, ¡llena todos los campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        // VALIDACIÓN DE EDAD MÍNIMA (5 AÑOS)
        if (edadCalculada < 5) {
            Toast.makeText(this, "Lo sentimos, debes tener al menos 5 años para jugar en KidzBrain.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "El correo electrónico no es válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        UsuarioDto nuevoUsuario = new UsuarioDto(nombre, email, password, edadCalculada);

        apiService.crearUsuario(nuevoUsuario).enqueue(new Callback<UsuarioDto>() {
            @Override
            public void onResponse(Call<UsuarioDto> call, Response<UsuarioDto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CrearCuentaActivity.this, "¡Cuenta creada con éxito!", Toast.LENGTH_SHORT).show();
                    finish(); // Regresa a la pantalla de login
                } else {
                    Toast.makeText(CrearCuentaActivity.this, "Error al crear la cuenta.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsuarioDto> call, Throwable t) {
                Toast.makeText(CrearCuentaActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
