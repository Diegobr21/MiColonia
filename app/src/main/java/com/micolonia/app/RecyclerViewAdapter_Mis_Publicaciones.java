package com.micolonia.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

class RecyclerViewAdapter_Mis_Publicaciones extends RecyclerView.Adapter<RecyclerViewAdapter_Mis_Publicaciones.PublicacionViewHolder> {
    private final List<NegocioCom> arraypublicaciones;
    private OnPublicacionListener mOnPublicacionListener;

    RecyclerViewAdapter_Mis_Publicaciones(List<NegocioCom> arraypublicaciones, OnPublicacionListener onPublicacionListener) {
        this.arraypublicaciones = arraypublicaciones;
        this.mOnPublicacionListener = onPublicacionListener;
    }

    public class PublicacionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            CardView cardView;
            TextView nombre_platillo;
            TextView descripcion;
            ImageView Logo;
            OnPublicacionListener onPublicacionListener;

            PublicacionViewHolder(View itemView, OnPublicacionListener onPublicacionListener) {
                super(itemView);
                cardView = (CardView) itemView.findViewById(R.id.cardView);
                nombre_platillo = (TextView) itemView.findViewById(R.id.dish_name);
                descripcion = (TextView)itemView.findViewById(R.id.dish_desc);
                Logo = (ImageView) itemView.findViewById(R.id.logo);
                this.onPublicacionListener = onPublicacionListener;
                itemView.setOnClickListener(this);
            }


            @Override
            public void onClick(View v) {
                onPublicacionListener.onPublicacionClick(getAdapterPosition(), arraypublicaciones.get(getAdapterPosition()));
            }
    }


        @Override
        public PublicacionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartas_comida, parent, false);
            PublicacionViewHolder cvh = new PublicacionViewHolder(view, mOnPublicacionListener);
            return cvh;
        }

        @Override
        public void onBindViewHolder(PublicacionViewHolder holder, int position) {
            holder.nombre_platillo.setText(arraypublicaciones.get(position).name);
            holder.descripcion.setText(arraypublicaciones.get(position).descripcion);

            int idType = arraypublicaciones.get(position).logoId;

            if (arraypublicaciones.get(position).getImagen() != null) {
                // Si existe la imagen la cargamos
                Glide.with(holder.itemView.getContext())
                        .load(arraypublicaciones.get(position).getImagen())
                        .centerCrop()
                        .fitCenter()
                        .into(holder.Logo);
            }else{
                // Si no existe no cargamos nada o la imagen estandar
                holder.Logo.setImageResource(R.drawable.food);
            }
    }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
        @Override
        public int getItemCount() {
            return arraypublicaciones.size();
        }
        public interface OnPublicacionListener {
            void onPublicacionClick(int position, NegocioCom publicacion);
        }
}
