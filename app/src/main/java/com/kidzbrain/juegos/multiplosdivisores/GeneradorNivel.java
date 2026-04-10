package com.kidzbrain.juegos.multiplosdivisores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeneradorNivel {

    public List<Integer> generarOpciones(int numeroObjetivo, boolean buscarMultiplos) {
        List<Integer> opciones = new ArrayList<>();
        Random random = new Random();

        // 1. Generar 2 respuestas CORRECTAS
        for (int i = 0; i < 2; i++) {
            if (buscarMultiplos) {
                // Múltiplo: multiplicamos el objetivo por un aleatorio (del 2 al 10)
                int multiplicador = random.nextInt(9) + 2;
                opciones.add(numeroObjetivo * multiplicador);
            } else {
                // Divisor: buscamos un número al azar que lo divida exactamente
                int divisor;
                do {
                    divisor = random.nextInt(numeroObjetivo) + 1;
                } while (numeroObjetivo % divisor != 0); // Repite si NO es divisor

                opciones.add(divisor);
            }
        }

        // 2. Generar 3 respuestas INCORRECTAS (Trampas)
        for (int i = 0; i < 3; i++) {
            int trampa = 0;
            boolean esTrampaValida = false;

            while (!esTrampaValida) {
                trampa = random.nextInt(50) + 1; // Número aleatorio del 1 al 50

                if (buscarMultiplos) {
                    // Es trampa válida si el residuo NO es cero
                    if (trampa % numeroObjetivo != 0) esTrampaValida = true;
                } else {
                    // Es trampa válida si el objetivo NO se puede dividir entre este número
                    if (numeroObjetivo % trampa != 0) esTrampaValida = true;
                }
            }
            opciones.add(trampa);
        }

        // 3. Mezclar la lista para que las correctas no salgan siempre primero
        Collections.shuffle(opciones);

        return opciones;
    }
}