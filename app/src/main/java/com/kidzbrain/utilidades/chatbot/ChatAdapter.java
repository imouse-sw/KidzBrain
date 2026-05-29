package com.kidzbrain.utilidades.chatbot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kidzbrain.login.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatMessage> mensajes;

    private static final int USER = 1;
    private static final int BOT = 2;

    public ChatAdapter(List<ChatMessage> mensajes) {
        this.mensajes = mensajes;
    }

    @Override
    public int getItemViewType(int position) {
        return mensajes.get(position).esUsuario() ? USER : BOT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        if(viewType == USER) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_user, parent, false);

            return new UserViewHolder(view);
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message_bot, parent, false);

        return new BotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder,
            int position
    ) {

        ChatMessage mensaje = mensajes.get(position);

        if(holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).mensaje.setText(mensaje.getMensaje());
        }
        else {
            ((BotViewHolder) holder).mensaje.setText(mensaje.getMensaje());
        }
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView mensaje;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            mensaje = itemView.findViewById(R.id.tvMensajeUser);
        }
    }

    static class BotViewHolder extends RecyclerView.ViewHolder {

        TextView mensaje;

        public BotViewHolder(@NonNull View itemView) {
            super(itemView);

            mensaje = itemView.findViewById(R.id.tvMensajeBot);
        }
    }
}