package com.micolonia.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Perfil extends AppCompatActivity {
    private Button cerrarsesion, ayuda, contacto, publicar, avisopriv;
    private TextView title;
    private FirebaseAuth mAuth;
    private ImageButton back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        title=findViewById(R.id.textsaludo);

        back= findViewById(R.id.imgbtn_back);

        ayuda = (Button) findViewById(R.id.btn_ayuda);
        cerrarsesion = (Button) findViewById(R.id.btn_cerrarsesion);
        publicar = (Button) findViewById(R.id.btn_a_publicar);
        contacto = (Button) findViewById(R.id.btn_contacto);


      title.setText("Perfil: "+ user.getEmail());

        avisopriv = (Button) findViewById(R.id.btn_avisodeprivacidad);


        /*Obtener el valor del string del intent, y creando diferentes intents para regresar a la actividad
        desde donde se ingreso a Perfil
        * */
        final Intent intentremitente= this.getIntent();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backto(intentremitente);
            }
        });

        cerrarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoalerta();
            }
        });


        ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ayuda();
            }
        });

        contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                contacto();
            }
        });

        publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llevar_a_publicar();
            }
        });

        avisopriv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                avisoprivacidad();
            }
        });
    }

    private void fueradeapp() {
        FirebaseAuth.getInstance().signOut();

        finish();
        Intent intent = new Intent(this, InicioSesion.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }

    public void backto(Intent intent){
        if(intent != null){
            String remitente= intent.getExtras().getString("Remitente");
            if(remitente.equals("DesdeInicio")){
                Intent intentback = new Intent(this, Inicio.class);
                startActivity(intentback);
            }
            else if(remitente.equals("DesdeComida")){
                Intent intentback = new Intent(this, seccion_comida.class);
                startActivity(intentback);
            }
            else if(remitente.equals("DesdeServicios")){
                Intent intentback = new Intent(this, seccion_servicios.class);
                startActivity(intentback);
            }
            else if(remitente.equals("DesdeVentas")){
                Intent intentback = new Intent(this, seccion_ventas.class);
                startActivity(intentback);
            }
        }

    }


    private void llevar_a_publicar() {
        Intent intent2 = new Intent(this, Publicar.class);

        startActivity(intent2);
    }

    private void contacto() {
        Intent intent3 = new Intent(this, Contacto.class);

        startActivity(intent3);
    }

    private void avisoprivacidad() {
        Intent intent4 = new Intent(this, AvisoPrivacidad.class);

        startActivity(intent4);
    }

    private void ayuda() {

        Intent intent5 = new Intent(this, Ayuda.class);

        startActivity(intent5);
    }

    private void dialogoalerta() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Cierre de Sesión");
        builder.setMessage("¿Quieres cerrar sesión?");
        //listeners de los botones
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fueradeapp();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    //TOAST AYUDA---> Interfaz estatica con instrucciones
    public void simpleToast(View view) {
        Toast.makeText(this, "Mas tarde se proporcionaran las instrucciones a seguir :)", Toast.LENGTH_SHORT).show();
    };
}