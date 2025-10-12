package com.example.login;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class CrearCuenta extends AppCompatActivity {

    EditText etUsuario, etFechaNacimiento, etCorreo, etPassword;
    Button btnCrearCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        etUsuario = findViewById(R.id.etUsuario);
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento);
        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);

        // eto es pa que los botoncitos salgan retasaditos
        mostrarConAnimacion(etUsuario, 500);
        mostrarConAnimacion(etFechaNacimiento, 1000);
        mostrarConAnimacion(etCorreo, 1500);
        mostrarConAnimacion(etPassword, 2000);
        mostrarConAnimacion(btnCrearCuenta, 2500);

        etFechaNacimiento.setOnClickListener(v -> mostrarDatePicker());

        btnCrearCuenta.setOnClickListener(view -> {
            Toast.makeText(this, "Cuenta creada correctamente (simulación)", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CrearCuenta.this, Inicio.class); // Ir a pantalla de inicio
            startActivity(intent);
        });
    }

    // eto es pa que los botoncitos salgan retasaditos
    private void mostrarConAnimacion(final View view, int delayMillis) {
        new Handler().postDelayed(() -> {
            view.setVisibility(View.VISIBLE);
            AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
            fadeIn.setDuration(500);
            view.startAnimation(fadeIn);
        }, delayMillis);
    }

    // el menu de cuando nacio y eso
    private void mostrarDatePicker() {
        final Calendar c = Calendar.getInstance();
        int año = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;
                    etFechaNacimiento.setText(fechaSeleccionada);
                }, año, mes, dia);

        datePickerDialog.show();
    }
}
