package com.kryzcorp.kryzale.organizate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kryzcorp.kryzale.organizate.entidades.Usuario;
import com.kryzcorp.kryzale.organizate.entidades.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class LoginActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    EditText txtUser;
    EditText txtPass;
    ProgressDialog pDialog;
    //RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;//SE MODIFICA



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUser = (EditText) findViewById(R.id.txtUsuario);
        txtPass = (EditText) findViewById(R.id.txtContra);
        //verificar que el usuario esté logged

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn){
            goMainScreen();
        }

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.loginButton);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (response.getError() != null) {
                            Toast.makeText(getApplicationContext(), response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                            return;
                        }
                        try {
                            String id = object.getString("id");
                            verificarFb(id);
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location");
                request.setParameters(parameters);
                request.executeAsync();

                goMainScreen();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verificarFb(final String id) {

        String ip=getString(R.string.ip);
        String url=ip+"/wsJSONLoginLocal.php?usuario="+id+"&password="+id;

        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                JSONArray json=response.optJSONArray("user");
                JSONObject jsonObject=null;

                try {
                    jsonObject=json.getJSONObject(0);

                    if (jsonObject.getInt("id")!=0){
                        saveLoginSharedPreferences(jsonObject.getInt("id"),jsonObject.getString("nombre"),true);
                    }else{
                        registrarFb(id);
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();

                Log.d("ERROR: ", error.toString());
            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(jsonObjectRequest);
    }

    private void registrarFb(final String idFb) {


        String ip=getString(R.string.ip);

        String url=ip+"/wsJSONRegistroRedSocial.php?";

        stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                if (response.trim().equalsIgnoreCase("registra")){
                    Toast.makeText(getApplicationContext(),"Se ha registrado con exito",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"No se ha registrado ",Toast.LENGTH_SHORT).show();
                    Log.i("RESPUESTA: ",""+response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String idRed=idFb;


                Map<String,String> parametros=new HashMap<>();
                parametros.put("id_redsocial",idRed);

                return parametros;
            }
        };
        //request.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(stringRequest);
    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void logearLocal(View view) {
       webServiceLogear();

    }

    private int getFromSharedPreferences(String idUsuario) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        int id = sharedPref.getInt(idUsuario,0);
        return id;
    }

    private void webServiceLogear() {
        pDialog=new ProgressDialog(this);
        pDialog.setMessage("Cargando...");
        pDialog.show();
        String ip=getString(R.string.ip);
        String url=ip+"/wsJSONLoginLocal.php?usuario="+txtUser.getText().toString() + "&password="+txtPass.getText().toString();

        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.hide();


                JSONArray json=response.optJSONArray("user");
                JSONObject jsonObject=null;

                try {
                    jsonObject=json.getJSONObject(0);

                        if (jsonObject.getInt("id")!=0){
                            saveLoginSharedPreferences(jsonObject.getInt("id"),jsonObject.getString("nombre"),true);
                            goMainScreen();
                        }else{
                            saveLoginSharedPreferences(jsonObject.getInt("id"),jsonObject.getString("nombre"),false);
                            Toast.makeText(getApplicationContext(),"Datos Incorrectos",Toast.LENGTH_LONG).show();
                        }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
                pDialog.hide();
                Log.d("ERROR: ", error.toString());
            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(jsonObjectRequest);
    }
    private void saveLoginSharedPreferences(int id,String nombre,boolean logeado){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("idUsuario",id );
        editor.putString("nombreUsuario",nombre);
        editor.putBoolean("logeado",logeado);
        editor.apply();
    }
}

