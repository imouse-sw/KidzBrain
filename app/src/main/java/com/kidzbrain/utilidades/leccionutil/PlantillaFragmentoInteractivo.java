package com.kidzbrain.utilidades.leccionutil;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;

public abstract class PlantillaFragmentoInteractivo extends Fragment implements IPasoLeccion {
    protected OyentePasoCompletado oyente;
    protected SoundPool soundPool;
    protected HashMap<String, Integer> mapaSonidos = new HashMap<>();

    @Override
    public boolean esInteractivo() {
        return true;
    }

    @Override
    public void setOyentePasoCompletado(OyentePasoCompletado oyente) {
        this.oyente = oyente;
    }

    protected void notificarPasoCompletado() {
        if (oyente != null) {
            oyente.onPasoCompletado();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configurarSoundPool();
        cargarSonidosEspecificos(requireContext(), soundPool);
    }

    protected abstract void comprobarRespuesta();

    protected abstract void cargarSonidosEspecificos(Context context, SoundPool soundPool);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Liberamos el SoundPool cuando la vista se destruye
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
        mapaSonidos.clear(); // Limpiamos el mapa
    }

    // --- Configuración del SoundPool (similar a la Actividad) ---
    private void configurarSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(3) // 3 streams suele ser suficiente para un fragmento
                .setAudioAttributes(attributes)
                .build();
    }

    // métodopúblico para que los fragmentos puedan reproducir sonidos
    /**
     * reproduce un sonido que haya sido cargado en la pool
     * @param claveSonido es la clave que se le asigna al sonido
     */
    public void reproducirSonido(String claveSonido) {
        if(soundPool!=null && mapaSonidos.containsKey(claveSonido)) {
            int sonidoId = mapaSonidos.get(claveSonido);
            if(sonidoId!=0) {
                soundPool.play(sonidoId, 1.0f, 1.0f, 1, 0, 1.0f);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
        mapaSonidos.clear();
    }
}
