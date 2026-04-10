package com.kidzbrain.juegos.habitats;

import com.kidzbrain.login.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RepositorioAnimales {

    private final List<Animal> animales = new ArrayList<>();
    private final List<Animal> animalesDisponibles = new ArrayList<>();

    private Animal ultimoAnimal = null;

    public RepositorioAnimales() {
        cargarAnimales();
        reiniciarCiclo();
    }

    private void cargarAnimales() {
        animales.add(new Animal(
                "León",
                "Sabana",
                "Carne",
                new String[]{"Sabana", "Selva", "Océano"},
                new String[]{"Carne", "Pasto", "Peces"},
                R.drawable.animal_leon
        ));

        animales.add(new Animal(
                "Pingüino",
                "Polo",
                "Peces",
                new String[]{"Polo", "Desierto", "Bosque"},
                new String[]{"Peces", "Frutas", "Semillas"},
                R.drawable.animal_pinguino
        ));

        animales.add(new Animal(
                "Mono",
                "Selva",
                "Frutas",
                new String[]{"Selva", "Polo", "Granja"},
                new String[]{"Frutas", "Carne", "Bambú"},
                R.drawable.animal_mono
        ));

        animales.add(new Animal(
                "Camello",
                "Desierto",
                "Plantas",
                new String[]{"Desierto", "Océano", "Montaña"},
                new String[]{"Plantas", "Peces", "Zanahorias"},
                R.drawable.animal_camello
        ));

        animales.add(new Animal(
                "Tiburón",
                "Océano",
                "Peces",
                new String[]{"Océano", "Río", "Sabana"},
                new String[]{"Peces", "Pasto", "Frutas"},
                R.drawable.animal_tiburon
        ));

        animales.add(new Animal(
                "Conejo",
                "Bosque",
                "Zanahorias",
                new String[]{"Bosque", "Polo", "Desierto"},
                new String[]{"Zanahorias", "Carne", "Peces"},
                R.drawable.animal_conejo
        ));

        animales.add(new Animal(
                "Rana",
                "Río",
                "Insectos",
                new String[]{"Río", "Polo", "Sabana"},
                new String[]{"Insectos", "Bambú", "Carne"},
                R.drawable.animal_rana
        ));

        animales.add(new Animal(
                "Panda",
                "Bosque",
                "Bambú",
                new String[]{"Bosque", "Océano", "Desierto"},
                new String[]{"Bambú", "Carne", "Peces"},
                R.drawable.animal_panda
        ));
    }

    private void reiniciarCiclo() {
        animalesDisponibles.clear();
        animalesDisponibles.addAll(animales);
        Collections.shuffle(animalesDisponibles);

        // Evita que el primer animal del nuevo ciclo sea el mismo
        // que el último del ciclo anterior
        if (ultimoAnimal != null
                && animalesDisponibles.size() > 1
                && animalesDisponibles.get(0).getNombre().equals(ultimoAnimal.getNombre())) {
            Collections.swap(animalesDisponibles, 0, 1);
        }
    }

    public Animal obtenerSiguienteAnimal() {
        if (animalesDisponibles.isEmpty()) {
            reiniciarCiclo();
        }

        Animal siguiente = animalesDisponibles.remove(0);
        ultimoAnimal = siguiente;
        return siguiente;
    }
}