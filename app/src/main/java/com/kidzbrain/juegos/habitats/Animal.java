package com.kidzbrain.juegos.habitats;

public class Animal {

    private final String nombre;
    private final String habitatCorrecto;
    private final String comidaCorrecta;
    private final String[] opcionesHabitat;
    private final String[] opcionesComida;
    private final int imagenResId;

    public Animal(String nombre, String habitatCorrecto, String comidaCorrecta,
                  String[] opcionesHabitat, String[] opcionesComida, int imagenResId) {
        this.nombre = nombre;
        this.habitatCorrecto = habitatCorrecto;
        this.comidaCorrecta = comidaCorrecta;
        this.opcionesHabitat = opcionesHabitat;
        this.opcionesComida = opcionesComida;
        this.imagenResId = imagenResId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getHabitatCorrecto() {
        return habitatCorrecto;
    }

    public String getComidaCorrecta() {
        return comidaCorrecta;
    }

    public String[] getOpcionesHabitat() {
        return opcionesHabitat;
    }

    public String[] getOpcionesComida() {
        return opcionesComida;
    }

    public int getImagenResId() {
        return imagenResId;
    }
}
