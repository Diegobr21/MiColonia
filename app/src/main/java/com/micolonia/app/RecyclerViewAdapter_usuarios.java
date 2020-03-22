package com.micolonia.app;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

class RecyclerViewAdapter_usuarios extends RecyclerView.Adapter<RecyclerViewAdapter_usuarios.UsuariosViewHolder> {
    private final List<Usuario> arrayusuarios;
    private RecyclerViewAdapter_usuarios.OnUsuarioListener mOnUsuarioListener;

    RecyclerViewAdapter_usuarios(List<Usuario> arrayusuarios, OnUsuarioListener onUsuarioListener) {
        this.arrayusuarios = arrayusuarios;
        this.mOnUsuarioListener = onUsuarioListener;
    }

    public class UsuariosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        TextView nombre_usu;
        //TextView email_usu;
        TextView calle_usu;
        TextView num_calle_usu;
        TextView status;
        OnUsuarioListener onUsuarioListener;

        UsuariosViewHolder(View itemView, OnUsuarioListener onUsuarioListener) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView_usuarios);
            nombre_usu = (TextView) itemView.findViewById(R.id.nombre_usu_C);
            //email_usu = (TextView) itemView.findViewById(R.id.correo_usu_C);
            calle_usu = (TextView) itemView.findViewById(R.id.calle_usu_C);
            num_calle_usu = (TextView) itemView.findViewById(R.id.num_calle_usu_C);
            status = (TextView) itemView.findViewById(R.id.status_usu_C);

            this.onUsuarioListener = onUsuarioListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            onUsuarioListener.onUsuarioClick(getAdapterPosition(), arrayusuarios.get(getAdapterPosition()));
        }
    }

    @Override
    public UsuariosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartas_usuarios, parent, false);
        UsuariosViewHolder cvh = new UsuariosViewHolder(view, mOnUsuarioListener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(UsuariosViewHolder holder, int position) {
        holder.nombre_usu.setText(arrayusuarios.get(position).nombre);
        //holder.email_usu.setText(arrayusuarios.get(position).email);
        holder.calle_usu.setText(arrayusuarios.get(position).nombre_calle);
        holder.num_calle_usu.setText(arrayusuarios.get(position).num_calle);
        holder.status.setText(arrayusuarios.get(position).status);



    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public interface OnUsuarioListener {
        void onUsuarioClick(int position, Usuario usuario);
    }

    @Override
    public int getItemCount() {
        return arrayusuarios.size();
    }
}
