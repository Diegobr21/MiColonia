package com.micolonia.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Pago_Usu_Admin extends AppCompatActivity {
    private TextView nombre, correo, calle, num_calle, status;
    Button cambiar_status_p, cambiar_status_d ;
    private DocumentReference usuRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    public String current_colonia, colonia_a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago__usu__admin);

        //Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        usuRef=db.collection("usuarios").document(user.getUid());

        nombre = findViewById(R.id.nombre_usu_pago);
        correo = findViewById(R.id.correo_usu_pago);
        calle = findViewById(R.id.calle_usu_pago);
        num_calle = findViewById(R.id.num_calle_usu_pago);
        status = findViewById(R.id.status_usu_pago);

        cambiar_status_p = findViewById(R.id.btn_cambiar_status_pagado);
        cambiar_status_d = findViewById(R.id.btn_cambiar_status_deudor);
        final Usuario usuariom = getIntent().getParcelableExtra("usuario");

        nombre.setText(usuariom.getNombre());
        correo.setText(usuariom.getEmail().trim());
        calle.setText(usuariom.getNombre_calle());
        num_calle.setText(usuariom.getNum_calle().trim());
        status.setText(usuariom.getStatus());

        /*//Colores para pago o deuda
        status.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals("deudor")){
                    status.setTextColor(Color.RED);
                }
                else if (s.equals("pagado")){
                    status.setTextColor(Color.GREEN);
                }
                else{
                    status.setTextColor(Color.BLACK);
                }

            }});


*/
        cambiar_status_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingColonia(usuariom.getId(), 1);
                //String uid = usuariom.getId();
                //Toast.makeText(Pago_Usu_Admin.this, uid, Toast.LENGTH_LONG).show();
            }
        });
        cambiar_status_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingColonia(usuariom.getId(), 2);
            }
        });


    }
    public String gettingColonia(final String userid, final int cambio){
        usuRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()){
                            Map<String, Object> usu = documentSnapshot.getData();
                            current_colonia = usu.get("colonia").toString().trim();
                            //Toast.makeText(Perfil.this, current_tipo, Toast.LENGTH_SHORT).show();
                            if (current_colonia.equals("1")){
                                colonia_a="Las Hadas";

                            }
                            else if (current_colonia.equals("2")){
                                colonia_a="Mision Anahuac";
                            }

                            CambiarStatus(colonia_a, userid, cambio);


                        }else{
                            Toast.makeText(Pago_Usu_Admin.this,"No existe el id_colonia del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Pago_Usu_Admin.this,"Error!", Toast.LENGTH_SHORT).show();
                    }
                });
        return current_colonia;
    }

    public void CambiarStatus(String colonia, String userid, int cambio){
        //Toast.makeText(this, "Changing status for colonia"+ colonia, Toast.LENGTH_LONG).show();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando");
        progressDialog.show();
//TODO: IF CAMBIO A PAGADO
        if (cambio == 1){
            if (colonia.equals("Las Hadas")){

                db.collection("las_hadas").document(userid).update("status", "pagado");
                progressDialog.dismiss();
                Pago_Usu_Admin.this.status.setText("pagado");
                Toast.makeText(Pago_Usu_Admin.this, "Los cambios se verán reflejados en la lista de usuarios",
                        Toast.LENGTH_LONG).show();
            }
            else if (colonia.equals("Mision Anahuac")) {
                db.collection("mision_anahuac").document(userid).update("status", "pagado");
                progressDialog.dismiss();
                Pago_Usu_Admin.this.status.setText("pagado");
                Toast.makeText(Pago_Usu_Admin.this, "Los cambios se verán reflejados en la lista de usuarios",
                        Toast.LENGTH_LONG).show();
            }
        }//TODO: IF CAMBIO A DEUDOR
        else if(cambio == 2){
            if (colonia.equals("Las Hadas")){

                db.collection("las_hadas").document(userid).update("status", "deudor");
                progressDialog.dismiss();
                Pago_Usu_Admin.this.status.setText("deudor");
                Toast.makeText(Pago_Usu_Admin.this, "Los cambios se verán reflejados en la lista de usuarios",
                        Toast.LENGTH_LONG).show();
            }
            else if (colonia.equals("Mision Anahuac")) {
                db.collection("mision_anahuac").document(userid).update("status", "deudor");
                progressDialog.dismiss();
                Pago_Usu_Admin.this.status.setText("deudor");
                Toast.makeText(Pago_Usu_Admin.this, "Los cambios se verán reflejados en la lista de usuarios",
                        Toast.LENGTH_LONG).show();
            }

        }


    }
}
