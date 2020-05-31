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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
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

public class seccion_comida extends AppCompatActivity implements RecyclerViewAdapter_comida.OnNegocioListener,
        NavigationView.OnNavigationItemSelectedListener {
    private AdapterViewFlipper flip_comida;
    public Button noticias;
    ImageView perfil;
    private DrawerLayout drawercomida;
    FloatingActionButton publicar;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView_com;
    Context context;
    private List<NegocioCom> arraycomida;
    private List<Imagen> arrayimagenes;
    public String current_colonia, current_tipo;

    private DocumentReference usuRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seccion_comida);
       // getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        usuRef=db.collection("usuarios").document(user.getUid());

        refreshLayout=findViewById(R.id.refresh_layout_com);
        recyclerView_com = (RecyclerView) findViewById(R.id.recyclerView_comida);
        recyclerView_com.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView_com.setLayoutManager(layoutManager);
        RecyclerViewAdapter_comida adapter_comida = new RecyclerViewAdapter_comida(arraycomida, this);

        gettingColonia();
        addImages();

        //Toolbar
        Toolbar toolbar = findViewById(R.id.comida_toolbar);
        setSupportActionBar(toolbar);

        drawercomida = findViewById(R.id.nav_drawerlayoutCOM);
        NavigationView navigationView = findViewById(R.id.navigation_comida);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawercomida, toolbar,
                R.string.drawer_abrir, R.string.drawer_cierre);
        drawercomida.addDrawerListener(toggle);
        toggle.syncState();

        publicar = findViewById(R.id.floatingpublicar);
        flip_comida = (AdapterViewFlipper) findViewById(R.id.flipper_comida);
        noticias = findViewById(R.id.btn_inicio);
        // servicio = findViewById(R.id.btn_servicio);
        // ventas = findViewById(R.id.btn_ventas);
        //perfil = findViewById(R.id.btn_ing_perfil);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gettingColonia();
                addImages();
            }
        });

        /*
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mandar a validar usuario y contraseña
                //saber que usuario es
                //  Log.d("Las Hadas", "Bienvenid@");//agregar nombre de usuario en Bienvenido
                verperfil();
            }
        });

         */
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
                                Toast.makeText(seccion_comida.this, "No es jefe de colonos", Toast.LENGTH_LONG).show();
                            }

                        }else{
                            Toast.makeText(seccion_comida.this,"No existe el id_colonia del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(seccion_comida.this,"Error!", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    public void onBackPressed() {
        if(drawercomida.isDrawerOpen(GravityCompat.START)){
            drawercomida.closeDrawer(GravityCompat.START);
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
    public void verperfil() {
        Intent intentperfil = new Intent(this, Perfil.class);
        intentperfil.putExtra("Remitente","DesdeComida");
        startActivity(intentperfil);
    }

     */
    public void vernoticias() {
        Intent intentvernoti = new Intent(this, Inicio.class);

        startActivity(intentvernoti);
    }



    public void verpublicar() {
        Intent intentpublica = new Intent(this, Publicar.class);

        startActivity(intentpublica);
    }

    /**
     * TODO: Add data
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
                            Toast.makeText(seccion_comida.this,"No existe el id_colonia del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(seccion_comida.this,"Error!", Toast.LENGTH_SHORT).show();
                    }
                });
        return current_colonia;
    }

    private void addData(String colonia) {
        // Los datos en Firestore deben tener la estructura de modelo en JSON.
        // Usando el objeto Aviso, su estructura es así:
        // {name: "Nombre", logoId: 1}
        // logoID equivale a dos valores, usaremos números y en la app
        // evaluamos esos números para colocar la imagen correcta.

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando");
        progressDialog.show();
        refreshLayout.setRefreshing(false);
        if(colonia.equals("1")){
            db.collection("comida_las_hadas")
                    //.whereEqualTo("id_colonia", colonia)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    //.whereEqualTo("visible", 1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arraycomida = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    NegocioCom negocioCom = doc.toObject(NegocioCom.class);
                                    negocioCom.id= doc.getId();
                                    arraycomida.add(negocioCom);
                                }

                                RecyclerViewAdapter_comida adapter_comida =
                                        new RecyclerViewAdapter_comida(
                                                arraycomida,
                                                seccion_comida.this
                                        );
                                recyclerView_com.setAdapter(adapter_comida);
                            } else {
                                Toast.makeText(seccion_comida.this, "Error", Toast.LENGTH_SHORT).show();
                                refreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });
        }else if(colonia.equals("2")){
            db.collection("comida_mision_anahuac")
                    //.whereEqualTo("id_colonia", colonia)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    //.whereEqualTo("visible", 1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                arraycomida = new ArrayList<>();

                                for (DocumentSnapshot doc : task.getResult()) {
                                    NegocioCom negocioCom = doc.toObject(NegocioCom.class);
                                    negocioCom.id= doc.getId();
                                    arraycomida.add(negocioCom);
                                }

                                RecyclerViewAdapter_comida adapter_comida =
                                        new RecyclerViewAdapter_comida(
                                                arraycomida,
                                                seccion_comida.this
                                        );
                                recyclerView_com.setAdapter(adapter_comida);
                            } else {
                                Toast.makeText(seccion_comida.this, "Error", Toast.LENGTH_SHORT).show();
                                refreshLayout.setRefreshing(false);
                            }

                            progressDialog.dismiss();
                        }
                    });
        }


    }

    public void addImages(){
        db.collection("imagenes")
                .whereEqualTo("idType", 2)
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
                            setFlip_comida();

                        } else {
                            Toast.makeText(seccion_comida.this, "No hay imagenes que cargar",
                                    Toast.LENGTH_LONG).show();
                            refreshLayout.setRefreshing(false);
                        }

                    }
                });


    }



    public void setFlip_comida(){
        CustomAdapter_com customAdapter_com = new CustomAdapter_com(getApplicationContext(), arrayimagenes);
        flip_comida.setAdapter(customAdapter_com);
        //flip_ventas.setFlipInterval(2700);
        // flip_ventas.setAutoStart(true);
    }

    public void onFlipperArrowLeftClick(View view) {
        if (flip_comida != null) {
            flip_comida.showPrevious();
        }
    }

    public void onFlipperArrowRightClick(View view) {
        if (flip_comida != null) {
            flip_comida.showNext();
        }
    }


    @Override
    public void onNegocioClick(int position, NegocioCom negocioCom) {
        arraycomida.get(position);
        //checar y mandar al negocio correspondiente
        Intent vernegocio_com_intent = new Intent(this, Comida.class);
        vernegocio_com_intent.putExtra("publicacion", negocioCom);
        startActivity(vernegocio_com_intent);
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

        drawercomida.closeDrawer(GravityCompat.START);
        return true;
    }
}


//CLASE PARA EL FLIPPER-------------------------------------------------------------
class CustomAdapter_com extends BaseAdapter {
    Context context;
    /*private */final List<Imagen> arrayimagenes;

    LayoutInflater inflater;



    public CustomAdapter_com(Context applicationContext, List <Imagen> arrayimagenes) {
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
        view = inflater.inflate(R.layout.imagen_flipper_comida, null);
        ImageView image = (ImageView) view.findViewById(R.id.imagencomidas);
        int tipo = arrayimagenes.get(position).getIdType();
        if(tipo== 2){
            Glide.with(view).load(arrayimagenes.get(position).getUrl()).into(image);
        }

        // image.setImageResource(images_ven[position]);
        return view;

    } }

class NegocioCom implements Parcelable {

    public static final int LOGO_ID_TYPE_WRECK = 1;
    public static final int LOGO_ID_TYPE_FOOD = 2;

    String name;
    int logoId;
    String id;
    String id_colonia;
    String imagen;
    String telefono;
    String descripcion;
    String direccion;
    String horario;
    String elemento1;
    String elemento2;
    String elemento3;
    String elemento4;
    String elemento5;
    String elemento6;
    String id_usu;


    NegocioCom(String name, int logoId) {
        this.name = name;
        this.logoId = logoId;
    }

    public NegocioCom() {
    }

    protected NegocioCom(Parcel in) {
        name = in.readString();
        logoId = in.readInt();
        imagen = in.readString();
        id = in.readString();
        id_colonia = in.readString();
        telefono = in.readString();
        descripcion = in.readString();
        direccion = in.readString();
        horario = in.readString();
        elemento1 = in.readString();
        elemento2 = in.readString();
        elemento3 = in.readString();
        elemento4 = in.readString();
        elemento5 = in.readString();
        elemento6 = in.readString();
        id_usu = in.readString();

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
        dest.writeString(direccion);
        dest.writeString(horario);
        dest.writeString(elemento1);
        dest.writeString(elemento2);
        dest.writeString(elemento3);
        dest.writeString(elemento4);
        dest.writeString(elemento5);
        dest.writeString(elemento6);
        dest.writeString(id_usu);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NegocioCom> CREATOR = new Creator<NegocioCom>() {
        @Override
        public NegocioCom createFromParcel(Parcel in) {
            return new NegocioCom(in);
        }

        @Override
        public NegocioCom[] newArray(int size) {
            return new NegocioCom[size];
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getId_usu() {
        return id_usu;
    }

    public void setId_usu(String id_usu) {
        this.id_usu = id_usu;
    }

    public String getId_colonia() {
        return id_colonia;
    }

    public void setId_colonia(String id_colonia) {
        this.id_colonia = id_colonia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setElemento5(String elemento5) {
        this.elemento5 = elemento5;
    }

    public String getElemento6() {
        return elemento6;
    }

    public void setElemento6(String elemento6) {
        this.elemento6 = elemento6;
    }
}
