package com.micolonia.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter_comentarios extends RecyclerView.Adapter<RecyclerViewAdapter_comentarios.ComentariosViewHolder> {
        private final List<Comentario> arraycomentarios;


    RecyclerViewAdapter_comentarios(List<Comentario> arraycomentarios) {
        this.arraycomentarios = arraycomentarios;

    }

    public class ComentariosViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView nombre_usuario;
        TextView comentario;


        ComentariosViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView_comentario);
            nombre_usuario = (TextView) itemView.findViewById(R.id.nombre_comentador);
            comentario=(TextView)itemView.findViewById(R.id.comentario);

        }

    }

    @Override
    public ComentariosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carta_comentario, parent, false);
        ComentariosViewHolder cvh = new ComentariosViewHolder(view);
        return cvh;
    }



    @Override
    public void onBindViewHolder(ComentariosViewHolder holder, int position) {
        holder.nombre_usuario.setText(arraycomentarios.get(position).nombre_usu);
        holder.comentario.setText(arraycomentarios.get(position).comentario);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return arraycomentarios.size();
    }


}
