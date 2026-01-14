package com.example.login.menuLateral;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.login.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Random;

public class PerfilActivity extends AppCompatActivity {

    // Vistas
    private ImageView ivPerfil;
    private View btnCambiarFoto;
    private TextView tvFraseMotivadora;
    private TextView tvNombre;

    // Para guardar datos
    private SharedPreferences prefs;

    // Lista de Frases Random
    private final String[] FRASES = {
            "¡Explorador KidzBrain!",
            "¡Futuro Genio!",
            "¡Experto en Matemáticas!",
            "¡Científico Loco!",
            "¡Súper Estudiante!",
            "¡Campeón del Saber!"
    };

    // Tus avatares
    private final int[] AVATARES_PREDEFINIDOS = {
            R.drawable.avatar_1,
            R.drawable.avatar_2,
            R.drawable.avatar_3,
            R.drawable.avatar_4,
            R.drawable.avatar_5,
            R.drawable.avatar_6,
            R.drawable.avatar_7,
            R.drawable.avatar_8,
            R.drawable.avatar_9,
            R.drawable.avatar_10
    };

    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);

        // Inicializar vistas
        ivPerfil = findViewById(R.id.imagen_perfil);
        btnCambiarFoto = findViewById(R.id.btnCambiarFoto);
        tvFraseMotivadora = findViewById(R.id.tvFraseMotivadora);
        tvNombre = findViewById(R.id.nombre_usuario);

        prefs = getSharedPreferences("KidzBrainPrefs", MODE_PRIVATE);

        // Configurar lanzadores
        configurarGalleryLauncher();

        // Cargar datos
        cargarDatosGuardados();

        // Poner frase
        ponerFraseAleatoria();

        // Listener para cambiar foto
        btnCambiarFoto.setOnClickListener(v -> mostrarDialogoSeleccion());

        // --- LÓGICA DEL BOTÓN MENÚ / VOLVER ---
        // Buscamos el botón por su ID (Asegúrate que en el XML se llame btnMenu)
        View btnMenu = findViewById(R.id.btnMenu);

        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> {
                finish(); // Cierra la actividad
                // Esta línea hace la magia de la transición suave:
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
        }
    }

    private void ponerFraseAleatoria() {
        Random random = new Random();
        int index = random.nextInt(FRASES.length);
        tvFraseMotivadora.setText(FRASES[index]);
    }

    private void mostrarDialogoSeleccion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar Foto de Perfil");
        builder.setMessage("¿Quieres subir tu propia foto o elegir un personaje?");

        builder.setPositiveButton("Mi Galería", (dialog, which) -> {
            abrirGaleria();
        });

        builder.setNegativeButton("Personajes", (dialog, which) -> {
            mostrarMenuAvatares();
        });

        builder.show();
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private void configurarGalleryLauncher() {
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();

                        // Permisos persistentes
                        try {
                            final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION |
                                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
                            getContentResolver().takePersistableUriPermission(imageUri, takeFlags);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        ivPerfil.setImageURI(imageUri);

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("tipo_foto", "uri");
                        editor.putString("uri_foto", imageUri.toString());
                        editor.apply();
                    }
                }
        );
    }

    private void mostrarMenuAvatares() {
        BottomSheetDialog sheetDialog = new BottomSheetDialog(this);

        LinearLayout layoutPrincipal = new LinearLayout(this);
        layoutPrincipal.setOrientation(LinearLayout.VERTICAL);
        layoutPrincipal.setPadding(40, 40, 40, 40);
        layoutPrincipal.setBackgroundColor(Color.WHITE);

        TextView titulo = new TextView(this);
        titulo.setText("Elige tu Personaje");
        titulo.setTextSize(20);
        titulo.setPadding(0, 0, 0, 30);
        titulo.setGravity(Gravity.CENTER);
        layoutPrincipal.addView(titulo);

        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(2);
        grid.setAlignmentMode(GridLayout.ALIGN_BOUNDS);

        for (int avatarId : AVATARES_PREDEFINIDOS) {
            ImageView iv = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(250, 250);
            params.setMargins(20, 20, 20, 20);
            iv.setLayoutParams(params);

            iv.setImageResource(avatarId);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

            iv.setOnClickListener(v -> {
                ivPerfil.setImageResource(avatarId);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("tipo_foto", "resource");
                editor.putInt("res_id_foto", avatarId);
                editor.apply();

                sheetDialog.dismiss();
                Toast.makeText(this, "¡Avatar actualizado!", Toast.LENGTH_SHORT).show();
            });

            CardView card = new CardView(this);
            card.setRadius(100);
            card.addView(iv);

            grid.addView(card);
        }

        layoutPrincipal.addView(grid);
        layoutPrincipal.setGravity(Gravity.CENTER_HORIZONTAL);

        sheetDialog.setContentView(layoutPrincipal);
        sheetDialog.show();
    }

    private void cargarDatosGuardados() {
        String tipo = prefs.getString("tipo_foto", "ninguna");

        if (tipo.equals("uri")) {
            String uriString = prefs.getString("uri_foto", null);
            if (uriString != null) {
                try {
                    ivPerfil.setImageURI(Uri.parse(uriString));
                } catch (Exception e) {
                    ivPerfil.setImageResource(R.drawable.img_prueba); // Tu imagen default
                    prefs.edit().putString("tipo_foto", "ninguna").apply();
                }
            }
        } else if (tipo.equals("resource")) {
            int resId = prefs.getInt("res_id_foto", R.drawable.img_prueba);
            ivPerfil.setImageResource(resId);
        }
    }
}