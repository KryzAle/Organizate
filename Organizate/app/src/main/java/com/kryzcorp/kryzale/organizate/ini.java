package com.kryzcorp.kryzale.organizate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;

public class ini extends AppCompatActivity {

    Button btnlogin , btnsignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ini);
        Button btnRegistrarse = findViewById(R.id.btnRegistrame);
        Button btnIncioSesion = findViewById(R.id.btnIniciaSesion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn){
            goMainScreen();
        }

    }
    public void LanzarLogin(View view){


        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);

    }
    public void LanzarRegistro(View view){


        Intent i = new Intent(this,registro.class);
        startActivity(i);

    }
    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
