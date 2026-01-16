package com.kidzbrain.juegos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kidzbrain.login.R;
import com.kidzbrain.spring.Juego;

import java.util.List;

public class JuegoAdapter extends RecyclerView.Adapter<JuegoAdapter.JuegoViewHolder> {

    private Context context;
    private List<Juego> listaJuegos;

    public JuegoAdapter(Context context, List<Juego> listaJuegos) {
        this.context = context;
        this.listaJuegos = listaJuegos;
    }

    @NonNull
    @Override
    public JuegoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Usamos tu layout item_juego.xml existente
        View view = LayoutInflater.from(context).inflate(R.layout.item_juego, parent, false);
        // TRUCO: Ajustamos el alto para que ocupe todo el ViewPager
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return new JuegoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JuegoViewHolder holder, int position) {
        Juego juego = listaJuegos.get(position);

        holder.tvNombre.setText(juego.getNombre());
        holder.tvDescripcion.setText(juego.getDescripcion());
        holder.ivIcono.setImageResource(juego.getImagenResId());

        holder.itemView.setOnClickListener(v -> {
            if (juego.getClaseActividad() != null) {
                Intent intent = new Intent(context, juego.getClaseActividad());
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Próximamente...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaJuegos.size();
    }

    public static class JuegoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre;
        TextView tvDescripcion;
        ImageView ivIcono;

        public JuegoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tv_juego_nombre);
            tvDescripcion = itemView.findViewById(R.id.tv_juego_descripcion);
            ivIcono = itemView.findViewById(R.id.iv_juego_icono);
        }
    }
}