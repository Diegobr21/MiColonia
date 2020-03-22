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


class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.AvisosViewHolder> {
    private final List<Aviso> arrayavisos;

    RecyclerViewAdapter(List<Aviso> arrayavisos) {
        this.arrayavisos = arrayavisos;
    }

    public class AvisosViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView titulo_aviso;
        ImageView Logo;
        TextView fecha;

        AvisosViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            titulo_aviso = (TextView) itemView.findViewById(R.id.titulo_aviso);
            Logo = (ImageView) itemView.findViewById(R.id.imagen_aviso);
            fecha=(TextView)itemView.findViewById(R.id.fecha_publicada);
        }
    }

    @Override
    public AvisosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartas_noticias, parent, false);
        AvisosViewHolder cvh = new AvisosViewHolder(view);
        return cvh;
    }

    public void oD(int position){

    }

    @Override
    public void onBindViewHolder(AvisosViewHolder holder, int position) {
        holder.titulo_aviso.setText(arrayavisos.get(position).name);
        int idType = arrayavisos.get(position).logoId;
        holder.fecha.setText(arrayavisos.get(position).fecha);

       /* if (idType == Aviso.LOGO_ID_TYPE_FOOD) {
            holder.Logo.setImageResource(R.drawable.food);
        } else {
            holder.Logo.setImageResource(R.drawable.wreck);
        }*/

        if (arrayavisos.get(position).getImagen() != null) {
            // Si existe la imagen la cargamos
            Glide.with(holder.itemView.getContext())
                    .load(arrayavisos.get(position).getImagen())
                    .into(holder.Logo);

        }else if( arrayavisos.get(position).getTipo()==2){

            holder.Logo.setImageResource(R.drawable.aviso);
        }
        else if( arrayavisos.get(position).getTipo()==3){

            holder.Logo.setImageResource(R.drawable.reciclaje);
        }
        else{
            holder.Logo.setImageResource(R.drawable.micolonialogo1);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return arrayavisos.size();
    }
}
