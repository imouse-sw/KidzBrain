package com.kidzbrain.spring.dto;

import com.google.gson.annotations.SerializedName;

public class JuegoResponseDto {
    @SerializedName("juegoId")
    private Integer juegoId;

    @SerializedName("nombreJuego")
    private String nombreJuego;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("tipo")
    private String tipo;

    // Getters
    public Integer getJuegoId() { return juegoId; }
    public String getNombreJuego() { return nombreJuego; }
    public String getDescripcion() { return descripcion; }
    public String getTipo() { return tipo; }
}