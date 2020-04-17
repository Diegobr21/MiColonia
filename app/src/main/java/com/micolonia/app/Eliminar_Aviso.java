package com.micolonia.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import java.util.Map;

public class Eliminar_Aviso extends AppCompatActivity {

    private Button delete;
    private TextView texto_aviso;

    //Firebase
    private DocumentReference usuRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    public String current_tipo, postId, current_colonia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar__aviso);

        getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        //email_current_user=user.getEmail();
        usuRef=db.collection("usuarios").document(user.getUid());

        delete = findViewById(R.id.btn_eliminar_aviso);
        texto_aviso = findViewById(R.id.texto_aviso);

        //DELETING
        delete.setVisibility(View.GONE);
        gettingTipo();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingColonia();
            }
        });

        Aviso aviso = getIntent().getParcelableExtra("aviso");

        texto_aviso.setText(aviso.getName());
        postId = aviso.getId();

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
                            deletePost(current_colonia);


                        }else{
                            Toast.makeText(Eliminar_Aviso.this,"No existe el id_colonia del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Eliminar_Aviso.this,"Error!", Toast.LENGTH_SHORT).show();
                    }
                });
        return current_colonia;
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
                                delete.setVisibility(View.VISIBLE);
                            }

                        }else{
                            Toast.makeText(Eliminar_Aviso.this,"No existe el id_colonia del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Eliminar_Aviso.this,"Error!", Toast.LENGTH_SHORT).show();
                    }
                });
        return current_tipo;
    }

    private void deletePost(final String colonia){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Eliminar publicación");
        builder.setMessage("¿Estas seguro de eliminar esta publicación?");
        //listeners de los botones
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final ProgressDialog progressDialog = new ProgressDialog(Eliminar_Aviso.this);
                progressDialog.setMessage("Cargando");
                progressDialog.show();


                if (colonia.equals("1")){
                    db.collection("avisos_las_hadas").document(postId).delete();
                    progressDialog.dismiss();
                    backIni();

                }else if(colonia.equals("2")){
                    db.collection("avisos_mision_anahuac").document(postId).delete();
                    progressDialog.dismiss();
                    backIni();

                }
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

    private void backIni(){
        Intent intentb = new Intent(this, Inicio.class);
        startActivity(intentb);
        finish();
    }

}
