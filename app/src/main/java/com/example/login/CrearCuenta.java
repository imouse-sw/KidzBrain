package com.example.login;

import com.example.api.Cliente;
import  com.example.utilidades.Animaciones;
import com.example.utilidades.Calendario;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.model.Usuarios;

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
            submitForm();
            Intent intent = new Intent(CrearCuenta.this, Inicio.class); // Ir a pantalla de inicio
            startActivity(intent);
        } else if (idsito == R.id.etFechaNacimiento) {
            Calendario.mostrarDatePicker(this, etFechaNacimiento);
        }
    }


    //Esto es un rompehuevos alch
    private void submitForm() {

        // 1. Obtener datos del formulario
        String name = etUsuario.getText().toString();
        String email = etCorreo.getText().toString();
        String password = etPassword.getText().toString();

        Usuarios newUsuarios = new Usuarios(name, email, password);
        Call<Usuarios> call = Cliente.getUsuariosService().createUsuarios(newUsuarios);

        call.enqueue(new Callback<Usuarios>() {
            @Override
            public void onResponse(Call<Usuarios> call, Response<Usuarios> response) {
                if (response.isSuccessful()) {
                    Usuarios createdUsuarios = response.body();
                    Log.d("POST_CALL", "Usuario creado con ID: " + createdUsuarios.getId());
                    Toast.makeText(getApplicationContext(), "Cuenta creada correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.e("POST_CALL", "Error HTTP: " + response.code());
                    Toast.makeText(getApplicationContext(), "Error al enviar datos: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Usuarios> call, Throwable t) {
                // Error de red (sin internet, URL incorrecta)
                Log.e("POST_CALL", "Fallo de red: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Fallo de conexión", Toast.LENGTH_LONG).show();
            }
        });
    }
}
