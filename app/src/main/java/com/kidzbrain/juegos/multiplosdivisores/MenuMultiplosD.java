package com.kidzbrain.juegos.multiplosdivisores; // Ajusta el paquete si lo pones en otra carpeta

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.kidzbrain.login.R;

import java.util.function.Consumer;

public class MenuMultiplosD extends AppCompatActivity implements View.OnClickListener {
    Button facil, normal;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_multiplos_d);

        facil = findViewById(R.id.bPractica);
        normal = findViewById(R.id.bNormal);

        MediaPlayer mpVozInstrucciones = MediaPlayer.create(this, R.raw.aud_voz_multiplos);
        mpVozInstrucciones.start();
        mpVozInstrucciones.setOnCompletionListener(MediaPlayer::release);

        facil.setOnClickListener(this);
        normal.setOnClickListener(this);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // GlobosMusica.reproduce(this); // Descomenta si tienes tu clase de música
    }

    @Override
    public void onClick(View v) {
        if(v instanceof Button) {
            int id = v.getId();

            // Apuntamos a la nueva actividad del juego
            Intent intentito = new Intent(this, MultiplosDivisores.class);

            if (id == R.id.bPractica) {
                // Mandamos false para indicar que NO es modo difícil
                intentito.putExtra("esModoDificil", false);
            }
            else if (id == R.id.bNormal) {
                // Mandamos true para indicar que SÍ es modo difícil
                intentito.putExtra("esModoDificil", true);
            }

            startActivity(intentito);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isFinishing()) {
            MultiplosDMusica.libera();
        }
    }
}