package com.kidzbrain.spring.dto;

import com.google.gson.annotations.SerializedName;

// Este es un POJO (Plain Old Java Object) que Retrofit usa para parsear el JSON de tu API.
public class UsuarioDto {

    @SerializedName("usuarioId")
    private Integer usuarioId;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("correo")
    private String correo;

    @SerializedName("password")
    private String password;

    @SerializedName("edadHijo")
    private Integer edadHijo;

    @SerializedName("fotoUrl")
    private String fotoUrl;

    @SerializedName("token")
    private String token;

    // Constructor para el registro
    public UsuarioDto(String nombre, String correo, String password, Integer edadHijo) {
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.edadHijo = edadHijo;
    }

    // Getters para acceder a los datos
    public Integer getUsuarioId() {
        return usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getPassword() {
        return password;
    }

    public String getFotoUrl() {return fotoUrl; }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
