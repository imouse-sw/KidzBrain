package com.kidzbrain.utilidades.chatbot;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.ai.client.generativeai.type.Content;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kidzbrain.login.R;

import android.widget.EditText;
import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;


import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import com.google.ai.client.generativeai.type.GenerateContentResponse;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;


public class ChatBotBottomSheet extends BottomSheetDialogFragment {

    private RecyclerView recyclerChat;
    private EditText etMensaje;
    private ImageButton btnEnviar;

    private List<ChatMessage> listaMensajes;
    private ChatAdapter adapter;

    private GenerativeModelFutures model;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chatbot, container, false);

        recyclerChat = view.findViewById(R.id.recyclerChat);
        etMensaje = view.findViewById(R.id.etMensaje);
        btnEnviar = view.findViewById(R.id.btnEnviar);

        GenerativeModel gm = new GenerativeModel(
                "gemini-flash-lite-latest",
                ApiKey.GEMINI_API_KEY
        );

        model = GenerativeModelFutures.from(gm);

        listaMensajes = new ArrayList<>();

        adapter = new ChatAdapter(listaMensajes);

        recyclerChat.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerChat.setAdapter(adapter);

        mensajeInicial();

        btnEnviar.setOnClickListener(v -> enviarMensaje());

        return view;
    }

    private void mensajeInicial() {

        listaMensajes.add(
                new ChatMessage(
                        "¡Hola! Soy BrainBot AI. ¿Qué quieres aprender hoy?",
                        false
                )
        );

        adapter.notifyDataSetChanged();
    }

    private void enviarMensaje() {

        String texto = etMensaje.getText().toString().trim();

        if(texto.isEmpty()) return;

        listaMensajes.add(
                new ChatMessage(texto, true)
        );

        adapter.notifyItemInserted(listaMensajes.size() - 1);

        recyclerChat.smoothScrollToPosition(listaMensajes.size() - 1);

        etMensaje.setText("");

        responderGemini(texto);
    }

    private void responderFake() {

        listaMensajes.add(
                new ChatMessage(
                        "Estoy aprendiendo todavía 😎",
                        false
                )
        );

        adapter.notifyItemInserted(listaMensajes.size() - 1);

        recyclerChat.smoothScrollToPosition(listaMensajes.size() - 1);
    }

    private void responderGemini(String preguntaUsuario) {

        String prompt =
                "Eres BrainBot AI, un asistente educativo súper divertido diseñado exclusivamente para niños de primaria (6 a 11 años). " +
                        "TU ÚNICA MISIÓN es enseñar y resolver dudas de MATEMÁTICAS y CIENCIAS. " +
                        "Responde de forma muy fácil, amigable y corta. Usa emojis. No uses palabras difíciles. " +
                        "REGLA ESTRICTA: Si el usuario te pregunta sobre cualquier otro tema que NO sea matemáticas o ciencias (por ejemplo: memes, videojuegos, música, películas, chismes, etc.), " +
                        "TIENES PROHIBIDO responder a eso. En su lugar, debes decirle amablemente que solo eres experto en números y ciencia, y desviar la plática ofreciéndole un dato curioso de esas materias.";

        String entradaFinal = prompt + "\n\nUsuario: " + preguntaUsuario;

        Content content = new Content.Builder()
                .addText(entradaFinal)
                .build();

        // 1. Creamos una burbuja vacía que se va a ir llenando de texto
        final int indexBurbuja = listaMensajes.size();
        listaMensajes.add(new ChatMessage("", false));
        adapter.notifyItemInserted(indexBurbuja);
        recyclerChat.smoothScrollToPosition(indexBurbuja);

        // 2. Pedimos el STREAM en lugar de la respuesta completa (SIN el Flow.)
        Publisher<GenerateContentResponse> streamingResponse = model.generateContentStream(content);

        // 3. Nos "suscribimos" para cachar cada palabra que va llegando (SIN el Flow.)
        streamingResponse.subscribe(new Subscriber<GenerateContentResponse>() {

            // SIN el Flow.
            private Subscription subscription;
            private StringBuilder respuestaCompleta = new StringBuilder();

            @Override
            // SIN el Flow.
            public void onSubscribe(Subscription s) {
                this.subscription = s;
                s.request(Long.MAX_VALUE); // Le decimos que nos mande todos los pedacitos
            }

            @Override
            public void onNext(GenerateContentResponse chunk) {
                String pedacito = chunk.getText();

                if (pedacito != null) {
                    respuestaCompleta.append(pedacito);

                    // Actualizamos la burbuja en la pantalla al instante
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            listaMensajes.get(indexBurbuja).setTexto(respuestaCompleta.toString());
                            adapter.notifyItemChanged(indexBurbuja);
                            recyclerChat.smoothScrollToPosition(indexBurbuja);
                        });
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                Log.e("BrainBot", "Error en el stream: ", t);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        listaMensajes.get(indexBurbuja).setTexto("Ups, mi cerebro digital tropezó. ¿Me lo repites? 😅");
                        adapter.notifyItemChanged(indexBurbuja);
                    });
                }
            }

            @Override
            public void onComplete() {
                // Ya no hace falta hacer nada visual aquí, la burbuja ya está llena
                Log.d("BrainBot", "¡Respuesta terminada con éxito!");
            }
        });
    }
}
