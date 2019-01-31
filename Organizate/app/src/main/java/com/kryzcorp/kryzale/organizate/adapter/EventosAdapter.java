package com.kryzcorp.kryzale.organizate.adapter;

import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kryzcorp.kryzale.organizate.R;
import com.kryzcorp.kryzale.organizate.entidades.Evento;
import com.kryzcorp.kryzale.organizate.entidades.VolleySingleton;


public class EventosAdapter extends RecyclerView.Adapter<EventosAdapter.UsuariosHolder>{
    int iddelEvento;
    StringRequest cadenaRequest;

    List<Evento> listaEventos;

    public EventosAdapter(List<Evento> listaEventos) {
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
        iddelEvento = listaEventos.get(position).getIdEvento();
    }

    @Override
    public int getItemCount() {
        return listaEventos.size();
    }

    public class UsuariosHolder extends RecyclerView.ViewHolder{

        TextView txtUbic,txtTit,txtfec,txtIni,txtFin, txtNot,txtNotific;
        Button justDelete;


        public UsuariosHolder(final View itemView) {
            super(itemView);
            txtUbic= (TextView) itemView.findViewById(R.id.txtUbicacion);
            txtTit= (TextView) itemView.findViewById(R.id.txtTitulo);
            txtfec= (TextView) itemView.findViewById(R.id.txtFecha);
            txtIni= (TextView) itemView.findViewById(R.id.txtInicio);
            txtFin= (TextView) itemView.findViewById(R.id.txtFin);
            txtNot= (TextView) itemView.findViewById(R.id.txtNota);
            txtNotific= (TextView) itemView.findViewById(R.id.txtNotificar);
            justDelete = itemView.findViewById(R.id.btnDelete);
            justDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webServiceEliminar();
                    Toast.makeText(itemView.getContext(),"Eliminando..." +txtUbic.getText().toString(), Toast.LENGTH_LONG).show();
                }
            });

        }
        private void webServiceEliminar() {
            final ProgressDialog pDialog;
            pDialog=new ProgressDialog(itemView.getContext());
            pDialog.setMessage("Cargando...");
            pDialog.show();

            String ip="https://organizateespe.000webhostapp.com";

            String url=ip+"/wsJSONDeleteEvento.php?id_evento="+String.valueOf(iddelEvento);

            cadenaRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.hide();

                    if (response.trim().equalsIgnoreCase("Borra")){
                        Toast.makeText(itemView.getContext(),"Se ha Eliminado con exito",Toast.LENGTH_SHORT).show();
                    }else{
                        if (response.trim().equalsIgnoreCase("noBorra")){
                            Toast.makeText(itemView.getContext(),"No se ha eliminado ",Toast.LENGTH_SHORT).show();
                            Log.i("RESPUESTA: ",""+response);
                        }else{
                            Toast.makeText(itemView.getContext(),"No existe el evento ",Toast.LENGTH_SHORT).show();
                            Log.i("RESPUESTA: ",""+response);
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(itemView.getContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();
                    pDialog.hide();
                }
            });
            //request.add(stringRequest);
            VolleySingleton.getIntanciaVolley(itemView.getContext()).addToRequestQueue(cadenaRequest);
        }
    }
}
