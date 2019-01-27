package com.kryzcorp.kryzale.organizate.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.kryzcorp.kryzale.organizate.R;
import com.kryzcorp.kryzale.organizate.entidades.VolleySingleton;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrarEventoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistrarEventoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrarEventoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private static final String CARPETA_PRINCIPAL = "misImagenesApp/";//directorio principal
    private static final String CARPETA_IMAGEN = "imagenes";//carpeta donde se guardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;//ruta carpeta de directorios
    private String path;//almacena la ruta de la imagen
    File fileImagen;
    Bitmap bitmap;

    private final int MIS_PERMISOS = 100;
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;

    EditText campoUbic,campoTitul,campoFec,campoIni,campoFi,campoNot,campoNotific;
    Button botonRegistro;
    ImageView imgFoto;
    ProgressDialog progreso;
    Calendar c;
    DatePickerDialog dpd;
    TimePickerDialog tpd;

    RelativeLayout layoutRegistrar;//permisos

   // RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    StringRequest stringRequest;

    public RegistrarEventoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrarEventoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrarEventoFragment newInstance(String param1, String param2) {
        RegistrarEventoFragment fragment = new RegistrarEventoFragment();
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

        View vista=inflater.inflate(R.layout.fragment_registrar_evento, container, false);
        campoUbic= (EditText) vista.findViewById(R.id.campoUbicacion);
        campoTitul= (EditText) vista.findViewById(R.id.campoTitulo);
        campoFec= (EditText) vista.findViewById(R.id.campoFecha);
        campoIni= (EditText) vista.findViewById(R.id.campoInicio);
        campoFi= (EditText) vista.findViewById(R.id.campoFin);
        campoNot= (EditText) vista.findViewById(R.id.campoNota);
        campoNotific= (EditText) vista.findViewById(R.id.campoNotificar);
        botonRegistro= (Button) vista.findViewById(R.id.btnRegistrar);


        layoutRegistrar= (RelativeLayout) vista.findViewById(R.id.idLayoutRegistrar);
        campoFec.setInputType(InputType.TYPE_NULL);
        campoIni.setInputType(InputType.TYPE_NULL);
        campoFi.setInputType(InputType.TYPE_NULL);

        campoFec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c= Calendar.getInstance();
                int day= c.get(Calendar.DAY_OF_MONTH);
                final int month= c.get(Calendar.MONTH);
                int year= c.get(Calendar.YEAR);
                dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        campoFec.setText(i+"/"+(i1+1)+"/"+i2);

                    }
                },day,month,year);

                dpd.show();
            }
        });
        campoIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c= Calendar.getInstance();
                int hora= c.get(Calendar.HOUR_OF_DAY);
                int minuto= c.get(Calendar.MINUTE);
                tpd = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        campoIni.setText(i+":"+i1);
                    }
                },hora,minuto,DateFormat.is24HourFormat(getActivity()));
                tpd.show();
            }
        });
        campoFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c= Calendar.getInstance();
                int hora= c.get(Calendar.HOUR_OF_DAY);
                int minuto= c.get(Calendar.MINUTE);
                tpd = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        campoFi.setText(i+":"+i1);
                    }
                },hora,minuto,DateFormat.is24HourFormat(getActivity()));
                tpd.show();
            }
        });


        campoNotific.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                c= Calendar.getInstance();
                int hora= c.get(Calendar.HOUR_OF_DAY);
                int minuto= c.get(Calendar.MINUTE);
                tpd = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {


                        campoNotific.setText( campoNotific.getText()+" "+i+":"+i1+":00");
                    }
                },hora,minuto,DateFormat.is24HourFormat(getActivity()));
                tpd.show();

                c= Calendar.getInstance();
                int day= c.get(Calendar.DAY_OF_MONTH);
                final int month= c.get(Calendar.MONTH);
                int year= c.get(Calendar.YEAR);
                dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        campoNotific.setText(i+"/"+(i1+1)+"/"+i2);

                    }
                },day,month,year);

                dpd.show();

            }
        });


        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarWebService();
            }
        });

        /*btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogOpciones();
            }
        });*/

        return vista;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case COD_SELECCIONA:
                Uri miPath=data.getData();
                imgFoto.setImageURI(miPath);

                try {
                    bitmap=MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),miPath);
                    imgFoto.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case COD_FOTO:
                MediaScannerConnection.scanFile(getContext(), new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Path",""+path);
                            }
                        });

                bitmap= BitmapFactory.decodeFile(path);
                imgFoto.setImageBitmap(bitmap);

                break;
        }
        bitmap=redimensionarImagen(bitmap,600,800);
    }

    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {

        int ancho=bitmap.getWidth();
        int alto=bitmap.getHeight();

        if(ancho>anchoNuevo || alto>altoNuevo){
            float escalaAncho=anchoNuevo/ancho;
            float escalaAlto= altoNuevo/alto;

            Matrix matrix=new Matrix();
            matrix.postScale(escalaAncho,escalaAlto);

            return Bitmap.createBitmap(bitmap,0,0,ancho,alto,matrix,false);

        }else{
            return bitmap;
        }


    }




    ///////////////
    private void cargarWebService() {

        progreso=new ProgressDialog(getContext());
        progreso.setMessage("Cargando...");
        progreso.show();

        String ip=getString(R.string.ip);

        String url=ip+"/wsJSONRegistroEvento.php?";

        stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progreso.hide();

                if (response.trim().equalsIgnoreCase("registra")){
                    campoUbic.setText("");
                    campoFec.setText("");
                    campoFi.setText("");
                    campoIni.setText("");
                    campoTitul.setText("");
                    campoNot.setText("");
                    campoNotific.setText("");
                    Toast.makeText(getContext(),"Se ha registrado con exito",Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(getContext(),"No se ha registrado ",Toast.LENGTH_SHORT).show();
                    Log.i("RESPUESTA: ",""+response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();
                progreso.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String ubicacion=campoUbic.getText().toString();
                String titulo=campoTitul.getText().toString();
                /*String fecha="2019-05-06";
                String inicio="05:05:05";
                String fin="05:05:05";
                String nota="Esta es una nota";
                String notificar="2019-05-06 05:05:05";*/
                String fecha=campoFec.getText().toString();
                String inicio=campoIni.getText().toString();
                String fin=campoFi.getText().toString();
                String nota=campoNot.getText().toString();
                String notificar=campoNotific.getText().toString();


                Map<String,String> parametros=new HashMap<>();
                parametros.put("ubicacion",ubicacion);
                parametros.put("titulo",titulo);
                parametros.put("fecha",fecha);
                parametros.put("inicio",inicio);
                parametros.put("fin",fin);
                parametros.put("nota",nota);
                parametros.put("notificar",notificar);
                parametros.put("id","1");

                return parametros;
            }
        };
        //request.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(stringRequest);
    }

    private String convertirImgString(Bitmap bitmap) {

        ByteArrayOutputStream array=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte=array.toByteArray();
        String imagenString= Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
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
}
