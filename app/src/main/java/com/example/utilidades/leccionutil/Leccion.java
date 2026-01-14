package com.example.utilidades.leccionutil;

public class Leccion {
    private String titulo;
    private String subtitulo;
    private int imagenResId;
    private int nivel;
    private Class<?> actividad;

    public Leccion(String titulo, String subtitulo, int imagenResId, int nivel, Class<?> actividad) {
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.imagenResId = imagenResId;
        this.nivel = nivel;
        this.actividad = actividad;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public int getImagenResId() {
        return imagenResId;
    }

    public int getNivel() {
        return nivel;
    }

    public Class<?> getActividad() {
        return actividad;
    }
}