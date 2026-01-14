package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.login.menuLateral.AjustesActivity;
import com.example.login.menuLateral.AvanceActivity;
import com.example.login.menuLateral.PerfilActivity;
import com.example.utilidades.leccionutil.mapa_niveles;
import com.google.android.material.navigation.NavigationView;

public class Inicio extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ConstraintLayout btnMatematicas, btnCiencias;
    private TextView tvNombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        btnMatematicas = findViewById(R.id.btnMatematicas);
        btnCiencias = findViewById(R.id.btnCiencias);
        tvNombreUsuario = findViewById(R.id.tv_nombre_usuario);

        // Cargar y mostrar el nombre del usuario
        cargarDatosUsuario();

        // Configuración del menú lateral
        ImageView btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(navigationView));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_perfil) {
                startActivity(new Intent(Inicio.this, PerfilActivity.class));
            } else if (id == R.id.nav_avance) {
                startActivity(new Intent(Inicio.this, AvanceActivity.class));
            } else if (id == R.id.nav_ajustes) {
                startActivity(new Intent(Inicio.this, AjustesActivity.class));
            } else if (id == R.id.nav_cerrar_sesion) {
                cerrarSesion();
            }
            drawerLayout.closeDrawers();
            return true;
        });

        // Listeners para las materias
        btnMatematicas.setOnClickListener(v -> abrirMapaNiveles("matematicas"));
        btnCiencias.setOnClickListener(v -> abrirMapaNiveles("ciencias"));
    }

    private void cargarDatosUsuario() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String nombre = prefs.getString("userName", "Invitado");
        tvNombreUsuario.setText("Hola, " + nombre);
    }

    private void abrirMapaNiveles(String materia) {
        Intent intent = new Intent(Inicio.this, mapa_niveles.class);
        intent.putExtra("materia", materia);
        startActivity(intent);
    }

    private void cerrarSesion() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear(); // Borra todos los datos de la sesión
        editor.apply();

        // Vuelve a la pantalla de Login
        Intent intent = new Intent(Inicio.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
