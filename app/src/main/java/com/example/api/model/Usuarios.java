package com.example.api.model;


public class Usuarios {

    private String nombre;
    private String correo;
    private String password;
    private int edadHijo;
    private int id;


    public Usuarios(String nombre, String correo, String password) {
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
    }
    public Usuarios(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEdadHijo() {
        return edadHijo;
    }

    public void setEdadHijo(int edadHijo) {
        this.edadHijo = edadHijo;
    }
}
