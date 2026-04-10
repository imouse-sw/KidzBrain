package com.kidzbrain.juegos.canicas;

import android.content.ClipData;
import android.content.ClipDescription;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.kidzbrain.login.R;

public class Canicas extends AppCompatActivity implements View.OnClickListener {

    ImageView canicaColores, canicaGuinda, canicaAmarilla, canicaTrans, canicaAzul;
    GridLayout bote1, bote2;
    Animation sacude;
    TextView problemita;
    EditText editadito;
    int n1, n2, respuestaReal, respuestaUsuario;
    Button comprobar;
    SoundPool soundPool;
    ImageButton btnBack;
    int correctoFxId, incorrectoFxId, canicaFxId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_canicas);

        canicaColores = findViewById(R.id.canica1);
        canicaGuinda = findViewById(R.id.canica2);
        canicaAmarilla = findViewById(R.id.canica3);
        canicaTrans = findViewById(R.id.canica4);
        canicaAzul = findViewById(R.id.canica5);

        bote1 = findViewById(R.id.bote_1);
        bote2 = findViewById(R.id.bote_2);
        
        problemita = findViewById(R.id.txt_problemita);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        comprobar = findViewById(R.id.btn_respuesta);
        editadito = findViewById(R.id.txt_respuesta);
        comprobar.setOnClickListener(this);

        canicaColores.setOnLongClickListener(longClickListener);
        canicaGuinda.setOnLongClickListener(longClickListener);
        canicaAmarilla.setOnLongClickListener(longClickListener);
        canicaTrans.setOnLongClickListener(longClickListener);
        canicaAzul.setOnLongClickListener(longClickListener);

        sacude = AnimationUtils.loadAnimation(this, R.anim.sacudelonena);

        bote1.setOnDragListener(onDragListener);
        bote2.setOnDragListener(onDragListener);

        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(5);
        soundPool = builder.build();

        CanicasMusica.reproduce(this);

        correctoFxId = soundPool.load(this, R.raw.aud_correcto, 1);
        incorrectoFxId = soundPool.load(this, R.raw.aud_incorrecto, 1);
        canicaFxId = soundPool.load(this, R.raw.aud_canicacae, 1);

        nuevaOperacion();
    }

    private void nuevaOperacion() {
        n1 = (int)(Math.random()*13) + 2;
        n2 = (int)(Math.random()*13) + 2;

        respuestaReal = n1+n2;
        problemita.setText(n1+" + "+n2+" = ?");

        editadito.setText("");

        bote1.removeAllViews();
        bote2.removeAllViews();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.btn_respuesta) {
            String respuestaTxt = editadito.getText().toString();

            if(respuestaTxt.trim().isEmpty()) {
                Toast.makeText(this, "¡Introduce una respuesta! 👀", Toast.LENGTH_SHORT).show();
                return;
            }

            respuestaUsuario = Integer.parseInt(respuestaTxt);

            if(respuestaUsuario == respuestaReal) {
                soundPool.play(correctoFxId, 1.0f, 1.0f, 1, 0, 1);
                Toast.makeText(this, "¡Correcto! ✅", Toast.LENGTH_SHORT).show();

                nuevaOperacion();
            }
            else {
                soundPool.play(incorrectoFxId, 1.0f, 1.0f, 1, 0, 1);
                Toast.makeText(this, "Prueba de nuevo. ❌", Toast.LENGTH_SHORT).show();
            }
        }
    }

    View.OnLongClickListener longClickListener = view -> {
        String canicatextito = view.getTag().toString();

        ClipData.Item item = new ClipData.Item(canicatextito);
        ClipData dragData =  new ClipData(
                canicatextito,
                new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                item
        );

        View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);
        view.startDragAndDrop(dragData, myShadow, null, 0);

        return true;
    };

    View.OnDragListener onDragListener = (v, event) -> {
        GridLayout bote = (GridLayout) v;

        int accion = event.getAction();

        if(accion == DragEvent.ACTION_DRAG_STARTED) {
            bote.startAnimation(sacude);
        }
        if(accion == DragEvent.ACTION_DRAG_ENTERED) {
            bote.setBackgroundResource(R.drawable.bote_canicas_activo);
        }
        else if(accion == DragEvent.ACTION_DRAG_EXITED) {
            bote.setBackgroundResource(R.drawable.bote_canicas);
        }
        else if(accion == DragEvent.ACTION_DROP) {
            String tag = event.getClipData().getItemAt(0).getText().toString();

            ImageView clonCanica = new ImageView(Canicas.this);

            if(tag.equals("canica_colores")) {
                clonCanica.setImageResource(R.drawable.canica1);
                clonCanica.setTag("canica_colores");
            }
            if(tag.equals("canica_guinda")) {
                clonCanica.setImageResource(R.drawable.canica2);
                clonCanica.setTag("canica_guinda");
            }
            if(tag.equals("canica_azul")) {
                clonCanica.setImageResource(R.drawable.canica6);
                clonCanica.setTag("canica_azul");
            }
            if(tag.equals("canica_amarilla")) {
                clonCanica.setImageResource(R.drawable.canica4);
                clonCanica.setTag("canica_amarilla");
            }
            if(tag.equals("canica_trans")) {
                clonCanica.setImageResource(R.drawable.canica5);
                clonCanica.setTag("canica_trans");
            }

            float density = getResources().getDisplayMetrics().density;
            int tamanitoEnPixelitos = (int) (32 * density);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();

            params.width = tamanitoEnPixelitos;
            params.height = tamanitoEnPixelitos;
            params.setMargins(8, 8, 8, 8);
            clonCanica.setLayoutParams(params);

            bote.addView(clonCanica);
        }
        else if(accion == DragEvent.ACTION_DRAG_ENDED) {
            bote.clearAnimation();
            bote.setBackgroundResource(R.drawable.bote_canicas);
            soundPool.play(canicaFxId, 1.0f, 1.0f, 1, 0, 1);
        }

        return true;
    };

    @Override
    protected void onPause() {
        super.onPause();
        CanicasMusica.pausa();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CanicasMusica.reproduce(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isFinishing()) {
            CanicasMusica.libera();
        }
    }
}
