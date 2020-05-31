package com.micolonia.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.zolad.zoominimageview.ZoomInImageView;

import java.util.Map;

public class Imagen_venta extends AppCompatActivity {
    public static final int REQUEST_CALL = 1;
    private ImageView img_tel;
    private TextView descgeneral;
    private TextView costo;
    private TextView titulo;
    private TextView contacto;
    private ZoomInImageView imagenventa;
    private ImageView wha;
    private ImageView back;
    private ImageButton delete;
    public Button editar;

    //Firebase
    private DocumentReference usuRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    public String current_tipo, postId, current_colonia, propietario_post, id_usu;

    Venta venta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen_venta);
        getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        //email_current_user=user.getEmail();
        usuRef=db.collection("usuarios").document(user.getUid());

        editar = findViewById(R.id.btn_editar_venta);
        delete = findViewById(R.id.delete_venta);
        back=findViewById(R.id.backbtnven);
        img_tel=findViewById(R.id.img_tel);
        wha= findViewById(R.id.wha);
        costo=findViewById(R.id.costo_venta);
        descgeneral=findViewById(R.id.desc_venta);
        titulo=findViewById(R.id.titulo_venta);
        imagenventa=findViewById(R.id.imagen_pub_venta);
        contacto=findViewById(R.id.contacto_venta);

        final Venta venta = getIntent().getParcelableExtra("publicacion");

        titulo.setText(venta.getName());
        postId = venta.getId();
        descgeneral.setText(venta.getDescripcion());
        costo.setText("$ " + venta.getCosto());
        contacto.setText(": "+venta.getTelefono());
        propietario_post = venta.getId_usu();

        //Editar
        editar.setVisibility(View.GONE);
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
                gettingColonia(0);
            }
        });

       // Toast.makeText(this, venta.getId(), Toast.LENGTH_SHORT).show();

        wha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsapp(v);
            }
        });

        img_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamada();
            }
        });

        contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamada();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backven();
            }
        });



        if(venta.getImagen()!=null){
        Glide.with(this).load(venta.getImagen()).into(imagenventa);
        }
        else if(venta.getTipo()=="2"){
            imagenventa.setImageResource(R.drawable.casa);
        }else{
            imagenventa.setImageResource(R.drawable.logonegropng);
        }

        imagenventa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageClick(venta);
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
                            //Toast.makeText(Perfil.this, current_tipo, Toast.LENGTH_SHORT).show();

                            if(opcion == 0){
                                deletePost(current_colonia);
                            }else if(opcion == 1){
                                EditPost(current_colonia, postId);
                            }


                        }else{
                            Toast.makeText(Imagen_venta.this,"No existe el id_colonia del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Imagen_venta.this,"Error!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Imagen_venta.this,"No existe el id_colonia del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Imagen_venta.this,"Error!", Toast.LENGTH_SHORT).show();
                    }
                });
        return current_tipo;
    }

    private void EditPost(String c_colonia, String id_post){
        Intent intedit = new Intent(this, Formato_pub_venta.class);
        Bundle bundle = new Bundle();
        bundle.putString("post_id", id_post);
        bundle.putString("colonia", c_colonia);
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
                final ProgressDialog progressDialog = new ProgressDialog(Imagen_venta.this);
                progressDialog.setMessage("Cargando");
                progressDialog.show();


                if (colonia.equals("1")){
                    db.collection("ventas_las_hadas").document(postId).delete();
                    progressDialog.dismiss();
                    backven();

                }else if(colonia.equals("2")){
                    db.collection("ventas_mision_anahuac").document(postId).delete();
                    progressDialog.dismiss();
                    backven();

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


    private void backven(){
        Intent intentb = new Intent(this, seccion_ventas.class);
        startActivity(intentb);
        finish();
    }

    public void whatsapp(View view){

        Venta venta = getIntent().getParcelableExtra("venta");
        String numero = venta.getTelefono().trim();
        String text ="Hola!";


        try {


            Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+numero +"&text="+text));
            startActivity(intent);

        } catch (Exception e){
            e.printStackTrace();

        }

    }

    private void llamada(){
        Venta venta = getIntent().getParcelableExtra("venta");
        if(ContextCompat.checkSelfPermission(Imagen_venta.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Imagen_venta.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }else{
            String telefono =venta.getTelefono().trim();
            String dial = "tel: "+telefono;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
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

    private void onImageClick(Venta venta){
        String imguri ="";
        if(venta.getImagen().isEmpty()){
            Toast.makeText(this, "No hay imagen", Toast.LENGTH_SHORT).show();

        }else{
            imguri = venta.getImagen();
        }
        Bundle bundle = new Bundle();
        bundle.putString("imagen", imguri);

        Intent fullscreen = new Intent(this, FullScreen_Ventas.class);
        fullscreen.putExtras(bundle);
        startActivity(fullscreen);
    }

}
