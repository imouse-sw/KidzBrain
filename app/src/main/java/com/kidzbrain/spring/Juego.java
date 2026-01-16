package com.kidzbrain.spring;

public class Juego {
    private int id;
    private String nombre;
    private String descripcion;
    private int imagenResId; // R.drawable...
    private Class<?> claseActividad; // La pantalla a abrir (MenuCambio.class)

    public Juego(int id, String nombre, String descripcion, int imagenResId, Class<?> claseActividad) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenResId = imagenResId;
        this.claseActividad = claseActividad;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getImagenResId() { return imagenResId; }
    public Class<?> getClaseActividad() { return claseActividad; }
}
