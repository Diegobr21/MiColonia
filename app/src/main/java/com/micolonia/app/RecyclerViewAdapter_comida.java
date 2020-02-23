package com.micolonia.app;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

class RecyclerViewAdapter_comida extends RecyclerView.Adapter<RecyclerViewAdapter_comida.NegocioComViewHolder> {
    private final List<NegocioCom> arraycomida;
    private OnNegocioListener mOnNegocioListener;

    RecyclerViewAdapter_comida(List<NegocioCom> arraycomida, OnNegocioListener onNegocioListener) {
        this.arraycomida = arraycomida;
        this.mOnNegocioListener = onNegocioListener;

    }

    public class NegocioComViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        TextView nombre_platillo;
        TextView descripcion;
        ImageView Logo;
        OnNegocioListener onNegocioListener;

        NegocioComViewHolder(View itemView, OnNegocioListener onNegocioListener) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            nombre_platillo = (TextView) itemView.findViewById(R.id.dish_name);
            descripcion = (TextView)itemView.findViewById(R.id.dish_desc);
            Logo = (ImageView) itemView.findViewById(R.id.logo);
            this.onNegocioListener = onNegocioListener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onNegocioListener.onNegocioClick(getAdapterPosition(), arraycomida.get(getAdapterPosition()));
        }
    }

    @Override
    public NegocioComViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartas_comida, parent, false);
        NegocioComViewHolder cvh = new NegocioComViewHolder(view, mOnNegocioListener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(NegocioComViewHolder holder, int position) {
        holder.nombre_platillo.setText(arraycomida.get(position).name);
        holder.descripcion.setText(arraycomida.get(position).descripcion);

        int idType = arraycomida.get(position).logoId;

        /*if (idType == Aviso.LOGO_ID_TYPE_FOOD) {
            holder.Logo.setImageResource(R.drawable.food);
        } else {
            holder.Logo.setImageResource(R.drawable.wreck);
        }*/
        if (arraycomida.get(position).getImagen() != null) {
            // Si existe la imagen la cargamos
            Glide.with(holder.itemView.getContext())
                    .load(arraycomida.get(position).getImagen())
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
        return arraycomida.size();
    }

    public interface OnNegocioListener {
        void onNegocioClick(int position, NegocioCom negocioCom);
    }
}
