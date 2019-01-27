package com.kryzcorp.kryzale.organizate.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kryzcorp.kryzale.organizate.R;
import com.kryzcorp.kryzale.organizate.adapter.UsuariosAdapter;
import com.kryzcorp.kryzale.organizate.entidades.Evento;
import com.kryzcorp.kryzale.organizate.entidades.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BienvenidaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BienvenidaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BienvenidaFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    RecyclerView recyclerUsuarios;
    ArrayList<Evento> packeventos = new ArrayList<Evento>() ;
    MCalendarView calendarView;
    ProgressDialog progress;
    Button btnActualizarCalendar;
    Evento evento =null;

    // RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    public BienvenidaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BienvenidaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BienvenidaFragment newInstance(String param1, String param2) {
        BienvenidaFragment fragment = new BienvenidaFragment();
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
        View vista= inflater.inflate(R.layout.fragment_bienvenida, container, false);
        calendarView = (MCalendarView) vista.findViewById(R.id.calendar);
        btnActualizarCalendar = vista.findViewById(R.id.btnsync);
        cargarWebService();


        calendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                for (int i=0;i<packeventos.size();i++) {
                    String currentFecha = String.valueOf(date.getYear()) + "-" + date.getMonthString() + "-" + date.getDayString();
                    if (packeventos.get(i).getFecha().equals(currentFecha)){
                        Toast.makeText(getActivity().getApplicationContext(), "Eventos : "+currentFecha+ "\n"
                                        +"Ubicacion: "+packeventos.get(i).getUbicacion()+"\n"
                                        +"Ubicacion: "+packeventos.get(i).getUbicacion()+"\n"
                                        +"Titulo: "+packeventos.get(i).getTitulo()+"\n"
                                        +"Hora Inicio: "+packeventos.get(i).getInicio()+"\n"
                                        +"Hora Fin: "+packeventos.get(i).getFin()+"\n"
                                        +"Nota: "+packeventos.get(i).getNota()+"\n"
                                        +"Notificar: "+packeventos.get(i).getNotificar()+"\n"
                                , Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        btnActualizarCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarWebService();
            }
        });

        return vista;
    }

    private void cargarWebService() {

        progress=new ProgressDialog(getContext());
        progress.setMessage("Sincronizando...");
        progress.show();

        String ip=getString(R.string.ip);

        String url=ip+"/wsJSONConsultarEventoList.php?id_usuario=1";


        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        // request.add(jsonObjectRequest);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
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

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
        System.out.println();
        Log.d("ERROR: ", error.toString());
        progress.hide();
    }
    @Override
    public void onResponse(JSONObject response) {

        String fecha;
        String[] parts;


        JSONArray json=response.optJSONArray("evento");

        try {

            for (int i=0;i<json.length();i++){
                evento =new Evento();
                JSONObject jsonObject=null;
                jsonObject=json.getJSONObject(i);

                evento.setUbicacion(jsonObject.optString("ubicacion"));
                evento.setTitulo(jsonObject.optString("titulo"));
                evento.setFecha(jsonObject.optString("fecha"));
                evento.setInicio(jsonObject.optString("inicio"));
                evento.setFin(jsonObject.optString("fin"));
                evento.setNota(jsonObject.optString("nota"));
                evento.setNotificar(jsonObject.optString("notificar"));
                evento.setIdUser(jsonObject.optInt("id"));

                fecha=evento.getFecha();
                parts = fecha.split("-");

                calendarView.markDate(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]),Integer.parseInt(parts[2]));
                //listaEventos.add(evento);
                packeventos.add(evento);
            }
            progress.hide();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor" +
                    " "+response, Toast.LENGTH_LONG).show();
            progress.hide();
        }
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
}
