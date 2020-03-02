package com.micolonia.app;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class seleccionar extends AppCompatActivity {
    public Button form_com, form_serv, form_venta, formato_colonia;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar);
        form_com=findViewById(R.id.aformatocom);
        form_serv=findViewById(R.id.aformatoserv);
        form_venta=findViewById(R.id.aformatoventa);

       


      form_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verformatocomida();
            }
        });

        form_serv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verformatoservicio();
            }
        });

        form_venta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verformatoventa();
            }
        });
        
    }
    public void verformatocomida(){
        Intent intentformatocom = new Intent(this, Formato_pub_comida.class);
        startActivity(intentformatocom);

    }

    public void verformatoservicio(){
        Intent intentformatoser = new Intent(this, Formato_pub_serv.class);
        startActivity(intentformatoser);
    }

    public void verformatoventa(){
        Intent intentformatoven = new Intent(this, Formato_pub_venta.class);
        startActivity(intentformatoven);
    }
}
