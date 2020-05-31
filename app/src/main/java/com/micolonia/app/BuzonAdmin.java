package com.micolonia.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuzonAdmin extends AppCompatActivity {

    RecyclerView recyclerViewbuzon;
    private List<Comentario> arraycomentarios;


    SwipeRefreshLayout swipeRefreshLayoutbuzon;
    Context context;
    //Firebase
    private DocumentReference usuRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    public String current_colonia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buzon_admin);
        getSupportActionBar().hide();

        swipeRefreshLayoutbuzon = findViewById(R.id.refresh_layout_buzon);

        swipeRefreshLayoutbuzon.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gettingColonia();
            }
        });


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        //email_current_user=user.getEmail();
        usuRef=db.collection("usuarios").document(user.getUid());

        //Recyclerview
        recyclerViewbuzon = findViewById(R.id.recyclerView_buzon_comentarios);

        recyclerViewbuzon.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerViewbuzon.setLayoutManager(layoutManager);
        RecyclerViewAdapter_comentarios adapter = new RecyclerViewAdapter_comentarios(arraycomentarios);



        gettingColonia();
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

                                addDataComments(current_colonia);
                            //Toast.makeText(BuzonAdmin.this, current_colonia, Toast.LENGTH_LONG).show();


                        }else{
                            Toast.makeText(BuzonAdmin.this,"No existe el id_colonia del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BuzonAdmin.this,"Error!", Toast.LENGTH_SHORT).show();
                    }
                });
        return current_colonia;
    }

    private void addDataComments(String colonia){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando");
        progressDialog.show();
        swipeRefreshLayoutbuzon.setRefreshing(false);

        if(colonia.equals("1")){
            db.collection("buzon_comentarios_las_hadas")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
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
                                recyclerViewbuzon.setAdapter(adapter_comentarios);

                            } else {
                                Toast.makeText(BuzonAdmin.this, "Error", Toast.LENGTH_SHORT).show();
                                swipeRefreshLayoutbuzon.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });
        }else if(colonia.equals("2")){

            db.collection("buzon_comentarios_mision_anahuac")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
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
                                recyclerViewbuzon.setAdapter(adapter_comentarios);
                            } else {
                                Toast.makeText(BuzonAdmin.this, "Error", Toast.LENGTH_SHORT).show();
                                swipeRefreshLayoutbuzon.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });
        }
    }


}
