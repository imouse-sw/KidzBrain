package com.example.login; // Asegúrate de que este sea tu paquete correcto

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.utilidades.Animaciones; // Asegúrate de tener esta clase o ajusta el import

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Nuevas variables para los elementos del diseño "Nube"
    EditText etUser, etPass;
    Button btnLogin;
    TextView tvRegister;
    ImageView ivBrainbotFeliz, ivTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Encontrar las vistas con los NUEVOS IDs del XML
        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        ivTitle = findViewById(R.id.ivTitle);
        ivBrainbotFeliz = findViewById(R.id.ivBrainbotFeliz);

        // 2. Configurar Listeners
        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);

        // 3. Secuencia de Animaciones de Entrada (Cascada)
        // Aparecen en orden: Bebé -> Título -> Usuario -> Pass -> Botón -> Registro

        // Animación especial de rebote para el bebé
        ivBrainbotFeliz.setScaleX(0f);
        ivBrainbotFeliz.setScaleY(0f);
        ivBrainbotFeliz.animate()
                .scaleX(1f).scaleY(1f)
                .setDuration(800)
                .setInterpolator(new OvershootInterpolator()) // Efecto rebote
                .start();

        // Usando tu clase de utilidad Animaciones (asumiendo que funciona con alpha/translation)
        Animaciones.mostrarConAnimacion(ivTitle, 300);
        Animaciones.mostrarConAnimacion(etUser, 600);
        Animaciones.mostrarConAnimacion(etPass, 900);
        Animaciones.mostrarConAnimacion(btnLogin, 1200);
        Animaciones.mostrarConAnimacion(tvRegister, 1500);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnLogin) {
            // Lógica de Iniciar Sesión
            Intent intent = new Intent(MainActivity.this, Inicio.class);
            startActivity(intent);
            // Animación de transición suave (opcional)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        } else if (id == R.id.tvRegister) {
            // Lógica de Registro (Aquí iría a tu actividad de registro)
            // Intent intent = new Intent(MainActivity.this, Registro.class);
            // startActivity(intent);
        }
    }
}