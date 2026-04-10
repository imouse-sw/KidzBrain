package com.kidzbrain.juegos.comida;

import java.util.List;

public class Alimento {

    private final String nombre;
    private final int imagenResId;
    private final List<String> etiquetas;

    public Alimento(String nombre, int imagenResId, List<String> etiquetas) {
        this.nombre = nombre;
        this.imagenResId = imagenResId;
        this.etiquetas = etiquetas;
    }

    public String getNombre() {
        return nombre;
    }

    public int getImagenResId() {
        return imagenResId;
    }

    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public boolean tieneEtiqueta(String etiqueta) {
        return etiquetas.contains(etiqueta);
    }
}