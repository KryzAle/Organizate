package com.kryzcorp.kryzale.organizate.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import com.kryzcorp.kryzale.organizate.R;
import com.kryzcorp.kryzale.organizate.adapter.EventosFechaAdapter;
import com.kryzcorp.kryzale.organizate.entidades.Evento;
import com.kryzcorp.kryzale.organizate.entidades.VolleySingleton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConsutarListaEventosPorFechaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConsutarListaEventosPorFechaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsutarListaEventosPorFechaFragment extends Fragment
        implements Response.Listener<JSONObject>,Response.ErrorListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerUsuarios;
    ArrayList<Evento> listaEventos;

    ProgressDialog dialog;
    EditText campoFecha;
    TextView txtNombre,txtProfesion;
    Button btnConsultarUsuario;
    ProgressDialog progreso;
    ImageView campoImagen;
    DatePickerDialog dpd;
    Calendar c;

   // RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    public ConsutarListaEventosPorFechaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConsutarListaEventosPorFechaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsutarListaEventosPorFechaFragment newInstance(String param1, String param2) {
        ConsutarListaEventosPorFechaFragment fragment = new ConsutarListaEventosPorFechaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista= inflater.inflate(R.layout.fragment_consutar_lista_eventos_fecha, container, false);
        campoFecha= (EditText) vista.findViewById(R.id.et_fecha);



        recyclerUsuarios = (RecyclerView) vista.findViewById(R.id.idRecyclerImagen);
        recyclerUsuarios.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerUsuarios.setHasFixedSize(true);
        btnConsultarUsuario= (Button) vista.findViewById(R.id.btn_consultar);
        campoFecha.setInputType(InputType.TYPE_NULL);
        campoFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c= Calendar.getInstance();
                int day= c.get(Calendar.DAY_OF_MONTH);
                final int month= c.get(Calendar.MONTH);
                int year= c.get(Calendar.YEAR);
                dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        campoFecha.setText(i+"/"+(i1+1)+"/"+i2);

                    }
                },day,month,year);

                dpd.show();
            }
        });

        // request= Volley.newRequestQueue(getContext());

        btnConsultarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarWebService();
            }
        });
       // request= Volley.newRequestQueue(getContext());

        return vista;
    }

    private void cargarWebService() {

        dialog=new ProgressDialog(getContext());
        dialog.setMessage("Consultando Imagenes");
        dialog.show();

        String ip=getString(R.string.ip);

        String url=ip+"/wsJSONConsultarEventoFecha.php?id_usuario="+dameUsuario()+"&fecha="+campoFecha.getText().toString();;
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
       // request.add(jsonObjectRequest);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        Evento evento =null;
        listaEventos =new ArrayList<>();


        JSONArray json=response.optJSONArray("evento");

        try {

            for (int i=0;i<json.length();i++){
                evento =new Evento();
                JSONObject jsonObject=null;
                jsonObject=json.getJSONObject(i);

                evento.setIdEvento(jsonObject.optInt("id_evento"));
                evento.setUbicacion(jsonObject.optString("ubicacion"));
                evento.setTitulo(jsonObject.optString("titulo"));
                evento.setFecha(jsonObject.optString("fecha"));
                evento.setInicio(jsonObject.optString("inicio"));
                evento.setFin(jsonObject.optString("fin"));
                evento.setNota(jsonObject.optString("nota"));
                evento.setNotificar(jsonObject.optString("notificar"));
                evento.setIdUser(jsonObject.optInt("id_usuario"));
                listaEventos.add(evento);
            }
            dialog.hide();
            EventosFechaAdapter adapter=new EventosFechaAdapter(listaEventos);
            recyclerUsuarios.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor" +
                    " "+response, Toast.LENGTH_LONG).show();
            dialog.hide();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public int dameUsuario(){
        return 1;
    }
}
