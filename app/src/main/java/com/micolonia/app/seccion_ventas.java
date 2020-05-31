package com.micolonia.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
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

public class seccion_ventas extends AppCompatActivity implements RecyclerViewAdapter_ventas.OnVentaListener,
        NavigationView.OnNavigationItemSelectedListener {
    public AdapterViewFlipper flip_ventas;
    public Button  noticias;
    //ImageView perfil;
   // ImageView prueba;
    private Switch bienesraices;
    private DrawerLayout drawerventas;
    FloatingActionButton publicar;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView_ven;
    Context context;
    private List<Venta> arrayventas;
    private List<Imagen> arrayimagenes;

    public String current_colonia, current_tipo;

    private DocumentReference usuRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seccion_ventas);

        //getSupportActionBar().hide();

        //Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        usuRef=db.collection("usuarios").document(user.getUid());

       // prueba = findViewById(R.id.imgprueba);
        refreshLayout=findViewById(R.id.refresh_layout_ven);
        recyclerView_ven = (RecyclerView) findViewById(R.id.recyclerView_ventas);
        recyclerView_ven.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView_ven.setLayoutManager(layoutManager);
        RecyclerViewAdapter_ventas adapter_ventas = new RecyclerViewAdapter_ventas(arrayventas, this);

        gettingColonia(0);
        addImages();

        //Toolbar
        Toolbar toolbar = findViewById(R.id.ventas_toolbar);
        setSupportActionBar(toolbar);

        drawerventas = findViewById(R.id.nav_drawerlayoutVEN);
        NavigationView navigationView = findViewById(R.id.navigation_ventas);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerventas, toolbar,
                R.string.drawer_abrir, R.string.drawer_cierre);
        drawerventas.addDrawerListener(toggle);
        toggle.syncState();


        //mostrarbienes();

        publicar=findViewById(R.id.floatingpublicar);
        flip_ventas=(AdapterViewFlipper) findViewById(R.id.flipper_ventas);
        noticias= findViewById(R.id.btn_noticias1);
       // perfil= findViewById(R.id.btn_ing_perfil);
        bienesraices = findViewById(R.id.switch_bienes);


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gettingColonia(0);
                addImages();
            }
        });

        /*
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  Log.d("Las Hadas", "Bienvenid@");//agregar nombre de usuario en Bienvenido
                verperfil();
            }
        });

         */

       bienesraices.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked==true){
                gettingColonia(1);
               }
               else{
                   gettingColonia(0);
               }
           }
       });

        noticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verseccionnoticias();
            }
        });

        publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verpublicar();
            }
        });

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
                                Toast.makeText(seccion_ventas.this, "No es jefe de colonos", Toast.LENGTH_LONG).show();
                            }

                        }else{
                            Toast.makeText(seccion_ventas.this,"No existe el id_colonia del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(seccion_ventas.this,"Error!", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    public void onBackPressed() {
        if(drawerventas.isDrawerOpen(GravityCompat.START)){
            drawerventas.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }

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

/*
    public void verperfil(){
        Intent intentperfil = new Intent(this, Perfil.class);
        intentperfil.putExtra("Remitente","DesdeVentas");
        startActivity( intentperfil);
    }

 */


    public void verseccionnoticias(){
        Intent intentnoti = new Intent(this, Inicio.class);

        startActivity( intentnoti);
    }

    public void verpublicar(){
        Intent intentpublica = new Intent(this, Publicar.class);

        startActivity( intentpublica);
    }



    /**
     * TODO: Aquí se lee la información desde Firebase y se muestra en Firebase.
     */

    public String gettingColonia(final int x){
        usuRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()){
                            Map<String, Object> usu = documentSnapshot.getData();
                            current_colonia = usu.get("colonia").toString().trim();

                                if(x==0){
                                    addData(current_colonia);
                                }else {
                                    mostrarbienes(current_colonia);
                                }


                        }else{
                            Toast.makeText(seccion_ventas.this,"No existe el id_colonia del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(seccion_ventas.this,"Error!", Toast.LENGTH_SHORT).show();
                    }
                });
        return current_colonia;
    }
    private void addData(String colonia_v) {
        // Los datos en Firestore deben tener la estructura de modelo en JSON.
        // Usando el objeto Aviso, su estructura es así:
        // {name: "Nombre", logoId: 1}
        // logoID equivale a dos valores, usaremos números y en la app
        // evaluamos esos números para colocar la imagen correcta.

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando");
        progressDialog.show();
        refreshLayout.setRefreshing(false);

        if(colonia_v.equals("1")){
            db.collection("ventas_las_hadas")
                    //.whereEqualTo("id_colonia", colonia_v)
                    //.whereGreaterThan("aprobado", 0)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arrayventas = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    Venta venta = doc.toObject(Venta.class);
                                    venta.id = doc.getId();
                                    arrayventas.add(venta);
                                }


                                RecyclerViewAdapter_ventas adapter_ventas =
                                        new RecyclerViewAdapter_ventas(arrayventas,
                                                seccion_ventas.this);
                                recyclerView_ven.setAdapter(adapter_ventas);
                            } else {
                                Toast.makeText(seccion_ventas.this, "Error, puede que no haya avisos de ocasión de este tipo",
                                        Toast.LENGTH_LONG).show();
                                refreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });
        }else if(colonia_v.equals("2")){
            db.collection("ventas_mision_anahuac")
                    //.whereEqualTo("id_colonia", colonia_v)
                    //.whereGreaterThan("aprobado", 0)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arrayventas = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    Venta venta = doc.toObject(Venta.class);
                                    venta.id = doc.getId();
                                    arrayventas.add(venta);
                                }


                                RecyclerViewAdapter_ventas adapter_ventas =
                                        new RecyclerViewAdapter_ventas(arrayventas,
                                                seccion_ventas.this);
                                recyclerView_ven.setAdapter(adapter_ventas);
                            } else {
                                Toast.makeText(seccion_ventas.this, "Error, puede que no haya avisos de ocasión de este tipo",
                                        Toast.LENGTH_LONG).show();
                                refreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });
        }

    }



    private void mostrarbienes(String colonia_v){
        final ProgressDialog progressDialog2 = new ProgressDialog(this);
        progressDialog2.setMessage("Cargando");
        progressDialog2.show();

        if(colonia_v.equals("1")){
            db.collection("ventas_las_hadas")
                    //.orderBy("idType")
                    //.orderBy("timestamp")
                    .whereEqualTo("idType", "2")
                    //.startAt(2)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arrayventas = new ArrayList<>();
                                refreshLayout.setRefreshing(false);

                                for (DocumentSnapshot doc : task.getResult()) {
                                    Venta venta = doc.toObject(Venta.class);
                                    venta.id = doc.getId();
                                    arrayventas.add(venta);
                                }

                                RecyclerViewAdapter_ventas adapter_ventas = new RecyclerViewAdapter_ventas(arrayventas,
                                        seccion_ventas.this);
                                recyclerView_ven.setAdapter(adapter_ventas);
                            }
                            else {
                                Toast.makeText(seccion_ventas.this, "Error", Toast.LENGTH_SHORT).show();
                                refreshLayout.setRefreshing(false);
                            }

                            progressDialog2.dismiss();
                        }
                    });
        }else if(colonia_v.equals("2")){
            db.collection("ventas_mision_anahuac")
                    //.orderBy("idType")
                    .orderBy("timestamp")
                    .whereEqualTo("idType", "2")
                    //.startAt(2)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arrayventas = new ArrayList<>();
                                refreshLayout.setRefreshing(false);

                                for (DocumentSnapshot doc : task.getResult()) {
                                    Venta venta = doc.toObject(Venta.class);
                                    venta.id = doc.getId();
                                    arrayventas.add(venta);
                                }

                                RecyclerViewAdapter_ventas adapter_ventas = new RecyclerViewAdapter_ventas(arrayventas,
                                        seccion_ventas.this);
                                recyclerView_ven.setAdapter(adapter_ventas);
                            }
                            else {
                                Toast.makeText(seccion_ventas.this, "Error", Toast.LENGTH_SHORT).show();
                                refreshLayout.setRefreshing(false);
                            }

                            progressDialog2.dismiss();
                        }
                    });
        }

    }


    public void addImages(){
        db.collection("imagenes")
                .whereEqualTo("idType", 4)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            arrayimagenes = new ArrayList<>();
                              //  int position =0;
                            for (DocumentSnapshot doc : task.getResult()) {
                             Imagen imagen = doc.toObject(Imagen.class);
                                arrayimagenes.add(imagen);

                            }
                           // final String urlimagen= task.getResult().getDocuments().toString();
                           // RootRef.child("imagenes").child("url").setValue(urlimagen);
                                    setFlip_ventas();

                        } else {
                            Toast.makeText(seccion_ventas.this, "No hay imagenes que cargar",
                                    Toast.LENGTH_LONG).show();
                            refreshLayout.setRefreshing(false);
                        }

                    }
                });


    }



