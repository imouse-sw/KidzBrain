package com.example.login;

import  com.example.utilidades.Animaciones;
import com.example.utilidades.Calendario;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class CrearCuenta extends AppCompatActivity implements View.OnClickListener {

    EditText etUsuario, etFechaNacimiento, etCorreo, etPassword;
    Button btnCrearCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        etUsuario = findViewById(R.id.etUsuario);
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento);
        etFechaNacimiento.setOnClickListener(this);

        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);

        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);
        btnCrearCuenta.setOnClickListener(this);

        // eto es pa que los botoncitos salgan retasaditos
        Animaciones.mostrarConAnimacion(etUsuario, 500);
        Animaciones.mostrarConAnimacion(etFechaNacimiento, 1000);
        Animaciones.mostrarConAnimacion(etCorreo, 1500);
        Animaciones.mostrarConAnimacion(etPassword, 2000);
        Animaciones.mostrarConAnimacion(btnCrearCuenta, 2500);
    }

    //Para cuando le picas a lo botones hagan su función.
    @Override
    public void onClick(View view) {
        int idsito = view.getId();
        if (idsito == R.id.btnCrearCuenta) {
            Toast.makeText(this, "Cuenta creada correctamente (simulación)", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CrearCuenta.this, Inicio.class); // Ir a pantalla de inicio
            startActivity(intent);
        } else if (idsito == R.id.etFechaNacimiento) {
            Calendario.mostrarDatePicker(this, etFechaNacimiento);
        }
    }
}
