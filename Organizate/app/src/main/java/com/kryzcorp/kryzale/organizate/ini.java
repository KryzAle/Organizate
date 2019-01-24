package com.kryzcorp.kryzale.organizate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class ini extends AppCompatActivity {

    Button btnlogin , btnsignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ini);

        btnlogin=findViewById(R.id.btnIniciarsesion);
         btnsignup=findViewById(R.id.btnRegistrar);

         btnsignup.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent login=new Intent(ini.this, LoginActivity.class);
             }
         });
         btnsignup.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent sign=new Intent(ini.this,registro.class)
             }
         });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
