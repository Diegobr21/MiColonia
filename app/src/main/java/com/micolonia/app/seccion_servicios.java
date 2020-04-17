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

import android.os.Parcel;
import android.os.Parcelable;
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

public class seccion_servicios extends AppCompatActivity implements RecyclerViewAdapter_servicios.OnNegocioSerListener {
    private AdapterViewFlipper flip_servicios;
    public Button noticias;
    ImageView perfil;
    FloatingActionButton publicar;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView_ser;
    Context context;
    private List<NegocioSer> arrayservicios;
    private List<Imagen> arrayimagenes;

    public String current_colonia;

    private DocumentReference usuRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seccion_servicios);

        getSupportActionBar().hide();

        //Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        usuRef=db.collection("usuarios").document(user.getUid());

        refreshLayout=findViewById(R.id.refresh_layout_serv);
        recyclerView_ser = (RecyclerView) findViewById(R.id.recyclerView_servicios);
        recyclerView_ser.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView_ser.setLayoutManager(layoutManager);
        RecyclerViewAdapter_servicios adapter_servicios = new RecyclerViewAdapter_servicios(arrayservicios, this);

        gettingColonia();
        addImages();

        publicar = findViewById(R.id.floatingpublicar);
        flip_servicios = (AdapterViewFlipper) findViewById(R.id.flipper_servicios);
        noticias = findViewById(R.id.btn_noticias);

        perfil = findViewById(R.id.btn_ing_perfil);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gettingColonia();
                addImages();
            }
        });

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verperfil();
            }
        });

        noticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vernoticias();
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
        intentperfil.putExtra("Remitente","DesdeServicios");
        startActivity(intentperfil);
    }

    public void vernoticias() {
        Intent intentvernoti = new Intent(this, Inicio.class);

        startActivity(intentvernoti);
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
                            addData(current_colonia);

                        }else{
                            Toast.makeText(seccion_servicios.this,"No existe el id_colonia del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(seccion_servicios.this,"Error!", Toast.LENGTH_SHORT).show();
                    }
                });
        return current_colonia;
    }


    private void addData(String colonia_s) {
        // Los datos en Firestore deben tener la estructura de modelo en JSON.
        // Usando el objeto Aviso, su estructura es así:
        // {name: "Nombre", logoId: 1}
        // logoID equivale a dos valores, usaremos números y en la app
        // evaluamos esos números para colocar la imagen correcta.

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando");
        progressDialog.show();
        refreshLayout.setRefreshing(false);

        if(colonia_s.equals("1")){
            db.collection("servicios_las_hadas")
                    //.whereEqualTo("id_colonia", colonia_s)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    //.whereEqualTo("visible", 1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arrayservicios = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    NegocioSer negocioSer = doc.toObject(NegocioSer.class);
                                    negocioSer.id = doc.getId();
                                    arrayservicios.add(negocioSer);
                                }

                                RecyclerViewAdapter_servicios adapter_servicios = new RecyclerViewAdapter_servicios(
                                        arrayservicios, seccion_servicios.this
                                );
                                recyclerView_ser.setAdapter(adapter_servicios);
                            } else {
                                Toast.makeText(seccion_servicios.this, "Error", Toast.LENGTH_SHORT).show();
                                refreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });
        }else if(colonia_s.equals("2")){
            db.collection("servicios_mision_anahuac")
                    //.whereEqualTo("id_colonia", colonia_s)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    //.whereEqualTo("visible", 1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arrayservicios = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    NegocioSer negocioSer = doc.toObject(NegocioSer.class);
                                    negocioSer.id = doc.getId();
                                    arrayservicios.add(negocioSer);
                                }

                                RecyclerViewAdapter_servicios adapter_servicios = new RecyclerViewAdapter_servicios(
                                        arrayservicios, seccion_servicios.this
                                );
                                recyclerView_ser.setAdapter(adapter_servicios);
                            } else {
                                Toast.makeText(seccion_servicios.this, "Error", Toast.LENGTH_SHORT).show();
                                refreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });
        }

    }
    public void addImages(){
        db.collection("imagenes")
                .whereEqualTo("idType", 3)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            arrayimagenes = new ArrayList<>();
                            refreshLayout.setRefreshing(false);
                            for (DocumentSnapshot doc : task.getResult()) {
                                Imagen imagen = doc.toObject(Imagen.class);
                                arrayimagenes.add(imagen);

                            }

                            setFlip_servicios();

                        } else {
                            Toast.makeText(seccion_servicios.this, "No hay imagenes que cargar",
                                    Toast.LENGTH_LONG).show();
                            refreshLayout.setRefreshing(false);
                        }

                    }
                });


    }

    public void setFlip_servicios(){
        CustomAdapter_ser customAdapter_ser = new CustomAdapter_ser(getApplicationContext(), arrayimagenes);
        flip_servicios.setAdapter(customAdapter_ser);
        //flip_ventas.setFlipInterval(2700);
        // flip_ventas.setAutoStart(true);
    }

    public void onFlipperArrowLeftClick(View view) {
        if (flip_servicios != null) {
            flip_servicios.showPrevious();
        }
    }

    public void onFlipperArrowRightClick(View view) {
        if (flip_servicios != null) {
            flip_servicios.showNext();
        }
    }



    @Override
    public void onNegocioSerClick(int position, NegocioSer negocioSer) {
        arrayservicios.get(position);
        //checar y mandar al negocio correspondiente
        Intent vernegocio_ser_intent = new Intent(this, Servicios.class);
        vernegocio_ser_intent.putExtra("servicio", negocioSer);
        startActivity(vernegocio_ser_intent);
    }
}


