package com.kryzcorp.kryzale.organizate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kryzcorp.kryzale.organizate.entidades.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        if (getFromSharedPreferencesLogin()){
            goMainScreen();
        }

        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.loginButton);

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
                            verificarFb2(id);
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location");
                request.setParameters(parameters);
                request.executeAsync();
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
    private void verificarFb2(final String id){
        String ip=getString(R.string.ip);
        String url=ip+"/wsJSONAunteticarFb.php?id_redsocial="+id;

        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                JSONArray json=response.optJSONArray("usuario");
                JSONObject jsonObject=null;

                try {
                    jsonObject=json.getJSONObject(0);

                    if (jsonObject.getInt("id")!=0){
                        saveLoginSharedPreferences(jsonObject.getInt("id"),jsonObject.getString("nombre"),true);
                        goMainScreen();
                    }else{
                        saveLoginSharedPreferences(0,"null",false);
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
    private boolean getFromSharedPreferencesLogin() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean logeado = sharedPref.getBoolean("logeado",false);
        return logeado;
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


                JSONArray json=response.optJSONArray("usuario");
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
    public void saveLoginSharedPreferences(int id,String nombre,boolean logeado){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("idUsuario",id );
        editor.putString("nombreUsuario",nombre);
        editor.putBoolean("logeado",logeado);
        editor.apply();
    }
}

