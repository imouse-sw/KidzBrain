package com.example.utilidades.leccionutil;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PlantillaFragmentoTeoria extends Fragment implements IPasoLeccion {
    private int miLayoutId;
    private int miAudioId = 0; // por defecto no hay audio entonces el id es cero
    private MediaPlayer mediaPlayer; // para las voces en off que dan la explicación teórica

    public PlantillaFragmentoTeoria() {
    }

    public static PlantillaFragmentoTeoria getInstance(int layoutId) {
        return getInstance(layoutId, 0);
    }

    public static PlantillaFragmentoTeoria getInstance(int layoutId, int audioId) {
        PlantillaFragmentoTeoria fragmento = new PlantillaFragmentoTeoria();
        Bundle args = new Bundle();
        args.putInt("layout_id", layoutId);
        args.putInt("audio_id", audioId);
        fragmento.setArguments(args);
        return fragmento;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            miLayoutId = getArguments().getInt("layout_id");
            miAudioId = getArguments().getInt("audio_id", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(miLayoutId, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(miAudioId!=0 && getContext()!=null) {
            mediaPlayer = MediaPlayer.create(getContext(), miAudioId);
        }
    }

    @Override
    public boolean esInteractivo() {
        return false;
    }

    @Override
    public void setOyentePasoCompletado(OyentePasoCompletado oyente) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        liberarMediaPlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        liberarMediaPlayer();
    }

    private void liberarMediaPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