public void setFlip_ventas(){
    CustomAdapter_ven customAdapter_ven = new CustomAdapter_ven(getApplicationContext(), arrayimagenes);
    flip_ventas.setAdapter(customAdapter_ven);
    //flip_ventas.setFlipInterval(2700);
   // flip_ventas.setAutoStart(true);
}

    public void onFlipperArrowLeftClick(View view) {
        if (flip_ventas != null) {
            flip_ventas.showPrevious();
        }
    }

    public void onFlipperArrowRightClick(View view) {
        if (flip_ventas != null) {
            flip_ventas.showNext();
        }
    }



    @Override
    public void onVentaClick(int position, Venta venta) {
        arrayventas.get(position);
        //checar y mandar al negocio correspondiente
        Intent ver_venta_intent = new Intent(this, Imagen_venta.class);
        ver_venta_intent.putExtra("publicacion", venta);
        startActivity(ver_venta_intent);
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

        drawerventas.closeDrawer(GravityCompat.START);
        return true;
    }
}


//CLASE PARA EL FLIPPER-------------------------------------------------------------
class CustomAdapter_ven extends BaseAdapter {

    Context context;
     /*private */final List<Imagen> arrayimagenes;

    LayoutInflater inflater;



    public CustomAdapter_ven(Context applicationContext, List <Imagen> arrayimagenes) {
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
        view = inflater.inflate(R.layout.imagen_flipper_ventas, null);
        ImageView image = (ImageView) view.findViewById(R.id.imagenventas);
        int tipo = arrayimagenes.get(position).getIdType();
        if(tipo== 4){
            Glide.with(view).load(arrayimagenes.get(position).getUrl()).into(image);
        }

       // image.setImageResource(images_ven[position]);
        return view;

    } }

