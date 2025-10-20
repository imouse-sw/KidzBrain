package com.kidzbrain.utilidades.leccionutil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class PlantillaFragmentoTeoria extends Fragment implements IPasoLeccion {
    private int miLayoutId;

    public PlantillaFragmentoTeoria() {

    }

    public static PlantillaFragmentoTeoria getInstance(int layoutId) {
        PlantillaFragmentoTeoria fragmento = new PlantillaFragmentoTeoria();
        Bundle args = new Bundle();
        args.putInt("layout_id", layoutId);
        fragmento.setArguments(args);
        return fragmento;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            miLayoutId = getArguments().getInt("layout_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(miLayoutId, container, false);
    }

    @Override
    public boolean esInteractivo() {
        return false;
    }

    @Override
    public void setOyentePasoCompletado(OyentePasoCompletado oyente) {

    }
}
