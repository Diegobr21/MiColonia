package com.micolonia.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class Admin extends AppCompatActivity implements RecyclerViewAdapter_usuarios.OnUsuarioListener {
private TextView usercolonia;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView_usu;
    ImageButton busqueda_usu;
    Button morosos, alpha;
    EditText edt_usuario_buscar;
    FloatingActionButton publicar_admin;
    private DocumentReference usuRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    //private Map<String, Object> current_admin;
    private List<Usuario> arrayusuarios;
    public String current_colonia, colonia_a;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        publicar_admin = findViewById(R.id.floatingpublicar_admin);
        edt_usuario_buscar= findViewById(R.id.busqueda_usu);
        busqueda_usu = findViewById(R.id.btn_filtros_admin);
        morosos = findViewById(R.id.filter_deudas);
        alpha = findViewById(R.id.filter_az);
        usercolonia=findViewById(R.id.user_admin_txt);
        refreshLayout=findViewById(R.id.refresh_layout_ADMIN);
        //recyclerview
        recyclerView_usu= findViewById(R.id.recyclerView_usuarios);
        recyclerView_usu.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView_usu.setLayoutManager(layoutManager);
        RecyclerViewAdapter_usuarios adapter = new RecyclerViewAdapter_usuarios(arrayusuarios, this);
        //Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        //email_current_user=user.getEmail();
        usuRef=db.collection("usuarios").document(user.getUid());


        gettingColonia();


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gettingColonia();
            }
        });
        busqueda_usu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usuario = edt_usuario_buscar.getText().toString().trim();
                showUser(usuario);

            }
        });

        morosos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMorosos();
            }
        });

        alpha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCallesA_Z();
            }
        });

        publicar_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicar_Admin();
            }
        });
    }

    private void publicar_Admin(){
        Intent intent_a = new Intent(this, Aviso_Admin.class );
        //colonia_a on intent
        intent_a.putExtra("colonia", colonia_a);
        startActivity(intent_a);

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
                            usercolonia.setText("Admin colonia: " + colonia_a);
                            addUsu(current_colonia);

                        }else{
                            Toast.makeText(Admin.this,"No existe el id_colonia del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Admin.this,"Error!", Toast.LENGTH_SHORT).show();
                    }
                });
        return current_colonia;
    }

    //TODO: Show specific user with the words on the edt_txt
    public void showUser(String username){
        //Toast.makeText(Admin.this, username, Toast.LENGTH_LONG).show();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando");
        progressDialog.show();

        //TODO: IF LAS HADAS
        if (colonia_a.equals("Las Hadas")){
            db.collection("las_hadas")
                    .whereEqualTo("nombre", username)
                    //.orderBy("num_calle", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arrayusuarios = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    Usuario usuario = doc.toObject(Usuario.class);
                                    usuario.id= doc.getId();
                                    arrayusuarios.add(usuario);
                                }

                                RecyclerViewAdapter_usuarios adapter =
                                        new RecyclerViewAdapter_usuarios(arrayusuarios,
                                                Admin.this);
                                recyclerView_usu.setAdapter(adapter);
                                refreshLayout.setRefreshing(false);
                            } else {
                                Toast.makeText(Admin.this, "Error", Toast.LENGTH_SHORT).show();
                                refreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });
        }else if (colonia_a.equals("Mision Anahuac")){
            db.collection("mision_anahuac")
                    .whereEqualTo("nombre", username)
                    //.orderBy("nombre", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arrayusuarios = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    Usuario usuario = doc.toObject(Usuario.class);
                                    usuario.id= doc.getId();
                                    arrayusuarios.add(usuario);
                                }

                                RecyclerViewAdapter_usuarios adapter =
                                        new RecyclerViewAdapter_usuarios(
                                                arrayusuarios, Admin.this);
                                recyclerView_usu.setAdapter(adapter);
                                refreshLayout.setRefreshing(false);
                            } else {
                                Toast.makeText(Admin.this, "Error", Toast.LENGTH_SHORT).show();
                                refreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });
        }


    }

    private void showMorosos(){
        Toast.makeText(Admin.this, "Lista de Morosos", Toast.LENGTH_LONG).show();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando");
        progressDialog.show();

        //TODO: IF LAS HADAS
        if (colonia_a.equals("Las Hadas")){
            db.collection("las_hadas")
                    .whereEqualTo("status", "deudor")
                    //.orderBy("num_calle", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arrayusuarios = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    Usuario usuario = doc.toObject(Usuario.class);
                                    usuario.id= doc.getId();
                                    arrayusuarios.add(usuario);
                                }

                                RecyclerViewAdapter_usuarios adapter = new RecyclerViewAdapter_usuarios(arrayusuarios, Admin.this);
                                recyclerView_usu.setAdapter(adapter);
                                refreshLayout.setRefreshing(false);
                            } else {
                                Toast.makeText(Admin.this, "Error", Toast.LENGTH_SHORT).show();
                                refreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });
        }else if (colonia_a.equals("Mision Anahuac")){
            db.collection("mision_anahuac")
                    .whereEqualTo("status", "deudor")
                    //.orderBy("nombre", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arrayusuarios = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    Usuario usuario = doc.toObject(Usuario.class);
                                    usuario.id= doc.getId();
                                    arrayusuarios.add(usuario);
                                }

                                RecyclerViewAdapter_usuarios adapter = new RecyclerViewAdapter_usuarios(arrayusuarios, Admin.this);
                                recyclerView_usu.setAdapter(adapter);
                                refreshLayout.setRefreshing(false);
                            } else {
                                Toast.makeText(Admin.this, "Error", Toast.LENGTH_SHORT).show();
                                refreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });
        }


    }
    private void showCallesA_Z(){
        Toast.makeText(Admin.this, "Alfabeticamente ordenados", Toast.LENGTH_LONG).show();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando");
        progressDialog.show();

        //TODO: IF LAS HADAS
        if (colonia_a.equals("Las Hadas")){
            db.collection("las_hadas")
                    .orderBy("nombre_calle", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arrayusuarios = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    Usuario usuario = doc.toObject(Usuario.class);
                                    usuario.id= doc.getId();
                                    arrayusuarios.add(usuario);
                                }

                                RecyclerViewAdapter_usuarios adapter = new RecyclerViewAdapter_usuarios(arrayusuarios, Admin.this);
                                recyclerView_usu.setAdapter(adapter);
                                refreshLayout.setRefreshing(false);
                            } else {
                                Toast.makeText(Admin.this, "Error", Toast.LENGTH_SHORT).show();
                                refreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });
        }else if (colonia_a.equals("Mision Anahuac")){
            db.collection("mision_anahuac")
                    .orderBy("nombre", Query.Direction.ASCENDING)
                    //.orderBy("nombre", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arrayusuarios = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    Usuario usuario = doc.toObject(Usuario.class);
                                    usuario.id= doc.getId();
                                    arrayusuarios.add(usuario);
                                }

                                RecyclerViewAdapter_usuarios adapter = new RecyclerViewAdapter_usuarios(arrayusuarios, Admin.this);
                                recyclerView_usu.setAdapter(adapter);
                                refreshLayout.setRefreshing(false);
                            } else {
                                Toast.makeText(Admin.this, "Error", Toast.LENGTH_SHORT).show();
                                refreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });
        }

    }

    private void addUsu(String colonia) {
        // Los datos en Firestore deben tener la estructura de modelo en JSON.
        // Usando el objeto Aviso, su estructura es así:
        // {name: "Nombre", logoId: 1}
        // logoID equivale a dos valores, usaremos números y en la app
        // evaluamos esos números para colocar la imagen correcta.

        //Agarramos el id_colonia del usuario
        //Toast.makeText(Inicio.this, "Num "+gettingColonia(), Toast.LENGTH_SHORT).show();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando");
        progressDialog.show();
        //refreshLayout.setRefreshing(false);
        //TODO: IF LAS HADAS
        if (colonia.equals("1")){
            db.collection("las_hadas")
                    //.whereEqualTo("colonia", gettingColonia())
                    .orderBy("nombre", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arrayusuarios = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    Usuario usuario = doc.toObject(Usuario.class);
                                    usuario.id= doc.getId();
                                    arrayusuarios.add(usuario);
                                }

                                RecyclerViewAdapter_usuarios adapter = new RecyclerViewAdapter_usuarios(arrayusuarios,
                                        Admin.this);
                                recyclerView_usu.setAdapter(adapter);
                                refreshLayout.setRefreshing(false);
                            } else {
                                Toast.makeText(Admin.this, "Error", Toast.LENGTH_SHORT).show();
                                refreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });
        }else if (colonia.equals("2")){
            db.collection("mision_anahuac")
                    //.whereEqualTo("colonia", gettingColonia())
                    .orderBy("num_calle", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arrayusuarios = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    Usuario usuario = doc.toObject(Usuario.class);
                                    usuario.id= doc.getId();
                                    arrayusuarios.add(usuario);
                                }

                                RecyclerViewAdapter_usuarios adapter = new RecyclerViewAdapter_usuarios(arrayusuarios,
                                        Admin.this);
                                recyclerView_usu.setAdapter(adapter);
                                refreshLayout.setRefreshing(false);
                            } else {
                                Toast.makeText(Admin.this, "Error", Toast.LENGTH_SHORT).show();
                                refreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });
        }


    }

    @Override
    public void onUsuarioClick(int position, Usuario usuario) {
        arrayusuarios.get(position);
        //checar y mandar al negocio correspondiente
        Intent verusuario_intent = new Intent(this, Pago_Usu_Admin.class);
        verusuario_intent.putExtra("usuario", usuario);
        startActivity(verusuario_intent);
    }
}

