package com.kidzbrain.utilidades.leccionutil;

import androidx.fragment.app.Fragment;

public abstract class PlantillaFragmentoInteractivo extends Fragment implements IPasoLeccion {
    protected OyentePasoCompletado oyente;

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

    public abstract void comprobarRespuesta();
}
