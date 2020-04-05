package com.micolonia.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class Imagen_venta extends AppCompatActivity {
    public static final int REQUEST_CALL = 1;
    private ImageView img_tel;
    private TextView descgeneral;
    private TextView costo;
    private TextView titulo;
    private TextView contacto;
    private ImageView imagenventa;
    private ImageView wha;
    private ImageView back;
    Venta venta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen_venta);
        getSupportActionBar().hide();

       back=findViewById(R.id.backbtnven);
        img_tel=findViewById(R.id.img_tel);
        wha= findViewById(R.id.wha);
        costo=findViewById(R.id.costo_venta);
        descgeneral=findViewById(R.id.desc_venta);
        titulo=findViewById(R.id.titulo_venta);
        imagenventa=findViewById(R.id.imagen_pub_venta);
        contacto=findViewById(R.id.contacto_venta);

        Venta venta = getIntent().getParcelableExtra("venta");

        titulo.setText(venta.getName());
        descgeneral.setText(venta.getDescripcion());
        costo.setText("$ " + venta.getCosto());
        contacto.setText(": "+venta.getTelefono());

        wha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsapp(v);
            }
        });

        img_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamada();
            }
        });

        contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamada();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backven();
            }
        });



        if(venta.getImagen()!=null){
        Glide.with(this).load(venta.getImagen()).into(imagenventa);
        }
        else if(venta.getTipo()=="2"){
            imagenventa.setImageResource(R.drawable.casa);
        }else{
            imagenventa.setImageResource(R.drawable.logonegropng);
        }

    }

    private void backven(){
        Intent intentb = new Intent(this, seccion_ventas.class);
        startActivity(intentb);
    }

    public void whatsapp(View view){

        Venta venta = getIntent().getParcelableExtra("venta");
        String numero = venta.getTelefono().trim();
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
        Venta venta = getIntent().getParcelableExtra("venta");
        if(ContextCompat.checkSelfPermission(Imagen_venta.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Imagen_venta.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }else{
            String telefono =venta.getTelefono().trim();
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
