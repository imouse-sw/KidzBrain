package com.example.login;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Inicio extends AppCompatActivity {

    private FrameLayout btnMatematicas, btnEspanol, btnCiencias;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        btnMenu = findViewById(R.id.btnMenu);

        btnMatematicas = findViewById(R.id.btnMatematicas);
        btnEspanol = findViewById(R.id.btnEspanol);
        btnCiencias = findViewById(R.id.btnCiencias);

        // eto es pa que los botoncitos salgan retasaditos
        mostrarConAnimacion(btnMatematicas, 300);
        mostrarConAnimacion(btnEspanol, 600);
        mostrarConAnimacion(btnCiencias, 900);

        btnMatematicas.setOnClickListener(v ->
                Toast.makeText(this, "Abrir Matemáticas", Toast.LENGTH_SHORT).show());

        btnEspanol.setOnClickListener(v ->
                Toast.makeText(this, "Abrir Español", Toast.LENGTH_SHORT).show());

        btnCiencias.setOnClickListener(v ->
                Toast.makeText(this, "Abrir Ciencias Naturales", Toast.LENGTH_SHORT).show());

        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(navigationView));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_perfil) {
                Toast.makeText(this, "Perfil seleccionado", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_avance) {
                Toast.makeText(this, "Avance seleccionado", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_ajustes) {
                Toast.makeText(this, "Ajustes seleccionado", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_cerrar_sesion) {
                Toast.makeText(this, "Cerrar sesión seleccionado", Toast.LENGTH_SHORT).show();
            }
            drawerLayout.closeDrawer(navigationView);
            return true;
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    setEnabled(false); // Deshabilita este callback para que el sistema maneje el botón atrás
                    onBackPressed();
                }
            }
        });
    }

    private void mostrarConAnimacion(View view, int delayMillis) {
        new Handler().postDelayed(() -> {
            view.setVisibility(View.VISIBLE);

            TranslateAnimation slideUp = new TranslateAnimation(0, 0, 100, 0);
            slideUp.setDuration(500);

            AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
            fadeIn.setDuration(500);

            view.startAnimation(slideUp);
            view.startAnimation(fadeIn);
        }, delayMillis);
    }
}
