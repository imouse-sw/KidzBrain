package com.kidzbrain.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.kidzbrain.utilidades.Animaciones;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextUsuario, editTextPassword;
    Button btnIniciar, btnRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextPassword = findViewById(R.id.editTextPassword);
        
        btnIniciar = findViewById(R.id.btnIniciar);
        btnIniciar.setOnClickListener(this);

        btnRegistro = findViewById(R.id.btnRegistro);
        btnRegistro.setOnClickListener(this);


        // eto es pa que los botoncitos salgan retasaditos
        Animaciones.mostrarConAnimacion(editTextUsuario, 500);
        Animaciones.mostrarConAnimacion(editTextPassword, 1000);
        Animaciones.mostrarConAnimacion(btnIniciar, 1500);
       Animaciones.mostrarConAnimacion(btnRegistro, 2000);
    }


    //Para picarle a los botones jiji
    @Override
    public void onClick(View view) {
        int idsito = ((Button)view).getId();
        if (idsito == R.id.btnIniciar) {
            Intent intent = new Intent(MainActivity.this, Inicio.class);
            startActivity(intent);
        } else if (idsito == R.id.btnRegistro) {
            Intent intent = new Intent(MainActivity.this, CrearCuenta.class);
            startActivity(intent);
        }
    }

}
