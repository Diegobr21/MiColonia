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

class RecyclerViewAdapter_servicios extends RecyclerView.Adapter<RecyclerViewAdapter_servicios.NegocioSerViewHolder> {
    private final List<NegocioSer> arrayservicios;
    private OnNegocioSerListener mOnNegocioserListener;

    RecyclerViewAdapter_servicios(List<NegocioSer> arrayservicios, OnNegocioSerListener onNegocioSerListener){
        this.arrayservicios = arrayservicios;
        this.mOnNegocioserListener = onNegocioSerListener;

    }
    public class NegocioSerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        TextView nombre_servicio;
        TextView descripcion;
        ImageView Logo;
        OnNegocioSerListener onNegocioSerListener;

        NegocioSerViewHolder(View itemView, OnNegocioSerListener onNegocioSerListener) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.cardView);
            nombre_servicio = (TextView)itemView.findViewById(R.id.nombre_servicio);
            descripcion =(TextView)itemView.findViewById(R.id.serv_desc);
            Logo = (ImageView)itemView.findViewById(R.id.logo);
            this.onNegocioSerListener = onNegocioSerListener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onNegocioSerListener.onNegocioSerClick(getAdapterPosition(), arrayservicios.get(getAdapterPosition()));
        }
    }
    @Override
    public NegocioSerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartas_servicios, parent, false);
        NegocioSerViewHolder cvh = new NegocioSerViewHolder(view, mOnNegocioserListener);
        return cvh;
    }
    @Override
    public void onBindViewHolder(NegocioSerViewHolder holder, int position) {
        holder.nombre_servicio.setText(arrayservicios.get(position).name);
        holder.descripcion.setText(arrayservicios.get(position).descripcion);


        int idType = arrayservicios.get(position).logoId;

        /*if (idType == Aviso.LOGO_ID_TYPE_FOOD) {
            holder.Logo.setImageResource(R.drawable.food);
        } else {
            holder.Logo.setImageResource(R.drawable.wreck);
        }*/
        if (arrayservicios.get(position).getImagen() != null) {
            // Si existe la imagen la cargamos
            Glide.with(holder.itemView.getContext())
                    .load(arrayservicios.get(position).getImagen())
                    .centerCrop()
                    .fitCenter()
                    .into(holder.Logo);
        }else{
            // Si no existe no cargamos nada o la imagen estandar
            holder.Logo.setImageResource(R.drawable.wreck);
        }
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    @Override
    public int getItemCount() {
        return arrayservicios.size();
    }

    public interface OnNegocioSerListener{
        void onNegocioSerClick(int position, NegocioSer negocioSer);
    }
}
