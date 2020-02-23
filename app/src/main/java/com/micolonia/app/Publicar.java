package com.micolonia.app;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Publicar extends AppCompatActivity {
    private Button contacto, siguiente;
    TextView se_parte, texto, contactanos, daclick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar);
        contacto = findViewById(R.id.btn_contacto1);
        siguiente = findViewById(R.id.btn_sig_publicar);
        se_parte = findViewById(R.id.tituloPublicar);
        //texto=(TextView)findViewById(R.id.inicio_publicar);
        contactanos = (TextView)findViewById(R.id.conta);
        daclick = (TextView)findViewById(R.id.daclick);

      //
       // texto.setText(Html.fromHtml(textolargo), TextView.BufferType.SPANNABLE);

        //texto.setText("Publicar tiene un costo, ya seas un negocio de comida o de algun servicio," +
             //   " si ya te pusiste en contacto con nosotros, da click en siguiente, y llena el formato, si no, " +
              //  " puedes llenar el formato y estar en lista de espera(max 2-3dias),
        //  o bien, dar click en contacto para comunicarte con nos");

        contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vercontacto();
            }
        });
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verformato();
            }
        });
    }

    public void vercontacto(){
        Intent intentcontacto = new Intent(this, Contacto.class);
                startActivity(intentcontacto);
    }

    public void verformato(){
        Intent intentformato = new Intent(this, seleccionar.class);
        startActivity(intentformato);

    }
}