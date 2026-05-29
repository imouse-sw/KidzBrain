package com.kidzbrain.utilidades.chatbot;

public class ChatMessage {

    private String mensaje;
    private boolean esUsuario;

    public ChatMessage(String mensaje, boolean esUsuario) {
        this.mensaje = mensaje;
        this.esUsuario = esUsuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public boolean esUsuario() {
        return esUsuario;
    }

    // Aquí está la corrección: usamos "this.mensaje" en lugar de "this.texto"
    public void setTexto(String nuevoTexto) {
        this.mensaje = nuevoTexto;
    }
}