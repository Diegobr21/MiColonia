package com.micolonia.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Eliminar_Aviso extends AppCompatActivity {

    private Button delete;
    private TextView texto_aviso, publicar_com, c;
    private RelativeLayout add_coment;
    private RecyclerView recyclerView_comentarios;
    private EditText comentario;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Comentario> arraycomentarios;
    private List<String> comments;
    Context context;


    //Firebase
    private DocumentReference usuRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    public String current_tipo, postId, current_colonia, userId, username, comentarios_activos;
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
        userId = mAuth.getCurrentUser().getUid();

        swipeRefreshLayout = findViewById(R.id.refresh_layout_avisos);
        c = findViewById(R.id.textView12);
        delete = findViewById(R.id.btn_eliminar_aviso);
        texto_aviso = findViewById(R.id.texto_aviso);
        add_coment = findViewById(R.id.bottom_comment);
        comentario = findViewById(R.id.txt_comentario_aviso);
        publicar_com = findViewById(R.id.publicar_comentario);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gettingColonia(2);
            }
        });

        //  Publicar comentario
        publicar_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!comentario.getText().toString().trim().isEmpty()){

                    gettingColonia(1);

                }else{
                    Toast.makeText(Eliminar_Aviso.this, "Comentario vacío", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //RECYCLERVIEW
        recyclerView_comentarios = findViewById(R.id.recyclerView_comentarios_aviso);
        recyclerView_comentarios.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView_comentarios.setLayoutManager(layoutManager);
        RecyclerViewAdapter_comentarios adapter = new RecyclerViewAdapter_comentarios(arraycomentarios);

        //DELETING
        delete.setVisibility(View.GONE);
        gettingTipo();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingColonia(0);
            }
        });

        Aviso aviso = getIntent().getParcelableExtra("aviso");

        texto_aviso.setText(aviso.getName());
        postId = aviso.getId();
        comentarios_activos = aviso.getComentarios();

        //Toast.makeText(this, comentarios_activos, Toast.LENGTH_SHORT).show();
        if (comentarios_activos.equals("no")){
            add_coment.setVisibility(View.GONE);
            c.setVisibility(View.GONE);
        }else if(comentarios_activos.equals("si")){
            gettingColonia(2);
        }

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
                            if(opcion == 0){
                                deletePost(current_colonia);
                            }else if(opcion == 1){
                                addComment(current_colonia, username);
                            }else if(opcion == 2){
                                addDataComments(current_colonia);
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

    private void addDataComments(String colonia){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando");
        progressDialog.show();
        swipeRefreshLayout.setRefreshing(false);

        if(colonia.equals("1")){
            db.collection("comentarios_avisos_las_hadas")
                   .whereEqualTo("post_id", postId)
                   .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arraycomentarios = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    Comentario comentario = doc.toObject(Comentario.class);
                                    arraycomentarios.add(comentario);
                                }
                                RecyclerViewAdapter_comentarios adapter_comentarios = new RecyclerViewAdapter_comentarios(arraycomentarios);
                                recyclerView_comentarios.setAdapter(adapter_comentarios);
                            } else {
                                Toast.makeText(Eliminar_Aviso.this, "Error", Toast.LENGTH_SHORT).show();
                                swipeRefreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });
        }else if(colonia.equals("2")){

            db.collection("comentarios_avisos_mision_anahuac")
                    .whereEqualTo("post_id", postId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arraycomentarios = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    Comentario comentario = doc.toObject(Comentario.class);
                                    arraycomentarios.add(comentario);
                                }
                                RecyclerViewAdapter_comentarios adapter_comentarios = new RecyclerViewAdapter_comentarios(arraycomentarios);
                                recyclerView_comentarios.setAdapter(adapter_comentarios);
                            } else {
                                Toast.makeText(Eliminar_Aviso.this, "Error", Toast.LENGTH_SHORT).show();
                                swipeRefreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });
        }
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
                    if(comentarios_activos.equals("si")){
                        db.collection("comentarios_avisos_las_hadas").whereEqualTo("post_id", postId).get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (DocumentSnapshot doc : task.getResult()) {
                                            doc.getReference().delete();

                                    }
                                    }
                                });
                    }

                    progressDialog.dismiss();
                    backIni();

                }else if(colonia.equals("2")){
                    db.collection("avisos_mision_anahuac").document(postId).delete();

                    if(comentarios_activos.equals("si")){
                        db.collection("comentarios_avisos_mision_anahuac").whereEqualTo("post_id", postId).get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (DocumentSnapshot doc : task.getResult()) {
                                            doc.getReference().delete();

                                        }
                                    }
                                });
                    }

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

    private void addComment(String colonia, String username){



        HashMap<String, Object> data = new HashMap<>();
        data.put("timestamp", System.currentTimeMillis());
        data.put("post_id", postId);
        data.put("user_id", userId);
        data.put("comentario", comentario.getText().toString());
        data.put("nombre_usu", username);



        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando");
        dialog.show();

        if(colonia.equals("1")){

            db.collection("comentarios_avisos_las_hadas")
                    .add(data)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    dialog.dismiss();
                    if (task.isSuccessful()) {

                        comentario.getText().clear();
                        Toast.makeText(Eliminar_Aviso.this,
                                "Comentario enviado",
                                Toast.LENGTH_LONG).show();


                    }else{
                            Toast.makeText(Eliminar_Aviso.this,
                                    "Algo no fue bien, intente de nuevo más tarde",
                                    Toast.LENGTH_LONG).show();
                    }
                    }
        });

        }else if(colonia.equals("2")){
            db.collection("comentarios_avisos_las_hadas")
                    .add(data)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()) {

                                comentario.getText().clear();
                                Toast.makeText(Eliminar_Aviso.this,
                                        "Comentario enviado",
                                        Toast.LENGTH_LONG).show();


                            }else{
                                Toast.makeText(Eliminar_Aviso.this,
                                        "Algo no fue bien, intente de nuevo más tarde",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }

    }

    private void backIni(){
        Intent intentb = new Intent(this, Inicio.class);
        startActivity(intentb);
        finish();
    }


                    }
