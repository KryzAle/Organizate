package com.kryzcorp.kryzale.organizate.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


import com.kryzcorp.kryzale.organizate.R;
import com.kryzcorp.kryzale.organizate.entidades.Evento;


public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.UsuariosHolder>{

    List<Evento> listaEventos;

    public UsuariosAdapter(List<Evento> listaEventos) {
        this.listaEventos = listaEventos;
    }

    @Override
    public UsuariosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.eventos_list,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new UsuariosHolder(vista);
    }

    @Override
    public void onBindViewHolder(UsuariosHolder holder, int position) {
        holder.txtUbic.setText(listaEventos.get(position).getUbicacion().toString());
        holder.txtTit.setText(listaEventos.get(position).getTitulo().toString());
        holder.txtfec.setText(listaEventos.get(position).getFecha().toString());
        holder.txtIni.setText(listaEventos.get(position).getInicio().toString());
        holder.txtFin.setText(listaEventos.get(position).getFin().toString());
        holder.txtNot.setText(listaEventos.get(position).getNota().toString());
        holder.txtNotific.setText(listaEventos.get(position).getNotificar().toString());
    }

    @Override
    public int getItemCount() {
        return listaEventos.size();
    }

    public class UsuariosHolder extends RecyclerView.ViewHolder{

        TextView txtUbic,txtTit,txtfec,txtIni,txtFin, txtNot,txtNotific;

        public UsuariosHolder(View itemView) {
            super(itemView);
            txtUbic= (TextView) itemView.findViewById(R.id.txtUbicacion);
            txtTit= (TextView) itemView.findViewById(R.id.txtTitulo);
            txtfec= (TextView) itemView.findViewById(R.id.txtFecha);
            txtIni= (TextView) itemView.findViewById(R.id.txtInicio);
            txtFin= (TextView) itemView.findViewById(R.id.txtFin);
            txtNot= (TextView) itemView.findViewById(R.id.txtNota);
            txtNotific= (TextView) itemView.findViewById(R.id.txtNotificar);

        }
    }
}
