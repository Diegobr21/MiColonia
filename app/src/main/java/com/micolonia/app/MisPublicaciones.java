package com.micolonia.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class MisPublicaciones extends AppCompatActivity implements RecyclerViewAdapter_Mis_Publicaciones.OnPublicacionListener, RecyclerViewAdapter_ventas.OnVentaListener, RecyclerViewAdapter_servicios.OnNegocioSerListener{
    private DocumentReference usuRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String current_colonia, id_usu;
    private Button comida, servicios, ventas;

    RecyclerView recyclerView_pubs, recyclerView_ventas, recyclerView_servs;
    Context context;
    private List<NegocioCom> arraypublicaciones;
    private List<Venta> arraypubventas;
    private List<NegocioSer> arraypubservs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_publicaciones);

        comida = findViewById(R.id.pubs_comida);
        servicios = findViewById(R.id.pubs_servicios);
        ventas = findViewById(R.id.pubs_venta);
        recyclerView_pubs = (RecyclerView) findViewById(R.id.recyclerView_mis_publicaciones);
        recyclerView_pubs.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView_pubs.setLayoutManager(layoutManager);
        RecyclerViewAdapter_Mis_Publicaciones adapter_pubs = new RecyclerViewAdapter_Mis_Publicaciones(arraypublicaciones,this);

        //Ventas
        recyclerView_ventas = (RecyclerView) findViewById(R.id.recyclerView_mis_publicaciones);
        recyclerView_ventas.setHasFixedSize(true);
        recyclerView_ventas.setLayoutManager(layoutManager);
        RecyclerViewAdapter_ventas adapter_ventas = new RecyclerViewAdapter_ventas(arraypubventas,this);

        //Servicios

        recyclerView_servs = (RecyclerView) findViewById(R.id.recyclerView_mis_publicaciones);
        recyclerView_servs.setHasFixedSize(true);
        recyclerView_servs.setLayoutManager(layoutManager);
        RecyclerViewAdapter_servicios adapter_servs = new RecyclerViewAdapter_servicios(arraypubservs,this);

        //Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        //email_current_user=user.getEmail();
        usuRef=db.collection("usuarios").document(user.getUid());

        Toast.makeText(this, "Seleccione el tipo de publicaciones a mostrar", Toast.LENGTH_LONG).show();

        comida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingColonia(0);
            }
        });

        servicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingColonia(1);
            }
        });

        ventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingColonia(2);
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
                            id_usu =mAuth.getCurrentUser().getUid();
                            switch (opcion){
                                case 0: addComida(current_colonia, id_usu);
                                break;
                                case 1: addServicios(current_colonia, id_usu);
                                break;
                                case 2: addVentas(current_colonia, id_usu);
                                break;
                                default: addComida(current_colonia, id_usu);
                            }


                        }else{
                            Toast.makeText(MisPublicaciones.this,"No existe el id_colonia del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MisPublicaciones.this,"Error!", Toast.LENGTH_SHORT).show();
                    }
                });
        return current_colonia;
    }


    private void addComida(String colonia, String id_usuario) {
        // Los datos en Firestore deben tener la estructura de modelo en JSON.
        // Usando el objeto Aviso, su estructura es así:
        // {name: "Nombre", logoId: 1}
        // logoID equivale a dos valores, usaremos números y en la app
        // evaluamos esos números para colocar la imagen correcta.

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando");
        progressDialog.show();
        //refreshLayout.setRefreshing(false);
        if(colonia.equals("1")){
            db.collection("comida_las_hadas")
                    .whereEqualTo("id_usu", id_usuario)
                    //.orderBy("timestamp", Query.Direction.DESCENDING)
                    //.whereEqualTo("visible", 1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arraypublicaciones = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    NegocioCom publicacion = doc.toObject(NegocioCom.class);
                                    publicacion.id= doc.getId();
                                    arraypublicaciones.add(publicacion);
                                }

                                RecyclerViewAdapter_Mis_Publicaciones adapter_pubs =
                                        new RecyclerViewAdapter_Mis_Publicaciones(
                                                arraypublicaciones,
                                                MisPublicaciones.this
                                        );
                                recyclerView_pubs.setAdapter(adapter_pubs);
                            } else {
                                Toast.makeText(MisPublicaciones.this, "Error", Toast.LENGTH_SHORT).show();
                                //refreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });

        }else if(colonia.equals("2")){
            db.collection("comida_mision_anahuac")
                    .whereEqualTo("id_usu", id_usuario)
                    //.orderBy("timestamp", Query.Direction.DESCENDING)
                    //.whereEqualTo("visible", 1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arraypublicaciones = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    NegocioCom negocioCom = doc.toObject(NegocioCom.class);
                                    negocioCom.id= doc.getId();
                                    arraypublicaciones.add(negocioCom);
                                }

                                RecyclerViewAdapter_Mis_Publicaciones adapter_pubs =
                                        new RecyclerViewAdapter_Mis_Publicaciones(
                                                arraypublicaciones,
                                                MisPublicaciones.this
                                        );
                                recyclerView_pubs.setAdapter(adapter_pubs);
                            } else {
                                Toast.makeText(MisPublicaciones.this, "Error", Toast.LENGTH_SHORT).show();
                                //refreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });

        }

    }

    private void addServicios(String colonia, String id_usuario) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando");
        progressDialog.show();
        //refreshLayout.setRefreshing(false);
        if (colonia.equals("1")) {
            db.collection("servicios_las_hadas")
                    .whereEqualTo("id_usu", id_usuario)
                    //.orderBy("timestamp", Query.Direction.DESCENDING)
                    //.whereEqualTo("visible", 1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arraypubservs = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    NegocioSer publicacion = doc.toObject(NegocioSer.class);
                                    publicacion.id = doc.getId();
                                    arraypubservs.add(publicacion);
                                }

                                RecyclerViewAdapter_servicios adapter_servicios =
                                        new RecyclerViewAdapter_servicios(
                                                arraypubservs,
                                                MisPublicaciones.this
                                        );
                                recyclerView_servs.setAdapter(adapter_servicios);
                            } else {
                                Toast.makeText(MisPublicaciones.this, "Error", Toast.LENGTH_SHORT).show();
                                //refreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });
        }else if(colonia.equals("2")){
            db.collection("servicios_mision_anahuac")
                    .whereEqualTo("id_usu", id_usuario)
                    //.orderBy("timestamp", Query.Direction.DESCENDING)
                    //.whereEqualTo("visible", 1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arraypubservs = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    NegocioSer publicacion = doc.toObject(NegocioSer.class);
                                    publicacion.id = doc.getId();
                                    arraypubservs.add(publicacion);
                                }

                                RecyclerViewAdapter_servicios adapter_servicios =
                                        new RecyclerViewAdapter_servicios(
                                                arraypubservs,
                                                MisPublicaciones.this
                                        );
                                recyclerView_servs.setAdapter(adapter_servicios);
                            } else {
                                Toast.makeText(MisPublicaciones.this, "Error", Toast.LENGTH_SHORT).show();
                                //refreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });
        }
    }

    private void addVentas(String colonia, String id_usuario) {
        // Los datos en Firestore deben tener la estructura de modelo en JSON.
        // Usando el objeto Aviso, su estructura es así:
        // {name: "Nombre", logoId: 1}
        // logoID equivale a dos valores, usaremos números y en la app
        // evaluamos esos números para colocar la imagen correcta.

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando");
        progressDialog.show();
        //refreshLayout.setRefreshing(false);
        if(colonia.equals("1")){
            db.collection("ventas_las_hadas")
                    .whereEqualTo("id_usu", id_usuario)
                    //.orderBy("timestamp", Query.Direction.DESCENDING)
                    //.whereEqualTo("visible", 1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arraypubventas = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    Venta publicacion = doc.toObject(Venta.class);
                                    publicacion.id= doc.getId();
                                    arraypubventas.add(publicacion);
                                }

                                RecyclerViewAdapter_ventas adapter_ventas =
                                        new RecyclerViewAdapter_ventas(
                                                arraypubventas,
                                                MisPublicaciones.this
                                        );
                                recyclerView_ventas.setAdapter(adapter_ventas);
                            } else {
                                Toast.makeText(MisPublicaciones.this, "Error", Toast.LENGTH_SHORT).show();
                                //refreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });

        }else if(colonia.equals("2")){
            db.collection("ventas_mision_anahuac")
                    .whereEqualTo("id_usu", id_usuario)
                    //.orderBy("timestamp", Query.Direction.DESCENDING)
                    //.whereEqualTo("visible", 1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arraypubventas = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    Venta publicacion = doc.toObject(Venta.class);
                                    publicacion.id= doc.getId();
                                    arraypubventas.add(publicacion);
                                }

                                RecyclerViewAdapter_ventas adapter_ventas =
                                        new RecyclerViewAdapter_ventas(
                                                arraypubventas,
                                                MisPublicaciones.this
                                        );
                                recyclerView_ventas.setAdapter(adapter_ventas);
                            } else {
                                Toast.makeText(MisPublicaciones.this, "Error", Toast.LENGTH_SHORT).show();
                                //refreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });

        }

    }

    @Override
    public void onPublicacionClick(int position, NegocioCom publicacion) {
        arraypublicaciones.get(position);
        //checar y mandar al negocio correspondiente
        Intent verpub_intent = new Intent(this, Comida.class); //??? Servicios???
        verpub_intent.putExtra("publicacion", publicacion);
        startActivity(verpub_intent);
    }

    @Override
    public void onVentaClick(int position, Venta venta) {
        arraypubventas.get(position);
        //checar y mandar al negocio correspondiente
        Intent verpub_intent2 = new Intent(this, Imagen_venta.class);
        verpub_intent2.putExtra("publicacion", venta);
        startActivity(verpub_intent2);
    }

    @Override
    public void onNegocioSerClick(int position, NegocioSer negocioSer) {
        arraypubservs.get(position);
        Intent verpub_intent3 = new Intent(this, Servicios.class);
        verpub_intent3.putExtra("publicacion", negocioSer);
        startActivity(verpub_intent3);
    }
}
