package com.kidzbrain.spring.dto;

import com.google.gson.annotations.SerializedName;

public class LeccionResponseDto {
    @SerializedName("leccionId")
    private Integer leccionId;

    @SerializedName("titulo")
    private String titulo;      // <--- AQUÍ LLEGA TU TEXTO DE LA BD

    @SerializedName("descripcion")
    private String descripcion; // <--- AQUÍ LLEGA TU DESCRIPCIÓN

    @SerializedName("orden")
    private Integer orden;      // <--- ESTO USAMOS PARA SABER QUÉ ACTIVITY ABRIR

    // Getters

    public Integer getLeccionId() {
        return leccionId;
    }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public Integer getOrden() { return orden; }
}