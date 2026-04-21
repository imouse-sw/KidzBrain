package com.kidzbrain.spring.dto;

public class SolicitudRecuperacionDto {
    private String correo;

    public SolicitudRecuperacionDto(String correo) {
        this.correo = correo;
    }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}