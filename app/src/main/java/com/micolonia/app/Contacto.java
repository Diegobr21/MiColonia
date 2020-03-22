package com.micolonia.app;

import android.Manifest;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Contacto extends AppCompatActivity {
    public static final int REQUEST_CALL = 1;
    private Button btnregresar;
    TextView telefono1,telefono2, txtgmail;
    ImageView tel, wha, gmail;
    String stel1="+5218116802827";
    String stel2="+5218181130263";
    String scorreo="jdiegobrana@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);
        getSupportActionBar().hide();
        btnregresar = findViewById(R.id.btn_regresar);
        tel = findViewById(R.id.img_tel);
        wha = findViewById(R.id.wha);
        telefono1= findViewById(R.id.txt_phone);
        telefono2= findViewById(R.id.txt_phone2);
        gmail=findViewById(R.id.gmail);
        txtgmail=findViewById(R.id.text_gmail);



        txtgmail.setText(scorreo);
        telefono1.setText(stel1);
        telefono2.setText(stel2);

        btnregresar.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    regresar_inicio();
                }
            }
        );

        txtgmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gmail(v);
            }
        });

        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gmail(v);
            }
        });

        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamada();
            }
        });

        telefono1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamada();
            }
        });

        telefono2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamada2();
            }
        });

        wha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsapp(v);
            }
        });
    }

    public void regresar_inicio(){
        Intent intentini = new Intent(this, Inicio.class);
        startActivity(intentini);
    }

    public void gmail(View view){
        Toast.makeText(this, "Da click en gmail", Toast.LENGTH_LONG).show();
    }

    public void whatsapp(View view) {
        String numero = stel1;
        String text ="Hola!";


        try {


            Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+numero +"&text="+text));
            startActivity(intent);

        } catch (Exception e){
            e.printStackTrace();

        }

    }
    private void llamada(){

        if(ContextCompat.checkSelfPermission(Contacto.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Contacto.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }else{
            String telefono =stel1;
            String dial = "tel: "+telefono;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }
    private void llamada2(){

        if(ContextCompat.checkSelfPermission(Contacto.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Contacto.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }else{
            String telefono =stel2;
            String dial = "tel: "+telefono;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CALL){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                llamada();
            }else {
                Toast.makeText(this, "No se ha concedido el permiso de llamada", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
