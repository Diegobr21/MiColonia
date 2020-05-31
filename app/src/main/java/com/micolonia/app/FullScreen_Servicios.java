package com.micolonia.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zolad.zoominimageview.ZoomInImageView;

public class FullScreen_Servicios extends AppCompatActivity {

    //private ImageView fullservicios;
    private ZoomInImageView fullservicios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen__servicios);

        fullservicios = findViewById(R.id.fullscreen_servicios);

        Bundle b = getIntent().getExtras();
        String imagen = b.getString("imagen");

        if (imagen != null){
            Glide.with(this).load(imagen).into(fullservicios);
        }else{
            Toast.makeText(this, "No se pudo cargar la imagen", Toast.LENGTH_LONG).show();
        }
    }
}
