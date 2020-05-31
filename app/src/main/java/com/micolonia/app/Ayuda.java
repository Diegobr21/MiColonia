package com.micolonia.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Ayuda extends AppCompatActivity {
    public ImageButton derecha, izquierda;
    ImageView aviso_privacidad;
   // Fragment fragment= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);
        getSupportActionBar().hide();

       // setTitle("Sección ayuda");


        derecha=findViewById(R.id.derecha);
        izquierda=findViewById(R.id.izq);
        aviso_privacidad = findViewById(R.id.btn_info_aviso_privacidad);

        aviso_privacidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a_avisoprivacidad();
            }
        });

        //Fragmento
        fragmentayuda1();

        derecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentayuda2();
            }
        });

        izquierda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentayuda1();
            }
        });




    }

    private void a_avisoprivacidad() {
        Intent intent4 = new Intent(this, AvisoPrivacidad.class);

        startActivity(intent4);
    }

    private void fragmentayuda1(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction  fragmentTransaction = fragmentManager.beginTransaction();
        Fragm_ayuda1 fragm_ayuda1 = new Fragm_ayuda1();
        fragmentTransaction.replace(R.id.fragment_container, fragm_ayuda1);
        fragmentTransaction.commit();
    }

    private void fragmentayuda2(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction  fragmentTransaction = fragmentManager.beginTransaction();
        Fragm_ayuda2 fragm_ayuda2 = new Fragm_ayuda2();
        fragmentTransaction.replace(R.id.fragment_container, fragm_ayuda2);
        fragmentTransaction.commit();
    }
   /* public void fragmentosayuda(View view){
        switch (view.getId()){
            case R.id.derecha:
                fragment = new Fragm_ayuda2();
                loadFragment(fragment);
                break;
            case R.id.izq:
                fragment=new Fragm_ayuda2();
                fragment= new Fragm_ayuda1();
                loadFragment(fragment);
                break;
    }


    }
    */


  /* private void setFragment(){
       Fragment fragment;
       fragment = new Fragm_ayuda1();
       if(fragment instanceof Fragm_ayuda1){
           fragment = new Fragm_ayuda2();
       }
       else {
           fragment = new Fragm_ayuda1();
       }

       FragmentManager manager = getSupportFragmentManager();
       FragmentTransaction transaction = manager.beginTransaction();
       transaction.replace(R.id.fragment_ayuda, fragment);
       transaction.commit();
   }
*/
}
