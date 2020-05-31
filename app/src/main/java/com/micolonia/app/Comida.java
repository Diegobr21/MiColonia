package com.micolonia.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class Comida extends AppCompatActivity {
    public static final int REQUEST_CALL = 1;
    private TextView descgeneral;
    private TextView Nom_negocio;
    private TextView horarios;
    private TextView direccion;
    private TextView contacto;
    private ImageView imagencom;
    private ImageView img_tel;
    private ImageView wha;
    private ImageView back;
    private ImageButton delete;
    public Button editar;
    //  protected ArrayList<String> platillos= new ArrayList<String>();
    //  protected ArrayList<String> descripciones= new ArrayList<String>();
    //  protected ArrayList<Integer> precios= new ArrayList<Integer>();
    //private double ratings[] = new double[6];
    private DocumentReference usuRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    // private String email_current_user;
    public String current_tipo, postId, current_colonia, id_usu, propietario_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comida);
        getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        //email_current_user=user.getEmail();
        usuRef=db.collection("usuarios").document(user.getUid());

        editar = findViewById(R.id.btn_editar_comida);
        delete = findViewById(R.id.delete_comida);
        back=findViewById(R.id.backbtncom);
        descgeneral = findViewById(R.id.desc_negcom);
        direccion = findViewById(R.id.direccion_negcom);
        Nom_negocio = findViewById(R.id.titulo_comida);
        horarios = findViewById(R.id.horarios_negcom);
        imagencom= findViewById(R.id.imagen_negociocom);
        contacto = findViewById(R.id.contacto_negcom);
        img_tel= findViewById(R.id.img_tel);
        wha=findViewById(R.id.wha);
        final NegocioCom comida = getIntent().getParcelableExtra("publicacion");
        postId = comida.getId();
        propietario_post = comida.getId_usu();

        //Editar
        editar.setVisibility(View.GONE);


        Nom_negocio.setText(comida.getName());

        if(comida.getImagen().isEmpty()){
            imagencom.setImageResource(R.drawable.foodsign);

        }else{
            Glide.with(this).load(comida.getImagen()).circleCrop().into(imagencom);
        }

        imagencom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageClick(comida);
            }
        });
        horarios.setText("Horario Activo: "+comida.getHorario());
        descgeneral.setText(comida.getDescripcion());
        img_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamada();
            }
        });
        wha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsapp(v);
            }
        });
        String telefono = comida.getTelefono().trim();
        String dir = comida.getDireccion().trim();
        if(!dir.isEmpty() && dir != null){
            direccion.setText("Dirección: " + dir);
        }else{
            direccion.setText("Dirección: N/A");
        }


        contacto.setText(": "+ telefono);
        contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamada();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backcom();
            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingColonia(1);
            }
        });

        //Delete
        delete.setVisibility(View.GONE);
        gettingTipo();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Comida.this, comida.getId_usu(), Toast.LENGTH_LONG).show();
                gettingColonia(0);
            }
        });

        //Toast.makeText(this, comida.getName(), Toast.LENGTH_SHORT).show();

      /*  FragmentoNegCom fragmentoNegCom = new FragmentoNegCom();
        Bundle args = new Bundle();
        args.putParcelable("comida", comida);
        fragmentoNegCom.setArguments(args);
*/
/*
        // TODO: Ejemplo de como deberia leer informacion de las comidas
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("comidainfo")
                .document("ABCDE12345")//El id del documento comida en la colección "comida".
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            // Leer datos.
                        } else {
                            // Error.
                        }
                    }
                });

                */
        // NOTA: Al agregar una comida, actualmente se guarda id, name y idType.
        // Note que id es el id de la comida, no del documento.
        // En la consola de Firebase, se ve como los id de documento son generados automáticamente.
        // Es el id que debería agregar en la colección comidainfo.

        // Es decir, así se relaciona la comida (comida) y su información (comidainfo).

        // Crea el fragmento
        FragmentoNegCom fragment = new FragmentoNegCom();
        // Crea los argumentos
        Bundle args = new Bundle();
        // Añade, en este caso, el objeto NegocioCom en los argumentos
        args.putParcelable("comida", comida);
        // Agrega esos argumentos al fragmento
        fragment.setArguments(args);
        // Finalmente carga el fragmento
        loadFragment(fragment);
    }

    public String gettingColonia(final int opcion){
        usuRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()){
                            Map<String, Object> usu = documentSnapshot.getData();
                            current_colonia = usu.get("colonia").toString().trim();
                            //Toast.makeText(Perfil.this, current_tipo, Toast.LENGTH_SHORT).show();
                            if(opcion == 0){
                                deletePost(current_colonia);
                            }else if(opcion == 1){
                                EditPost(current_colonia, postId);
                            }


                        }else{
                            Toast.makeText(Comida.this,"No existe el id_colonia del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Comida.this,"Error!", Toast.LENGTH_SHORT).show();
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
                            id_usu = mAuth.getCurrentUser().getUid();
                            if(id_usu.equals(propietario_post)){
                                editar.setVisibility(View.VISIBLE);
                            }

                        }else{
                            Toast.makeText(Comida.this,"No existe el id_colonia del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Comida.this,"Error!", Toast.LENGTH_SHORT).show();
                    }
                });
        return current_tipo;
    }

    private void regresarComida(){
        Intent intentc = new Intent(this, seccion_comida.class);
        startActivity(intentc);
        finish();
    }

    private void EditPost(String colonia, String id_post){
        Intent intedit = new Intent(this, Formato_pub_comida.class);
        Bundle bundle = new Bundle();
        bundle.putString("post_id", id_post);
        bundle.putString("colonia", colonia);
        intedit.putExtras(bundle);
        startActivity(intedit);

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
                final ProgressDialog progressDialog = new ProgressDialog(Comida.this);
                progressDialog.setMessage("Cargando");
                progressDialog.show();


                if (colonia.equals("1")){
                    db.collection("comida_las_hadas").document(postId).delete();
                    progressDialog.dismiss();
                    regresarComida();

                }else if(colonia.equals("2")){
                    db.collection("comida_mision_anahuac").document(postId).delete();
                    progressDialog.dismiss();
                    regresarComida();

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

    private void backcom(){
        Intent intentb = new Intent(this, seccion_comida.class);
        startActivity(intentb);
    }

    private void llamada(){
        NegocioCom comida = getIntent().getParcelableExtra("comida");
        if(ContextCompat.checkSelfPermission(Comida.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Comida.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }else{
            String telefono =comida.getTelefono().trim();
            String dial = "tel: "+telefono;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    public void whatsapp(View view){

        NegocioCom comida = getIntent().getParcelableExtra("comida");
        String numero = comida.getTelefono().trim();
        String text ="Hola!";


        try {


            Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+numero +"&text="+text));
            startActivity(intent);

        } catch (Exception e){
            e.printStackTrace();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CALL){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                llamada();
            }else {
                Toast.makeText(this, "No se ha concedido el permiso de llamada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*  protected double ratingenerator(){

        //comando SQL PARA TRAER LOS ULTIMOS 6 RATINGS DE EL RESTAURANTE CON EL ID
        double rating= (ratings[0]+ratings[1]+ratings[2]+ratings[3]+ratings[4]+ratings[5])/6 ;
        return rating;
    }
    */

    private void onImageClick(NegocioCom comida){
        String imguri ="";
        if(comida.getImagen().isEmpty()){
            Toast.makeText(this, "No hay imagen", Toast.LENGTH_SHORT).show();

        }else{
            imguri = comida.getImagen();
        }
        Bundle bundle = new Bundle();
        bundle.putString("imagen", imguri);

        Intent fullscreen = new Intent(this, FullScreen_Comida.class);
        fullscreen.putExtras(bundle);
        startActivity(fullscreen);
    }

   private void loadFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_negociocomida, fragment);
        transaction.commit();
    }
}