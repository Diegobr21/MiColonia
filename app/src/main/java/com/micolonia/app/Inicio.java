package com.micolonia.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

   // ImageView[] imagflipper;
  /*  int[] imagflipper = {


            R.drawable.food,
            R.drawable.wreck,
            R.drawable.foco
    };
*/
    private FirebaseFirestore db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);


        getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();

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
        //Toolbar iniciotoolbar=(androidx.appcompat.widget.Toolbar) findViewById(R.id.inicio_tool);
      //  setSupportActionBar(iniciotoolbar);

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

  /*  private void initializeData() {
        arrayavisos = new ArrayList<>();
        arrayavisos.add(new Aviso("Hoy habra una reunion en el kiosko a las 7pm", R.drawable.wreck));
        arrayavisos.add(new Aviso("Asalto ayer en el 7eleven a las 11pm", R.drawable.wreck));
        arrayavisos.add(new Aviso("El Lunes 8 pasará la basura", R.drawable.wreck));
        arrayavisos.add(new Aviso("Vecino, no olvides reciclar tu basura", R.drawable.food));
        arrayavisos.add(new Aviso("En el 2do sector habra venta de electrodomesticos por parte de
        arrayavisos.add(new Aviso("El alcalde hará una visita en 2 semanas", R.drawable.food));
        arrayavisos.add(new Aviso("Campaña para planta de arboles inicia el 10 de Julio", R.drawable.food));
        arrayavisos.add(new Aviso("Se un buen vecino!", R.drawable.food));
    }

    private void initializeAdapter() {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(arrayavisos);
        recyclerView.setAdapter(adapter);
    }
*/
    /**
     * TODO: Aquí se lee la información desde Firebase y se muestra en Firebase.
     */

    private void addData() {
        // Los datos en Firestore deben tener la estructura de modelo en JSON.
        // Usando el objeto Aviso, su estructura es así:
        // {name: "Nombre", logoId: 1}
        // logoID equivale a dos valores, usaremos números y en la app
        // evaluamos esos números para colocar la imagen correcta.

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando");
        progressDialog.show();
        refreshLayout.setRefreshing(false);

        db.collection("avisos")
                .orderBy("timestamp", Query.Direction.DESCENDING)
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
    int id_colonia;
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

    public int getId_colonia() {
        return id_colonia;
    }

    public void setId_colonia(int id_colonia) {
        this.id_colonia = id_colonia;
    }
}