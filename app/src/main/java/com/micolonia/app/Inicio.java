package com.micolonia.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Inicio extends AppCompatActivity {
    private AdapterViewFlipper flip_inicio;
   //public ImageButton comida, servicio, ventas;
    public Button  comida, servicio, ventas;
    FloatingActionButton publicar;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    ImageView perfil;
    ImageView imagen;
    Context context;
    private List<Aviso> arrayavisos;
    private List<Imagen> arrayimagenes;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private DocumentReference usuRef;
    private String email_current_user;
    public String current_colonia, colonia;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);


        getSupportActionBar().hide();
    //Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        email_current_user=user.getEmail();
        usuRef=db.collection("usuarios").document(user.getUid());

        try{
            colonia = gettingColonia();
        }catch (Exception e){
            colonia="1";
        }

//Toast.makeText(Inicio.this, "Num "+colonia, Toast.LENGTH_SHORT).show();

        refreshLayout=findViewById(R.id.refresh_layout_ini);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(arrayavisos);
        //initializeData();
        //initializeAdapter();
        addData();
        addImages();


        publicar = (FloatingActionButton) findViewById(R.id.floatingpublicar);
        flip_inicio = (AdapterViewFlipper) findViewById(R.id.flipper);
        comida = findViewById(R.id.imgbtn_comida);
        servicio = findViewById(R.id.imgbtn_servicio);
        ventas =  findViewById(R.id.imgbtn_ventas);


        perfil = (ImageView) findViewById(R.id.btn_ing_perfil);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addData();
                addImages();
            }
        });


        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mandar a validar usuario y contraseña
                //saber que usuario es
                //  Log.d("Las Hadas", "Bienvenid@");//agregar nombre de usuario en Bienvenido
                verperfil();
            }
        });

        comida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verseccioncomida();
            }
        });

        servicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verseccionservicios();
            }
        });

        ventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verseccionventas();
            }
        });

        publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verpublicar();
            }
        });


    }

    public void verperfil() {
        Intent intentperfil = new Intent(this, Perfil.class);
        intentperfil.putExtra("Remitente","DesdeInicio");
        startActivity(intentperfil);
    }

    public void verseccioncomida() {
        Intent intentcomida = new Intent(this, seccion_comida.class);

        startActivity(intentcomida);
    }

    public void verseccionservicios() {
        Intent intentserv = new Intent(this, seccion_servicios.class);

        startActivity(intentserv);
    }

    public void verseccionventas() {
        Intent intentventas = new Intent(this, seccion_ventas.class);

        startActivity(intentventas);
    }

    public void verpublicar() {
        Intent intentpublica = new Intent(this, Publicar.class);

        startActivity(intentpublica);
    }

    /**
     * TODO: Aquí se lee la información desde Firebase y se muestra en Firebase.
     */
    public String gettingColonia(){
        usuRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()){
                            Map<String, Object> usu = documentSnapshot.getData();
                            current_colonia = usu.get("colonia").toString().trim();
                                 //documentSnapshot.getString("colonia");
                                 //current_colonia=Integer.parseInt(cur_co);
                            //Toast.makeText(Inicio.this, "Num "+current_colonia, Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(Inicio.this,"No existe el id_colonia del usaurio", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Inicio.this,"Error!", Toast.LENGTH_SHORT).show();
                    }
                });
        return current_colonia;
    }


    private void addData() {
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
        refreshLayout.setRefreshing(false);

        db.collection("avisos")
                .whereEqualTo("id_colonia", gettingColonia())
                // .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            arrayavisos = new ArrayList<>();

                            for (DocumentSnapshot doc : task.getResult()) {
                                Aviso aviso = doc.toObject(Aviso.class);
                                arrayavisos.add(aviso);
                            }

                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(arrayavisos);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(Inicio.this, "Error", Toast.LENGTH_SHORT).show();
                            refreshLayout.setRefreshing(false);
                        }

                        progressDialog.dismiss();
                    }
                });
    }


    public void addImages(){
        db.collection("imagenes")
                .whereEqualTo("idType", 1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            arrayimagenes = new ArrayList<>();
                            refreshLayout.setRefreshing(false);
                            //  int position =0;
                            for (DocumentSnapshot doc : task.getResult()) {
                                Imagen imagen = doc.toObject(Imagen.class);
                                arrayimagenes.add(imagen);

                            }
                            // final String urlimagen= task.getResult().getDocuments().toString();
                            // RootRef.child("imagenes").child("url").setValue(urlimagen);
                            setFlip_inicio();

                        } else {
                            Toast.makeText(Inicio.this, "No hay imagenes que cargar", Toast.LENGTH_LONG).show();
                            refreshLayout.setRefreshing(false);
                        }

                    }
                });


    }

    public void setFlip_inicio(){
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), arrayimagenes);
        flip_inicio.setAdapter(customAdapter);
        //flip_ventas.setFlipInterval(2700);
        // flip_ventas.setAutoStart(true);
    }

    public void onFlipperArrowLeftClick(View view) {
        if (flip_inicio != null) {
            flip_inicio.showPrevious();
        }
    }

    public void onFlipperArrowRightClick(View view) {
        if (flip_inicio != null) {
            flip_inicio.showNext();
        }
    }




}


//CLASE PARA EL FLIPPER-------------------------------------------------------------
class CustomAdapter extends BaseAdapter {
    Context context;
    final List<Imagen> arrayimagenes;

    LayoutInflater inflater;



    public CustomAdapter(Context applicationContext, List <Imagen> arrayimagenes) {
        this.context = applicationContext;
        this.arrayimagenes = arrayimagenes;
        inflater = (LayoutInflater.from(applicationContext));

    }


    @Override
    public int getCount() {
        return arrayimagenes.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayimagenes.get(position);
        // return null;
    }
    @Override
    public long getItemId(int position) {
        return arrayimagenes.get(position).getId();
        // return 0;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.imagen_flipper, null);
        ImageView image = (ImageView) view.findViewById(R.id.imagen_flip);
        int tipo = arrayimagenes.get(position).getIdType();
        if(tipo== 1){
            Glide.with(view).load(arrayimagenes.get(position).getUrl()).into(image);
        }

        // image.setImageResource(images_ven[position]);
        return view;

    } }

class Aviso {

    public static final int LOGO_ID_TYPE_WRECK = 1;
    public static final int LOGO_ID_TYPE_FOOD = 2;

    String name;
    int logoId;
    String imagen;
    String fecha;
    int tipo;
    String id_colonia;
    double timestamp;
    //Siempre un constructor vacio.

    public Aviso() {
    }

    Aviso(String name, int logoId) {
        this.name = name;
        this.logoId = logoId;
    }

    // Siempre getters y setters.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLogoId() {
        return logoId;
    }

    public void setLogoId(int logoId) {
        this.logoId = logoId;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getId_colonia() { return id_colonia; }

    public void setId_colonia(String id_colonia) { this.id_colonia = id_colonia; }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }
}