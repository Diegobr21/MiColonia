package com.micolonia.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Inicio extends AppCompatActivity implements RecyclerViewAdapter.OnAvisoListener,
        NavigationView.OnNavigationItemSelectedListener {
    private AdapterViewFlipper flip_inicio;
    private DrawerLayout drawerini;
   //public ImageButton comida, servicio, ventas;
    public Button  comida, servicio, ventas;
    FloatingActionButton publicar;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    ImageView perfil;
    ImageView imagen;
    //TextView correo;
    Context context;
    private List<Aviso> arrayavisos;
    private List<Imagen> arrayimagenes;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private DocumentReference usuRef;
    private String email_current_user;
    public String current_colonia, colonia, current_tipo;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean agreed = sharedPreferences.getBoolean("agreed",false);
        if (!agreed) {
            new AlertDialog.Builder(this)
                    .setTitle("License agreement")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("agreed", true);
                            editor.commit();
                        }
                    })
                    //.setNegativeButton("No", null)
                    .setTitle(R.string.avipriv )
                    .setMessage(R.string.avisodeprivacidad1)
                    .show();
        }


        Toolbar toolbar = findViewById(R.id.inicio_toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().hide();

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


        drawerini = findViewById(R.id.nav_drawerlayoutINI);
        NavigationView navigationView = findViewById(R.id.navigation_inicio);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerini, toolbar,
                R.string.drawer_abrir, R.string.drawer_cierre);
        drawerini.addDrawerListener(toggle);
        toggle.syncState();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(arrayavisos, this);
        //initializeData();
        //initializeAdapter();
        gettingColonia();
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
                gettingColonia();
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

        /*
        if (savedInstanceState == null){
            navigationView.setCheckedItem(R.id.nav_publicar);
        }

         */

    }


    @Override
    public void onBackPressed() {
        if(drawerini.isDrawerOpen(GravityCompat.START)){
            drawerini.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }

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

    private void fueradeapp() {
        FirebaseAuth.getInstance().signOut();

        finish();
        Intent intent = new Intent(this, InicioSesion.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }

    private void toAdminLayout(){
        Intent intentadmin = new Intent(this, Admin.class);

        startActivity(intentadmin);
    }

    private void contacto() {
        Intent intent3 = new Intent(this, Contacto.class);

        startActivity(intent3);
    }


    private void ayuda() {

        Intent intent5 = new Intent(this, Ayuda.class);

        startActivity(intent5);
    }

    private void QR(){
        Intent intent6 = new Intent(this, LectorQR.class);

        startActivity(intent6);
    }

    private void a_buzon(){
        Intent intent8 = new Intent(this, Buzon_Comentarios.class);

        startActivity(intent8);
    }

    private void dialogoalerta() {
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Cierre de Sesión");
        builder.setMessage("¿Quieres cerrar sesión?");
        //listeners de los botones
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fueradeapp();
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
                            Toast.makeText(Inicio.this,"No existe el id_colonia del usuario", Toast.LENGTH_SHORT).show();
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

    public void gettingTipo(){
        usuRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()){
                            Map<String, Object> usu = documentSnapshot.getData();
                            current_tipo = usu.get("tipo").toString().trim();
                            //Toast.makeText(Perfil.this, current_tipo, Toast.LENGTH_SHORT).show();
                            if (current_tipo.equals("2")){
                                toAdminLayout();
                            }else{
                                Toast.makeText(Inicio.this, "No es jefe de colonos", Toast.LENGTH_LONG).show();
                            }

                        }else{
                            Toast.makeText(Inicio.this,"No existe el id_colonia del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Inicio.this,"Error!", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void addData(String colonia_avisos) {
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

        if (colonia_avisos.equals("1")){
            db.collection("avisos_las_hadas")
                    //.whereEqualTo("id_colonia", colonia_avisos)
                     .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arrayavisos = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    Aviso aviso = doc.toObject(Aviso.class);
                                    aviso.id = doc.getId();
                                    arrayavisos.add(aviso);
                                }

                                RecyclerViewAdapter adapter = new RecyclerViewAdapter(arrayavisos, Inicio.this);
                                recyclerView.setAdapter(adapter);
                            } else {
                                Toast.makeText(Inicio.this, "Error", Toast.LENGTH_SHORT).show();
                                refreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });

        }else if(colonia_avisos.equals("2")){
            db.collection("avisos_mision_anahuac")
                    //.whereEqualTo("id_colonia", colonia_avisos)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arrayavisos = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    Aviso aviso = doc.toObject(Aviso.class);
                                    aviso.id = doc.getId();
                                    arrayavisos.add(aviso);
                                }

                                RecyclerViewAdapter adapter = new RecyclerViewAdapter(arrayavisos, Inicio.this);
                                recyclerView.setAdapter(adapter);
                            } else {
                                Toast.makeText(Inicio.this, "Error", Toast.LENGTH_SHORT).show();
                                refreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });
        }

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

    @Override
    public void onAvisoClick(int position, Aviso aviso) {
        arrayavisos.get(position);
        //checar y mandar al negocio correspondiente
        Intent ver_avisointent = new Intent(this, Eliminar_Aviso.class);
        ver_avisointent.putExtra("aviso", aviso);
        startActivity(ver_avisointent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.nav_publicar:
                verpublicar();
                break;

            case R.id.nav_qr:
                    QR();
                    break;

            case R.id.nav_buzon:
                    a_buzon();
                    break;

            case R.id.nav_admin:
                    gettingTipo();
                    break;

            case R.id.nav_contacto:
                contacto();
                break;

            case R.id.nav_ayuda:
                ayuda();
                break;

            case R.id.nav_cerrar_sesion:
                dialogoalerta();
                break;

        }

        drawerini.closeDrawer(GravityCompat.START);
        return true;
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

class Aviso implements Parcelable {

    public static final int LOGO_ID_TYPE_WRECK = 1;
    public static final int LOGO_ID_TYPE_FOOD = 2;

    String id;
    String name;
    int logoId;
    String imagen;
    String fecha;
    int tipo;
    String id_colonia;
    double timestamp;
    String comentarios;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
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

    protected Aviso(Parcel in) {
        name = in.readString();
        imagen = in.readString();
        id = in.readString();
        id_colonia = in.readString();
        fecha = in.readString();
        logoId = in.readInt();
        tipo = in.readInt();
        comentarios = in.readString();

    }

    @Override
    public int describeContents() { return 0; }

    public static final Creator<Aviso> CREATOR = new Creator<Aviso>() {
        @Override
        public Aviso createFromParcel(Parcel in) {
            return new Aviso(in);
        }

        @Override
        public Aviso[] newArray(int size) {
            return new Aviso[size];
        }
    };


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imagen);
        dest.writeString(id);
        dest.writeString(id_colonia);
        dest.writeString(fecha);
        dest.writeInt(logoId);
        dest.writeInt(tipo);
        dest.writeString(comentarios);



    }
}

