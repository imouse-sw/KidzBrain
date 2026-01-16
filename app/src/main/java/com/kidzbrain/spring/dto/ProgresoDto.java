package com.kidzbrain.spring.dto; // O el paquete que uses en Android

import com.google.gson.annotations.SerializedName;

public class ProgresoDto {

    @SerializedName("progresoId")
    private Integer progresoId;

    @SerializedName("usuarioId")
    private Integer usuarioId;

    // --- CAMBIO IMPORTANTE ---
    // Antes era "juegoId", ahora debe coincidir con el backend
    @SerializedName("leccionId")
    private Integer leccionId;

    @SerializedName("completado")
    private Integer completado;

    @SerializedName("puntuacion")
    private Integer puntuacion;

    // Constructor, Getters y Setters...
    public ProgresoDto(Integer usuarioId, Integer leccionId, Integer completado, Integer puntuacion) {
        this.usuarioId = usuarioId;
        this.leccionId = leccionId;
        this.completado = completado;
        this.puntuacion = puntuacion;
    }

    public Integer getProgresoId() {
        return progresoId;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public Integer getLeccionId() {
        return leccionId;
    }

    public Integer getCompletado() {
        return completado;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }
}