package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.api.Cliente;
import com.example.api.model.Usuarios;
import com.example.utilidades.Animaciones;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etEmailLogin, etPasswordLogin;//, editTextPassword;
    Button btnIniciar, btnRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmailLogin = findViewById(R.id.editTextUsuario);
        etPasswordLogin= findViewById(R.id.editTextPassword);
        
        btnIniciar = findViewById(R.id.btnIniciar);
        btnIniciar.setOnClickListener(this);

        btnRegistro = findViewById(R.id.btnRegistro);
        btnRegistro.setOnClickListener(this);


        // eto es pa que los botoncitos salgan retasaditos
        Animaciones.mostrarConAnimacion(etEmailLogin, 500);
        Animaciones.mostrarConAnimacion(etPasswordLogin, 1000);
        Animaciones.mostrarConAnimacion(btnIniciar, 1500);
       Animaciones.mostrarConAnimacion(btnRegistro, 2000);
    }


    //Para picarle a los botones jiji
    @Override
    public void onClick(View view) {
        int idsito = ((Button)view).getId();
        if (idsito == R.id.btnIniciar) {
            attemptLoginRapido();
        } else if (idsito == R.id.btnRegistro) {
            Intent intent = new Intent(MainActivity.this, CrearCuenta.class);
            startActivity(intent);
        }
    }

    private void attemptLoginRapido() {
        String email = etEmailLogin.getText().toString();
        String password = etPasswordLogin.getText().toString();

        // Obtener la llamada usando el correo
        Call<Usuarios> call = Cliente.getUsuariosService().getUsuarioByCorreo(email);

        call.enqueue(new Callback<Usuarios>() {
            @Override
            public void onResponse(Call<Usuarios> call, Response<Usuarios> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // ÉXITO: Usuario encontrado (Código 200 OK)
                    Usuarios usuarioEncontrado = response.body();

                    // 1. Validar la contraseña localmente
                    if (password.equals(usuarioEncontrado.getPassword())) {
                        // Contraseña válida: Inicio de sesión exitoso
                        Toast.makeText(getApplicationContext(), "¡Bienvenido, " + usuarioEncontrado.getNombre() + "!", Toast.LENGTH_SHORT).show();

                        // ➡️ GUARDAR ID/NOMBRE DE USUARIO para la sesión (ej. Shared Preferences)
                        // y navegar a la pantalla principal.
                        startActivity(new Intent(getApplicationContext(), Inicio.class));
                        finish();
                    } else {
                        // Contraseña incorrecta
                        Toast.makeText(getApplicationContext(), "Contraseña incorrecta.", Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 404) {
                    // Usuario no encontrado
                    Toast.makeText(getApplicationContext(), "Correo no registrado.", Toast.LENGTH_LONG).show();
                } else {
                    // Otro error HTTP
                    Toast.makeText(getApplicationContext(), "Error en el servidor: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Usuarios> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fallo de conexión.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
