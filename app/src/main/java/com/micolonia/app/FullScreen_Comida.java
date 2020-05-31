package com.micolonia.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.zolad.zoominimageview.ZoomInImageView;

public class FullScreen_Comida extends AppCompatActivity {

    //private ImageView fullcomida;
    private ZoomInImageView fullcomida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen__comida);

        fullcomida = findViewById(R.id.fullscreen_comida);

        Bundle b = getIntent().getExtras();
        String imagen = b.getString("imagen");

        if (imagen != null){
            Glide.with(this).load(imagen).into(fullcomida);
        }else{
            Toast.makeText(this, "No se pudo cargar la imagen", Toast.LENGTH_LONG).show();
        }

    }
}
