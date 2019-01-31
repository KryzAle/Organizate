package com.kryzcorp.kryzale.organizate.adapter;

import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kryzcorp.kryzale.organizate.entidades.Evento;
import com.kryzcorp.kryzale.organizate.R;
import com.kryzcorp.kryzale.organizate.entidades.VolleySingleton;

import java.util.List;


public class EventosFechaAdapter extends RecyclerView.Adapter<EventosFechaAdapter.UsuariosHolder>{
    int idEvento;
    StringRequest stringRequest;

    List<Evento> listaEventos;

    public EventosFechaAdapter(List<Evento> listaEventos) {
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
        holder.txtTitulo.setText(listaEventos.get(position).getTitulo().toString());
        holder.txtFecha.setText(listaEventos.get(position).getFecha().toString());
        holder.txtNota.setText(listaEventos.get(position).getNota().toString());
        idEvento = listaEventos.get(position).getIdEvento();

    }

    @Override
    public int getItemCount() {
        return listaEventos.size();
    }

    public class UsuariosHolder extends RecyclerView.ViewHolder{

        TextView txtTitulo,txtFecha,txtNota;
        ImageView imagen;
        Button eliminarButton;

        public UsuariosHolder(final View itemView) {
            super(itemView);
            txtTitulo= (TextView) itemView.findViewById(R.id.idTitulo);
            txtFecha= (TextView) itemView.findViewById(R.id.idFecha);
            txtNota= (TextView) itemView.findViewById(R.id.idNota);
            imagen=(ImageView) itemView.findViewById(R.id.idImagen);
            eliminarButton = itemView.findViewById(R.id.btn_eliminar);
            eliminarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webServiceEliminar();
                    Toast.makeText(itemView.getContext(),"Eliminando..." +txtTitulo.getText().toString(), Toast.LENGTH_LONG).show();
                }
            });
        }

        private void webServiceEliminar() {
            final ProgressDialog pDialog;
            pDialog=new ProgressDialog(itemView.getContext());
            pDialog.setMessage("Cargando...");
            pDialog.show();

            String ip="https://organizateespe.000webhostapp.com";

            String url=ip+"/wsJSONDeleteEvento.php?id_evento="+String.valueOf(idEvento);

            stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
            VolleySingleton.getIntanciaVolley(itemView.getContext()).addToRequestQueue(stringRequest);
        }



    }

}
