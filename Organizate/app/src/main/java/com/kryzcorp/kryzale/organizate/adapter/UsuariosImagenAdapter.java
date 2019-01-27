package com.kryzcorp.kryzale.organizate.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kryzcorp.kryzale.organizate.entidades.Evento;
import com.kryzcorp.kryzale.organizate.R;
import java.util.List;


public class UsuariosImagenAdapter extends RecyclerView.Adapter<UsuariosImagenAdapter.UsuariosHolder>{

    List<Evento> listaEventos;

    public UsuariosImagenAdapter(List<Evento> listaEventos) {
        this.listaEventos = listaEventos;
    }

    @Override
    public UsuariosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.usuarios_list_image,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new UsuariosHolder(vista);
    }

    @Override
    public void onBindViewHolder(UsuariosHolder holder, int position) {
        holder.txtDocumento.setText(listaEventos.get(position).getNotificar().toString());
        holder.txtNombre.setText(listaEventos.get(position).getTitulo().toString());
        holder.txtProfesion.setText(listaEventos.get(position).getNota().toString());

    }

    @Override
    public int getItemCount() {
        return listaEventos.size();
    }

    public class UsuariosHolder extends RecyclerView.ViewHolder{

        TextView txtDocumento,txtNombre,txtProfesion;
        ImageView imagen;

        public UsuariosHolder(View itemView) {
            super(itemView);
            txtDocumento= (TextView) itemView.findViewById(R.id.idDocumento);
            txtNombre= (TextView) itemView.findViewById(R.id.idNombre);
            txtProfesion= (TextView) itemView.findViewById(R.id.idProfesion);
            imagen=(ImageView) itemView.findViewById(R.id.idImagen);
        }
    }
}
