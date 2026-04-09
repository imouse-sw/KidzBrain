package com.kidzbrain.juegos.comida;

import com.kidzbrain.login.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RepositorioPedidosSaludables {

    private final List<Alimento> alimentos = new ArrayList<>();
    private final List<ClientePedido> pedidos = new ArrayList<>();

    private final List<ClientePedido> pedidosDisponibles = new ArrayList<>();
    private ClientePedido ultimoPedido = null;

    public RepositorioPedidosSaludables() {
        cargarAlimentos();
        cargarPedidos();
        reiniciarPedidos();
    }

    private void cargarAlimentos() {
        alimentos.add(new Alimento("Lechuga", R.drawable.alim_lechuga,
                Arrays.asList("verde", "verdura", "saludable")));
        alimentos.add(new Alimento("Agua", R.drawable.alim_agua,
                Arrays.asList("bebida_saludable", "saludable")));
        alimentos.add(new Alimento("Pescado", R.drawable.alim_pescado,
                Arrays.asList("proteina", "mar", "saludable")));
        alimentos.add(new Alimento("Manzana", R.drawable.alim_manzana,
                Arrays.asList("fruta", "saludable")));
        alimentos.add(new Alimento("Plátano", R.drawable.alim_platano,
                Arrays.asList("fruta", "energia", "saludable")));
        alimentos.add(new Alimento("Zanahoria", R.drawable.alim_zanahoria,
                Arrays.asList("verdura", "saludable")));
        alimentos.add(new Alimento("Brócoli", R.drawable.alim_brocoli,
                Arrays.asList("verde", "verdura", "saludable")));
        alimentos.add(new Alimento("Yogur", R.drawable.alim_yogur,
                Arrays.asList("desayuno", "saludable")));
        alimentos.add(new Alimento("Avena", R.drawable.alim_avena,
                Arrays.asList("desayuno", "energia", "saludable")));
        alimentos.add(new Alimento("Pollo", R.drawable.alim_pollo,
                Arrays.asList("proteina", "saludable")));
        alimentos.add(new Alimento("Atún", R.drawable.alim_atun,
                Arrays.asList("proteina", "mar", "saludable")));
        alimentos.add(new Alimento("Pan integral", R.drawable.alim_pan_integral,
                Arrays.asList("energia", "saludable")));
        alimentos.add(new Alimento("Refresco", R.drawable.alim_refresco,
                Arrays.asList("chatarra")));
        alimentos.add(new Alimento("Papas fritas", R.drawable.alim_papas,
                Arrays.asList("chatarra")));
        alimentos.add(new Alimento("Dona", R.drawable.alim_dona,
                Arrays.asList("chatarra")));
        alimentos.add(new Alimento("Galletas", R.drawable.alim_galletas,
                Arrays.asList("chatarra")));
    }

    private void cargarPedidos() {
        pedidos.add(new ClientePedido(
                "Quiero exactamente 2 cosas: una fruta y una bebida saludable.",
                R.drawable.cliente_1,
                Arrays.asList("fruta", "bebida_saludable"),
                Arrays.asList("chatarra"),
                2,
                2
        ));

        pedidos.add(new ClientePedido(
                "Quiero exactamente 2 cosas: una proteína y una verdura.",
                R.drawable.cliente_2,
                Arrays.asList("proteina", "verdura"),
                Arrays.asList("chatarra"),
                2,
                2
        ));

        pedidos.add(new ClientePedido(
                "Quiero exactamente 2 cosas: algo verde y una bebida saludable.",
                R.drawable.cliente_3,
                Arrays.asList("verde", "bebida_saludable"),
                Arrays.asList("chatarra"),
                2,
                2
        ));

        pedidos.add(new ClientePedido(
                "Quiero exactamente 2 cosas: algo del mar y una fruta.",
                R.drawable.cliente_4,
                Arrays.asList("mar", "fruta"),
                Arrays.asList("chatarra"),
                2,
                2
        ));

        pedidos.add(new ClientePedido(
                "Quiero exactamente 2 cosas: una fruta y una verdura.",
                R.drawable.cliente_1,
                Arrays.asList("fruta", "verdura"),
                Arrays.asList("chatarra"),
                2,
                2
        ));

        pedidos.add(new ClientePedido(
                "Quiero exactamente 2 cosas: una proteína y una bebida saludable.",
                R.drawable.cliente_2,
                Arrays.asList("proteina", "bebida_saludable"),
                Arrays.asList("chatarra"),
                2,
                2
        ));

        pedidos.add(new ClientePedido(
                "Quiero exactamente 2 cosas: algo verde y una proteína.",
                R.drawable.cliente_3,
                Arrays.asList("verde", "proteina"),
                Arrays.asList("chatarra"),
                2,
                2
        ));

        pedidos.add(new ClientePedido(
                "Quiero exactamente 2 cosas: algo del mar y una bebida saludable.",
                R.drawable.cliente_4,
                Arrays.asList("mar", "bebida_saludable"),
                Arrays.asList("chatarra"),
                2,
                2
        ));
        pedidos.add(new ClientePedido(
                "Quiero 1 cosa: una fruta.",
                R.drawable.cliente_1,
                Arrays.asList("fruta"),
                Arrays.asList("chatarra"),
                1,
                1
        ));

        pedidos.add(new ClientePedido(
                "Quiero 1 cosa: una bebida saludable.",
                R.drawable.cliente_2,
                Arrays.asList("bebida_saludable"),
                Arrays.asList("chatarra"),
                1,
                1
        ));

        pedidos.add(new ClientePedido(
                "Quiero 1 cosa: algo del mar.",
                R.drawable.cliente_3,
                Arrays.asList("mar"),
                Arrays.asList("chatarra"),
                1,
                1
        ));

        pedidos.add(new ClientePedido(
                "Quiero 1 cosa: una proteína.",
                R.drawable.cliente_4,
                Arrays.asList("proteina"),
                Arrays.asList("chatarra"),
                1,
                1
        ));

        pedidos.add(new ClientePedido(
                "Quiero 1 cosa: una verdura verde.",
                R.drawable.cliente_1,
                Arrays.asList("verde", "verdura"),
                Arrays.asList("chatarra"),
                1,
                1
        ));
        pedidos.add(new ClientePedido(
                "Quiero 3 cosas: una fruta, una verdura y una bebida saludable.",
                R.drawable.cliente_2,
                Arrays.asList("fruta", "verdura", "bebida_saludable"),
                Arrays.asList("chatarra"),
                3,
                3
        ));

        pedidos.add(new ClientePedido(
                "Quiero 3 cosas: una proteína, algo verde y una bebida saludable.",
                R.drawable.cliente_3,
                Arrays.asList("proteina", "verde", "bebida_saludable"),
                Arrays.asList("chatarra"),
                3,
                3
        ));

        pedidos.add(new ClientePedido(
                "Quiero 3 cosas: algo del mar, una fruta y una bebida saludable.",
                R.drawable.cliente_4,
                Arrays.asList("mar", "fruta", "bebida_saludable"),
                Arrays.asList("chatarra"),
                3,
                3
        ));

        pedidos.add(new ClientePedido(
                "Quiero 3 cosas: una fruta, una proteína y una verdura.",
                R.drawable.cliente_1,
                Arrays.asList("fruta", "proteina", "verdura"),
                Arrays.asList("chatarra"),
                3,
                3
        ));

        pedidos.add(new ClientePedido(
                "Quiero 3 cosas: algo verde, una fruta y una proteína.",
                R.drawable.cliente_2,
                Arrays.asList("verde", "fruta", "proteina"),
                Arrays.asList("chatarra"),
                3,
                3
        ));
    }

    private void reiniciarPedidos() {
        pedidosDisponibles.clear();
        pedidosDisponibles.addAll(pedidos);
        Collections.shuffle(pedidosDisponibles);

        if (ultimoPedido != null
                && pedidosDisponibles.size() > 1
                && pedidosDisponibles.get(0).getTextoPedido().equals(ultimoPedido.getTextoPedido())) {
            Collections.swap(pedidosDisponibles, 0, 1);
        }
    }

    public ClientePedido obtenerSiguientePedido() {
        if (pedidosDisponibles.isEmpty()) {
            reiniciarPedidos();
        }

        ClientePedido pedido = pedidosDisponibles.remove(0);
        ultimoPedido = pedido;
        return pedido;
    }

    public List<Alimento> obtenerOpcionesParaPedido(ClientePedido pedido) {
        List<Alimento> opciones = new ArrayList<>();
        List<Alimento> disponibles = new ArrayList<>(alimentos);

        for (String etiqueta : pedido.getEtiquetasObligatorias()) {
            Alimento candidato = buscarAlimentoPorEtiqueta(etiqueta, opciones, pedido.getEtiquetasProhibidas());
            if (candidato != null) {
                opciones.add(candidato);
                disponibles.remove(candidato);
            }
        }

        Collections.shuffle(disponibles);

        for (Alimento alimento : disponibles) {
            if (opciones.size() >= 8) {
                break;
            }
            if (!opciones.contains(alimento)) {
                opciones.add(alimento);
            }
        }

        Collections.shuffle(opciones);

        return opciones;
    }

    private Alimento buscarAlimentoPorEtiqueta(String etiqueta, List<Alimento> yaElegidos, List<String> prohibidas) {
        List<Alimento> candidatos = new ArrayList<>();

        for (Alimento alimento : alimentos) {
            if (alimento.tieneEtiqueta(etiqueta) && !yaElegidos.contains(alimento)) {
                boolean prohibido = false;
                for (String prohibida : prohibidas) {
                    if (alimento.tieneEtiqueta(prohibida)) {
                        prohibido = true;
                        break;
                    }
                }
                if (!prohibido) {
                    candidatos.add(alimento);
                }
            }
        }

        if (candidatos.isEmpty()) {
            return null;
        }

        Collections.shuffle(candidatos);
        return candidatos.get(0);
    }
}