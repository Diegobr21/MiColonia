package com.micolonia.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Map;

public class LectorQR extends AppCompatActivity {
    private Button btnescanear_qr, btnverificar_qr;
    private TextView txtqr, txtnom, txtcalle, txtnum, txtfecha;
    //Firebase
    private DocumentReference usuRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    public String current_colonia, colonia_a, nombreusu, calleusu, numcalleusu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector_qr);

        //Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        //email_current_user=user.getEmail();
        usuRef=db.collection("usuarios").document(user.getUid());

        btnescanear_qr = findViewById(R.id.btn_escanear_qr);
        btnverificar_qr = findViewById(R.id.btn_verifycode);
        txtqr = findViewById(R.id.txt_qr);
        txtcalle = findViewById(R.id.calle_verify);
        txtnom = findViewById(R.id.nom_verify);
        txtnum = findViewById(R.id.numcalle_verify);
        txtfecha = findViewById(R.id.fecha_validez);

        btnescanear_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(LectorQR.this).initiateScan();
            }
        });

        btnverificar_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingColonia();
            }
        });
    }

    public String gettingColonia(){
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
                            //usercolonia.setText("Admin colonia: " + colonia_a);
                            verificarUID(current_colonia);

                        }else{
                            Toast.makeText(LectorQR.this,"No existe el id_colonia del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LectorQR.this,"Error!", Toast.LENGTH_SHORT).show();
                    }
                });
        return current_colonia;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if(result.getContents() != null){
                txtqr.setText(result.getContents());
            }else{
                Toast.makeText(this, "Error al escanear", Toast.LENGTH_SHORT).show();
                txtqr.setText("");
            }
        }
    }

    private void verificarUID(final String colonia){


        //final String uid = txtqr.getText().toString().trim();
        String qr = txtqr.getText().toString().trim();
        if (qr.length() > 0){
            String[] parts = qr.split(" ");
            final String uid = parts[0];
            final String fecha = parts[1];
            if (fecha.length() > 0){
                txtfecha.setText(fecha);
            }

            //Toast.makeText(this, uid2, Toast.LENGTH_LONG).show();
            if (uid.isEmpty()){
                Toast.makeText(this, "No se ha escaneado codigo", Toast.LENGTH_LONG).show();

            }else{
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Cargando");
                progressDialog.show();

                db.collection("usuarios").document(uid).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()){
                                    Map<String, Object> usuario = documentSnapshot.getData();

                                    nombreusu = usuario.get("nombre").toString().trim();
                                    calleusu = usuario.get("nombre_calle").toString().trim();
                                    numcalleusu = usuario.get("num_calle").toString().trim();

                                    //if(nombreusu.isEmpty() || calleusu.isEmpty() || numcalleusu.isEmpty()){}
                                    txtcalle.setText("Calle " + calleusu);
                                    txtnom.setText(nombreusu);
                                    txtnum.setText("#" + numcalleusu);
                                    progressDialog.dismiss();


                                }else{
                                    Toast.makeText(LectorQR.this,"Error, no hubo coincidencias con el codigo",
                                            Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LectorQR.this,"Error, no hubo coincidencias con el codigo",
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }
                        });


            }
        }else{
            Toast.makeText(this, "No se ha escaneado codigo", Toast.LENGTH_LONG).show();
        }

    }
}
