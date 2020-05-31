package com.micolonia.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Buzon_Comentarios extends AppCompatActivity {

    EditText comentario_b;
    TextView publicar;
    //Firebase
    private DocumentReference usuRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    public String  current_colonia, userId, username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buzon__comentarios);
        getSupportActionBar().hide();


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        //email_current_user=user.getEmail();
        usuRef=db.collection("usuarios").document(user.getUid());
        userId = mAuth.getCurrentUser().getUid();

        comentario_b = findViewById(R.id.txt_comentario);
        publicar = findViewById(R.id.publicar_comentario_b);

        //  Publicar comentario
        publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!comentario_b.getText().toString().trim().isEmpty()){

                    gettingColonia(1);

                }else{
                    Toast.makeText(Buzon_Comentarios.this, "Comentario vacío", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public String gettingColonia(final int opcion){
        usuRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()){
                            Map<String, Object> usu = documentSnapshot.getData();
                            current_colonia = usu.get("colonia").toString().trim();
                            username = usu.get("nombre").toString().trim();
                            //Toast.makeText(Perfil.this, current_tipo, Toast.LENGTH_SHORT).show();
                            if(opcion == 1){
                                addComment(current_colonia, username);
                            }

                        }else{
                            Toast.makeText(Buzon_Comentarios.this,"No existe el id_colonia del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Buzon_Comentarios.this,"Error!", Toast.LENGTH_SHORT).show();
                    }
                });
        return current_colonia;
    }


    private void addComment(String colonia, String username){


        HashMap<String, Object> data = new HashMap<>();
        data.put("timestamp", System.currentTimeMillis());
        data.put("post_id", "buzon");
        data.put("user_id", userId);
        data.put("comentario", comentario_b.getText().toString());
        data.put("nombre_usu", username);



        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando");
        dialog.show();

        if(colonia.equals("1")){

            db.collection("buzon_comentarios_las_hadas")
                    .add(data)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()) {

                                comentario_b.getText().clear();
                                Toast.makeText(Buzon_Comentarios.this,
                                        "Comentario enviado a jefe de colonos",
                                        Toast.LENGTH_LONG).show();


                            }else{
                                Toast.makeText(Buzon_Comentarios.this,
                                        "Algo no fue bien, intente de nuevo más tarde",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }else if(colonia.equals("2")){
            db.collection("buzon_comentarios_las_hadas")
                    .add(data)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()) {

                                comentario_b.getText().clear();
                                Toast.makeText(Buzon_Comentarios.this,
                                        "Comentario enviado",
                                        Toast.LENGTH_LONG).show();


                            }else{
                                Toast.makeText(Buzon_Comentarios.this,
                                        "Algo no fue bien, intente de nuevo más tarde",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }

    }

}
