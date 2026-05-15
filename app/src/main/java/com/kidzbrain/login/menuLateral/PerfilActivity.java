package com.kidzbrain.login.menuLateral;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kidzbrain.login.MainActivity;
import com.kidzbrain.login.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kidzbrain.spring.ApiService;
import com.kidzbrain.spring.RetrofitClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.kidzbrain.spring.dto.BorrarCuentaDto;

public class PerfilActivity extends AppCompatActivity {

    // Vistas
    private ImageView ivPerfil;
    private View btnCambiarFoto;
    private TextView tvFraseMotivadora;
    private TextView tvNombre;
    private TextView tvCorreo;
    private int usuarioId = -1;

    // Para guardar datos
    private SharedPreferences prefs;
    private ApiService apiService;
    private FrameLayout layoutBorrarOverlay;
    private ImageView btnCloseBorrar;
    private EditText etPasswordBorrar;
    private Button btnConfirmarBorrar;
    private TextView btnCancelarBorrar;
    // Asumo que tienes un botón en tu perfil para abrir esto:
    private Button btnBorrarCuenta;

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
        tvCorreo = findViewById(R.id.email_usuario);
        btnBorrarCuenta = findViewById(R.id.btnBorrarCuenta);

        prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);

        // Configurar lanzadores
        configurarGalleryLauncher();

        // Cargar datos
        cargarDatosGuardados();

        // Poner frase
        ponerFraseAleatoria();

        // Listener para cambiar foto
        btnCambiarFoto.setOnClickListener(v -> mostrarDialogoSeleccion());
        layoutBorrarOverlay = findViewById(R.id.layoutBorrarOverlay);
        btnCloseBorrar = findViewById(R.id.btnCloseBorrar);
        etPasswordBorrar = findViewById(R.id.etPasswordBorrar);
        btnConfirmarBorrar = findViewById(R.id.btnConfirmarBorrar);
        btnCancelarBorrar = findViewById(R.id.btnCancelarBorrar);
        btnBorrarCuenta = findViewById(R.id.btnBorrarCuenta); // Ajusta el ID al de tu botón principal

        btnBorrarCuenta.setOnClickListener(v -> mostrarOverlayBorrar());

        btnCloseBorrar.setOnClickListener(v -> ocultarOverlayBorrar());
        btnCancelarBorrar.setOnClickListener(v -> ocultarOverlayBorrar());

        btnConfirmarBorrar.setOnClickListener(v -> {
            String password = etPasswordBorrar.getText().toString().trim();
            if (password.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa tu contraseña", Toast.LENGTH_SHORT).show();
            } else {
                ejecutarBorrado(password);
            }
        });

        apiService = RetrofitClient.getApiService(this);

        // --- LÓGICA DEL BOTÓN MENÚ / VOLVER ---
        View btnMenu = findViewById(R.id.btnMenu);
        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
        }
    }

    private void ocultarOverlayBorrar() {
        if (layoutBorrarOverlay != null) {
            layoutBorrarOverlay.animate().alpha(0f).setDuration(300).withEndAction(() -> {
                layoutBorrarOverlay.setVisibility(View.GONE);
            }).start();
        }
    }

    private void mostrarOverlayBorrar() {
        if (layoutBorrarOverlay != null) {
            etPasswordBorrar.setText("");
            layoutBorrarOverlay.setVisibility(View.VISIBLE);
            layoutBorrarOverlay.setAlpha(0f);
            layoutBorrarOverlay.animate().alpha(1f).setDuration(300).start();
        }
    }

    private void ejecutarBorrado(String password) {
        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String correo = prefs.getString("userEmail", "");

        BorrarCuentaDto dto = new BorrarCuentaDto(correo, password);

        apiService.borrarCuenta(dto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.apply();

                    Toast.makeText(PerfilActivity.this, "Cuenta eliminada con éxito", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(PerfilActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else if (response.code() == 401) {
                    Toast.makeText(PerfilActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(PerfilActivity.this, "Error al eliminar la cuenta", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PerfilActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

        builder.setPositiveButton("Mi Galería", (dialog, which) -> abrirGaleria());
        builder.setNegativeButton("Personajes", (dialog, which) -> mostrarMenuAvatares());
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

                        // 1. Mostrar visualmente
                        ivPerfil.setImageURI(imageUri);

                        // 2. Guardar la referencia (Solo URI string)
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("tipo_foto", "uri");
                        editor.putString("uri_foto", imageUri.toString());
                        editor.apply();

                        // 3. Intentar subir
                        // ELIMINAMOS takePersistableUriPermission PORQUE SUELE FALLAR
                        subirImagenAlServidor(imageUri);
                    }
                }
        );
    }

    private void subirImagenAlServidor(Uri uri) {
        if (usuarioId == -1) {
            Toast.makeText(this, "Error: Usuario no identificado", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // 1. Convertir Uri a File (usando tu método existente)
            File file = uriToFile(uri);

            // 2. Crear RequestBody (tipo de archivo)
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

            // 3. Crear la parte Multipart (el nombre "foto" debe coincidir con @RequestParam("foto") en Spring)
            MultipartBody.Part body = MultipartBody.Part.createFormData("foto", file.getName(), requestFile);

            // 4. Llamar a Retrofit
            Call<String> call = RetrofitClient.getApiService(this).subirFotoPerfil(usuarioId, body);

            // Mostrar indicador de carga si quieres...
            Toast.makeText(this, "Subiendo foto...", Toast.LENGTH_SHORT).show();

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        String urlRelativa = response.body(); // Esto es "/uploads/..."
                        Toast.makeText(PerfilActivity.this, "¡Foto subida con éxito!", Toast.LENGTH_SHORT).show();

                        // --- NUEVO: GUARDAR LA URL DEL SERVIDOR ---
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("tipo_foto", "server"); // Marcamos que viene del servidor
                        editor.putString("url_foto_server", urlRelativa); // Guardamos la ruta
                        editor.apply();

                        // Forzar carga inmediata con Glide para que se vea el cambio
                        cargarFotoConGlide(urlRelativa);

                    } else {
                        Toast.makeText(PerfilActivity.this, "Error en servidor: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(PerfilActivity.this, "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error preparando archivo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
        // 1. Cargar textos
        String nombre = prefs.getString("userName", "Usuario");
        String correo = prefs.getString("userEmail", "correo@ejemplo.com");
        tvNombre.setText(nombre);
        tvCorreo.setText(correo);

        // 2. Cargar ID
        usuarioId = prefs.getInt("userId", -1);

        // 3. Cargar foto de perfil
        String tipo = prefs.getString("tipo_foto", "ninguna");

        if (tipo.equals("server")) {
            // --- NUEVO: Si la foto viene del servidor, usamos Glide ---
            String urlServer = prefs.getString("url_foto_server", "");
            cargarFotoConGlide(urlServer);

        } else if (tipo.equals("resource")) {
            // Si es un avatar predefinido (R.drawable...)
            int resId = prefs.getInt("res_id_foto", R.drawable.img_prueba);
            ivPerfil.setImageResource(resId);

        } else if (tipo.equals("uri")) {
            // (Opcional) Dejamos esto por si acaso quedó alguna vieja guardada así,
            // pero las nuevas entrarán en "server".
            String uriString = prefs.getString("uri_foto", null);
            if (uriString != null) {
                try {
                    ivPerfil.setImageURI(Uri.parse(uriString));
                } catch (Exception e) {
                    ivPerfil.setImageResource(R.drawable.img_prueba);
                }
            }
        } else {
            // Default
            ivPerfil.setImageResource(R.drawable.img_prueba);
        }
    }

    private File uriToFile(Uri uri) throws Exception {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        File file = new File(getCacheDir(), "perfil_temp.jpg");

        OutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int read;

        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }

        inputStream.close();
        outputStream.close();

        return file;
    }

    private void cargarFotoConGlide(String urlRelativa) {
        if (urlRelativa == null || urlRelativa.isEmpty()) return;

        // 1. Verificar IP (IMPORTANTE: Checa que esta sea tu IP actual)
        String urlCompleta = "http://192.168.1.76:8080" + urlRelativa;

        // Imprimir qué estamos intentando cargar
        Log.e("GLIDE_DEBUG", "Intentando cargar: " + urlCompleta);

        Glide.with(this)
                .load(urlCompleta)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.img_prueba)
                .error(R.drawable.img_prueba) // Imagen si falla
                .listener(new RequestListener<Drawable>() { // <--- ESTO ES LO NUEVO
                    @Override
                    public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // AQUÍ VEREMOS EL ERROR REAL
                        Log.e("GLIDE_ERROR", "Falló la carga: " + e.getMessage());
                        if (e != null) e.logRootCauses("GLIDE_ERROR");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("GLIDE_SUCCESS", "¡Imagen cargada exitosamente!");
                        return false;
                    }
                })
                .into(ivPerfil);
    }

}
