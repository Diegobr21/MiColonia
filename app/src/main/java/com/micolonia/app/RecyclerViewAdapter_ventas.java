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

class RecyclerViewAdapter_ventas extends RecyclerView.Adapter<RecyclerViewAdapter_ventas.VentasViewHolder> {
    private final List<Venta> arrayventas;
    private OnVentaListener mOnVentaListener;

    RecyclerViewAdapter_ventas(List<Venta> arrayventas, OnVentaListener onVentaListener){
        this.arrayventas = arrayventas;
        this.mOnVentaListener = onVentaListener;
    }

    public class VentasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cardView;
        TextView titulo_venta;
        ImageView Logo;
        TextView telefono;
        OnVentaListener onVentaListener;

        VentasViewHolder(View itemView, OnVentaListener onVentaListener) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.cardView);
            titulo_venta = (TextView)itemView.findViewById(R.id.nombre_venta);
            Logo = (ImageView)itemView.findViewById(R.id.imagen_venta);
            telefono = itemView.findViewById(R.id.telefono_venta);
            this.onVentaListener = onVentaListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onVentaListener.onVentaClick(getAdapterPosition(), arrayventas.get(getAdapterPosition()));
        }
    }
    @Override
    public VentasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartas_ventas, parent, false);
        VentasViewHolder cvh = new VentasViewHolder(view, mOnVentaListener);
        return cvh;
    }
    @Override
    public void onBindViewHolder(VentasViewHolder holder, int position) {
        holder.titulo_venta.setText(arrayventas.get(position).name);
        holder.telefono.setText("Celular: "+ arrayventas.get(position).telefono);

        int idType = arrayventas.get(position).logoId;

       /* if (idType == Aviso.LOGO_ID_TYPE_FOOD) {
            holder.Logo.setImageResource(R.drawable.food);
        } else {
            holder.Logo.setImageResource(R.drawable.wreck);
        }
        */
        if (arrayventas.get(position).getImagen() != null) {
            // Si existe la imagen la cargamos
            Glide.with(holder.itemView.getContext())
                    .load(arrayventas.get(position).getImagen())
                    .into(holder.Logo);

        }else if( arrayventas.get(position).getTipo()==2){

            holder.Logo.setImageResource(R.drawable.casa);
            }
            else{
                holder.Logo.setImageResource(R.drawable.logonegropng);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    @Override
    public int getItemCount() {
        return arrayventas.size();
    }

    public interface OnVentaListener {
        void onVentaClick(int position, Venta venta);
    }
}
