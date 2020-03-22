package com.micolonia.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class Perfil extends AppCompatActivity {
    private Button cerrarsesion, ayuda, contacto, publicar, avisopriv, generar_qr;
    private TextView title;
    private ImageButton back, admin;
    private DocumentReference usuRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
   // private String email_current_user;
    public String current_tipo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        getSupportActionBar().hide();
        //Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        //email_current_user=user.getEmail();
        usuRef=db.collection("usuarios").document(user.getUid());

        title=findViewById(R.id.textsaludo);
        back= findViewById(R.id.imgbtn_back);
        admin=findViewById(R.id.btn_admin);

        generar_qr = findViewById(R.id.generar_qr);
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

        //ADMIN STUFF
        admin.setVisibility(View.GONE);
        gettingTipo();
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Perfil.this, "Welcome admin", Toast.LENGTH_LONG).show();
                toAdminLayout();
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



    public String gettingTipo(){
        usuRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()){
                            Map<String, Object> usu = documentSnapshot.getData();
                            current_tipo = usu.get("tipo").toString().trim();
                            //Toast.makeText(Perfil.this, current_tipo, Toast.LENGTH_SHORT).show();
                            if (current_tipo.equals("2")){
                                admin.setVisibility(View.VISIBLE);
                            }

                        }else{
                            Toast.makeText(Perfil.this,"No existe el id_colonia del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Perfil.this,"Error!", Toast.LENGTH_SHORT).show();
                    }
                });
        return current_tipo;
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

    private void toAdminLayout(){
        Intent intentadmin = new Intent(this, Admin.class);

        startActivity(intentadmin);
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