class Venta implements Parcelable {

    public static final int LOGO_ID_TYPE_WRECK = 1;
    public static final int LOGO_ID_TYPE_FOOD = 2;

    String name;
    String id;
    String descripcion;
    String costo;
    String telefono;
    String imagen;
    String id_colonia;
    String id_usu;
    // String titulo;
    String tipo;
    int aprobado;
    int logoId;

    Venta(String name, int logoId) {
        this.name = name;
        this.logoId = logoId;
    }

    public Venta() {
    }

    protected Venta(Parcel in) {
        name = in.readString();
        id = in.readString();
        descripcion = in.readString();
        costo = in.readString();
        telefono = in.readString();
        imagen = in.readString();
        logoId = in.readInt();
        id_colonia = in.readString();
        id_usu = in.readString();
        tipo = in.readString();
        aprobado = in.readInt();
      //  titulo = in.readString();


    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(descripcion);
        dest.writeString(costo);
        dest.writeString(telefono);
        dest.writeString(imagen);
        dest.writeInt(logoId);
        dest.writeString(id_colonia);
        dest.writeString(id_usu);
        dest.writeString(tipo);
        dest.writeInt(aprobado);
       // dest.writeString(titulo);

    }

    @Override
    public int describeContents() { return 0;    }

    public static final Creator<Venta> CREATOR = new Creator<Venta>() {
        @Override
        public Venta createFromParcel(Parcel in) {
            return new Venta(in);
        }

        @Override
        public Venta[] newArray(int size) {
            return new Venta[size];
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

    public String getId_colonia() {
        return id_colonia;
    }

    public void setId_colonia(String id_colonia) {
        this.id_colonia = id_colonia;
    }

    public String getId_usu() {
        return id_usu;
    }

    public void setId_usu(String id_usu) {
        this.id_usu = id_usu;
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

    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTelefono() { return telefono; }

    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCosto() { return costo; }

    public void setCosto(String costo) { this.costo = costo; }

    public int getAprobado() {
        return aprobado;
    }

    public void setAprobado(int aprobado) {
        this.aprobado = aprobado;
    }
}


