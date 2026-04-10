package com.kidzbrain.juegos.comida;

import java.util.List;

public class ClientePedido {

    private final String textoPedido;
    private final int imagenClienteResId;
    private final List<String> etiquetasObligatorias;
    private final List<String> etiquetasProhibidas;
    private final int cantidadMinima;
    private final int cantidadMaxima;

    public ClientePedido(String textoPedido, int imagenClienteResId,
                         List<String> etiquetasObligatorias,
                         List<String> etiquetasProhibidas,
                         int cantidadMinima,
                         int cantidadMaxima) {
        this.textoPedido = textoPedido;
        this.imagenClienteResId = imagenClienteResId;
        this.etiquetasObligatorias = etiquetasObligatorias;
        this.etiquetasProhibidas = etiquetasProhibidas;
        this.cantidadMinima = cantidadMinima;
        this.cantidadMaxima = cantidadMaxima;
    }

    public String getTextoPedido() {
        return textoPedido;
    }

    public int getImagenClienteResId() {
        return imagenClienteResId;
    }

    public List<String> getEtiquetasObligatorias() {
        return etiquetasObligatorias;
    }

    public List<String> getEtiquetasProhibidas() {
        return etiquetasProhibidas;
    }

    public int getCantidadMinima() {
        return cantidadMinima;
    }

    public int getCantidadMaxima() {
        return cantidadMaxima;
    }
}