//CLASE PARA EL FLIPPER-------------------------------------------------------------
class CustomAdapter_ser extends BaseAdapter {
    Context context;
    /*private */final List<Imagen> arrayimagenes;

    LayoutInflater inflater;



    public CustomAdapter_ser(Context applicationContext, List <Imagen> arrayimagenes) {
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
        view = inflater.inflate(R.layout.imagen_flipper_servicios, null);
        ImageView image = (ImageView) view.findViewById(R.id.imagenservicios);
        int tipo = arrayimagenes.get(position).getIdType();
        if(tipo== 3){
            Glide.with(view).load(arrayimagenes.get(position).getUrl()).into(image);
        }

        // image.setImageResource(images_ven[position]);
        return view;

    } }


class NegocioSer implements Parcelable {

    public static final int LOGO_ID_TYPE_WRECK = 1;
    public static final int LOGO_ID_TYPE_FOOD = 2;

    String name;
    int logoId;
    String id;
    String id_colonia;
    String imagen;
    String telefono;
    String descripcion;
    String horario;
    String elemento1;
    String elemento2;
    String elemento3;
    String elemento4;
    String elemento5;
    String elemento6;

    NegocioSer(String name, int logoId) {
        this.name = name;
        this.logoId = logoId;
    }
    public NegocioSer() {
    }

    protected NegocioSer(Parcel in) {
        name = in.readString();
        logoId = in.readInt();
        imagen = in.readString();
        id = in.readString();
        id_colonia = in.readString();
        telefono = in.readString();
        descripcion = in.readString();
        horario = in.readString();
        elemento1 = in.readString();
        elemento2 = in.readString();
        elemento3 = in.readString();
        elemento4 = in.readString();
        elemento5 = in.readString();
        elemento6 = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeInt(logoId);
        dest.writeString(imagen);
        dest.writeString(id);
        dest.writeString(id_colonia);
        dest.writeString(telefono);
        dest.writeString(descripcion);
        dest.writeString(horario);
        dest.writeString(elemento1);
        dest.writeString(elemento2);
        dest.writeString(elemento3);
        dest.writeString(elemento4);
        dest.writeString(elemento5);
        dest.writeString(elemento6);
    }
    @Override
    public int describeContents()  {
        return 0;
    }

        public static final Creator<NegocioSer> CREATOR = new Creator<NegocioSer>() {
            @Override
            public NegocioSer createFromParcel(Parcel in) {
                return new NegocioSer(in);
            }

            @Override
            public NegocioSer[] newArray(int size) {
                return new NegocioSer[size];
            }
    };


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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_colonia() {
        return id_colonia;
    }

    public void setId_colonia(String id_colonia) {
        this.id_colonia = id_colonia;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getElemento1() {
        return elemento1;
    }

    public void setElemento1(String elemento1) {
        this.elemento1 = elemento1;
    }
    public String getElemento2() {
        return elemento2;
    }

    public void setElemento2(String elemento2) {
        this.elemento2 = elemento2;
    }
    public String getElemento3() {
        return elemento3;
    }

    public void setElemento3(String elemento3) {
        this.elemento3 = elemento3;
    }
    public String getElemento4() {
        return elemento4;
    }

    public void setElemento4(String elemento4) {
        this.elemento4 = elemento4;
    }

    public String getElemento5() {
        return elemento5;
    }

    public void setElemento5(String elemento5) { this.elemento5 = elemento5;
    }

    public String getElemento6() {
        return elemento6;
    }

    public void setElemento6(String elemento6) {
        this.elemento6 = elemento6;
    }


}
