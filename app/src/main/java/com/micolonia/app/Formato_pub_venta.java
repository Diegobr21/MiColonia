package com.micolonia.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Formato_pub_venta extends AppCompatActivity {

    private EditText nombre_negcom, contacto, descripcion, precio;
   private Button inicio, guardar, actualizar;
   private RadioGroup radioGroup;
   private RadioButton radioButton;
    private ImageButton addpic;
    private ImageView pic;
    public static final int GALLERY_REQUEST_CODE = 105;
    public String imagen = "";
    public String current_colonia, selectedType, id_usu, colonia_bundle="", post_id_bundle="";
    public String set_imagen, _nombre_aviso,_celular, _precio, _descripcion_b;
    public boolean new_img_upload = false;
    //String currentPhotoPath;
    StorageReference storageReference;
    private DocumentReference usuRef, postRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formato_pub_venta);
        getSupportActionBar().hide();

        nombre_negcom= findViewById(R.id.Titulo);
        contacto=findViewById(R.id.contac_formato_venta);
        descripcion=findViewById(R.id.desc_formato_venta);
        inicio= findViewById(R.id.btn_3_v);
        precio=findViewById(R.id.precio_formato_venta);
        addpic = findViewById(R.id.imgbtn_add_pic_venta);
        pic = findViewById(R.id.img_add_venta);
        radioGroup = findViewById(R.id.radiogroup);
        guardar = findViewById(R.id.btn_formato_venta);
        actualizar = findViewById(R.id.btn_actualizar_venta);
        actualizar.setVisibility(View.GONE);


        db= FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        usuRef=db.collection("usuarios").document(user.getUid());
        storageReference = FirebaseStorage.getInstance().getReference();

        //Editar
        Bundle b = getIntent().getExtras();
        if(b != null){
            colonia_bundle = b.getString("colonia");
            post_id_bundle = b.getString("post_id");
            if(!colonia_bundle.isEmpty() && !post_id_bundle.isEmpty()){
                //postRef=db.collection("").document(post_id_bundle);
                EditPost(colonia_bundle, post_id_bundle);

                final String usuario = user.getUid();
                actualizar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Actualizar_Edited(usuario, colonia_bundle, post_id_bundle);
                    }
                });
            }
        }


        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingColonia();
            }
        });

        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RegresarInicio();
            }
        });

        addpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

    }

    public void checkRadioBtn(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        selectedType = radioButton.getText().toString().trim();
        if(selectedType.isEmpty()){
            selectedType = "Venta";
        }
        //Toast.makeText(this, selectedType, Toast.LENGTH_SHORT).show();
    }

    private void EditPost(String c_colonia, String post_id){
        inicio.setVisibility(View.GONE);
        guardar.setVisibility(View.GONE);
        actualizar.setVisibility(View.VISIBLE);
        radioGroup.setVisibility(View.GONE);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando");
        progressDialog.show();
        //Toast.makeText(this, post_id_bundle, Toast.LENGTH_LONG).show();
        if(c_colonia.equals("1")){
            db.collection("ventas_las_hadas").document(post_id)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if (documentSnapshot.exists()) {
                                Map<String, Object> post = documentSnapshot.getData();
                                _nombre_aviso = post.get("name").toString().trim();
                                _precio = post.get("costo").toString().trim();
                                _celular = post.get("telefono").toString().trim();
                                _descripcion_b = post.get("descripcion").toString().trim();
                                set_imagen = post.get("imagen").toString().trim();


                                nombre_negcom.setText(_nombre_aviso);
                                precio.setText(_precio);
                                contacto.setText(_celular);
                                descripcion.setText(_descripcion_b);
                                Glide.with(Formato_pub_venta.this).load(set_imagen).into(pic);

                                progressDialog.dismiss();

                            }else{
                                Toast.makeText(Formato_pub_venta.this,"No se reconoció el id del post",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Formato_pub_venta.this,"Error!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else if(c_colonia.equals("2")){

            db.collection("ventas_mision_anahuac").document(post_id)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if (documentSnapshot.exists()) {
                                Map<String, Object> post = documentSnapshot.getData();
                                _nombre_aviso = post.get("name").toString().trim();
                                _precio = post.get("costo").toString().trim();
                                _celular = post.get("telefono").toString().trim();
                                _descripcion_b = post.get("descripcion").toString().trim();
                                set_imagen = post.get("imagen").toString().trim();

                                nombre_negcom.setText(_nombre_aviso);
                                precio.setText(_precio);
                                contacto.setText(_celular);
                                descripcion.setText(_descripcion_b);
                                Glide.with(Formato_pub_venta.this).load(set_imagen).into(pic);


                                progressDialog.dismiss();

                            }else{
                                Toast.makeText(Formato_pub_venta.this,"No se reconoció el id del post",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Formato_pub_venta.this,"Error!", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    //Solo se llama si se está editando el post
    private void Actualizar_Edited(String uid, String colonia_id, String post_id) {
        String nombre_negcom = this.nombre_negcom.getText().toString().trim();
        String costo = this.precio.getText().toString().trim();
        String contac_negcom = this.contacto.getText().toString().trim();
        String desc_negcom = this.descripcion.getText().toString().trim();
        String url = set_imagen;


        if (nombre_negcom != _nombre_aviso || costo != _precio ||
                contac_negcom != _celular || desc_negcom != _descripcion_b || imagen != url) {
            if(colonia_id.equals("1")){
                postRef = db.collection("ventas_las_hadas").document(post_id);
            }else if(colonia_id.equals("2")){
                postRef = db.collection("ventas_mision_anahuac").document(post_id);
            }


            if (new_img_upload){
                url = imagen;
            }
            postRef.update("name", nombre_negcom);
            postRef.update("costo", costo);
            postRef.update("telefono", contac_negcom);
            postRef.update("descripcion", desc_negcom);
            postRef.update("imagen", url);


            Toast.makeText(this, "Se han actualizado los datos", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "No se ha modificado ningún campo", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == GALLERY_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp +"."+ getFileExt(contentUri);
                //Toast.makeText(this, "onActivityResult: Gallery Image Uri:  " +  imageFileName, Toast.LENGTH_LONG).show();
                pic.setImageURI(contentUri);

                uploadImageToFirebase(imageFileName,contentUri);


            }

        }
    }

    public void ToastGuardar(String colonia, String id_usu){
        String nombre_negcom = this.nombre_negcom.getText().toString().trim();
        String precio = this.precio.getText().toString().trim();
        String contac_negcom = this.contacto.getText().toString().trim();
        String desc_negcom = this.descripcion.getText().toString().trim();
        String url = "";
        String idType = "";

        if (nombre_negcom.isEmpty() || precio.isEmpty() ||
                contac_negcom.isEmpty() || desc_negcom.isEmpty()) {
            Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            if (pic.getDrawable()!=null){
                url = imagen;}
            if (selectedType.equals("Bienes Raices")){
                idType = "2";
            }else if(selectedType.equals("Venta")){
                idType = "1";
            }else{
                idType = "1";
            }


            HashMap<String, Object> data = new HashMap<>();
            data.put("timestamp", System.currentTimeMillis());
            data.put("name", nombre_negcom);
            data.put("costo", precio);
            data.put("telefono", contac_negcom);
            data.put("descripcion", desc_negcom);
            data.put("imagen", url);
            data.put("idType", idType);
            data.put("id_usu", id_usu);
            // Este campo es importante, hará que no se muestre en la app hasta su aprobación.
            data.put("aprobado", 0);

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Cargando");
            dialog.show();

            if (colonia.equals("1")){
                db.collection("ventas_las_hadas")
                        .add(data)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                dialog.dismiss();

                                if (task.isSuccessful()) {
                                    Formato_pub_venta.this.nombre_negcom.getText().clear();
                                    Formato_pub_venta.this.precio.getText().clear();
                                    Formato_pub_venta.this.contacto.getText().clear();
                                    Formato_pub_venta.this.descripcion.getText().clear();

                                    Toast.makeText(Formato_pub_venta.this,
                                            "Su publicación fue exitosa",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Formato_pub_venta.this,
                                            "Algo no fue bien, intente de nuevo más tarde",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }else if (colonia.equals("2")){
                db.collection("ventas_mision_anahuac")
                        .add(data)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                dialog.dismiss();

                                if (task.isSuccessful()) {
                                    Formato_pub_venta.this.nombre_negcom.getText().clear();
                                    Formato_pub_venta.this.precio.getText().clear();
                                    Formato_pub_venta.this.contacto.getText().clear();
                                    Formato_pub_venta.this.descripcion.getText().clear();

                                    Toast.makeText(Formato_pub_venta.this,
                                            "Su publicación fue exitosa",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Formato_pub_venta.this,
                                            "Algo no fue bien, intente de nuevo más tarde",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }

        }
    }

    private void uploadImageToFirebase(String name, final Uri contentUri) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando");
        progressDialog.show();
        final StorageReference image = storageReference.child("logos_ventas/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
                        imagen = uri.toString();
                        new_img_upload = true;
                    }
                });

                progressDialog.dismiss();
                Toast.makeText(Formato_pub_venta.this, "Imagen Recibida", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Formato_pub_venta.this, "Upload Failled.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    public String gettingColonia(){
        usuRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()){
                            Map<String, Object> usu = documentSnapshot.getData();
                            current_colonia = usu.get("colonia").toString().trim();
                            id_usu = mAuth.getCurrentUser().getUid();
                            ToastGuardar(current_colonia, id_usu);

                        }else{
                            Toast.makeText(Formato_pub_venta.this,"No existe el id_colonia del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Formato_pub_venta.this,"Error!", Toast.LENGTH_SHORT).show();
                    }
                });
        return current_colonia;
    }


    public void RegresarInicio(){
        Intent intent = new Intent(this, Inicio.class);
        startActivity(intent);
    }

}