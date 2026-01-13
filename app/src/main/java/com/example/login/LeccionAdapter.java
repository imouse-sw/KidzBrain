package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class LeccionAdapter extends RecyclerView.Adapter<LeccionAdapter.LeccionViewHolder> {

    private Context context;
    private List<Leccion> lecciones;
    private String materia;
    private int nivelDesbloqueado;

    public LeccionAdapter(Context context, List<Leccion> lecciones, String materia, int nivelDesbloqueado) {
        this.context = context;
        this.lecciones = lecciones;
        this.materia = materia;
        this.nivelDesbloqueado = nivelDesbloqueado;
    }

    @NonNull
    @Override
    public LeccionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_leccion, parent, false);
        return new LeccionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeccionViewHolder holder, int position) {
        Leccion leccion = lecciones.get(position);

        holder.tvTitulo.setText(leccion.getTitulo());
        holder.tvSubtitulo.setText(leccion.getSubtitulo());
        holder.imgLeccion.setImageResource(leccion.getImagenResId());

        boolean isUnlocked = nivelDesbloqueado >= leccion.getNivel();

        if (isUnlocked) {
            holder.imgLock.setVisibility(View.GONE);
            holder.scrim.setVisibility(View.GONE);
            holder.itemView.setAlpha(1.0f);
            holder.cardView.setOnClickListener(v -> {
                Intent intent = new Intent(context, leccion.getActividad());
                intent.putExtra("materia", materia);
                intent.putExtra("nivel", leccion.getNivel());
                context.startActivity(intent);
            });
        } else {
            holder.imgLock.setVisibility(View.VISIBLE);
            holder.scrim.setVisibility(View.VISIBLE);
            holder.itemView.setAlpha(0.7f);
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            holder.imgLeccion.setColorFilter(filter);
            holder.cardView.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return lecciones.size();
    }

    public static class LeccionViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        ImageView imgLeccion, imgLock;
        TextView tvTitulo, tvSubtitulo;
        View scrim;

        public LeccionViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_leccion);
            imgLeccion = itemView.findViewById(R.id.img_leccion);
            imgLock = itemView.findViewById(R.id.img_lock);
            tvTitulo = itemView.findViewById(R.id.tv_titulo_leccion);
            tvSubtitulo = itemView.findViewById(R.id.tv_subtitulo_leccion);
            scrim = itemView.findViewById(R.id.scrim);
        }
    }
}