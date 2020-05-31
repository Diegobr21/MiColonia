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

public class Formato_pub_serv extends AppCompatActivity {
    private EditText nombre_negcom, horarios, descripcion, edtdireccion,  contac_negcom, s1,s2,s3,s4,s5,s6;
    private Button inicio, guardar, actualizar;

    private ImageButton addpic;
    private ImageView pic;
    public static final int GALLERY_REQUEST_CODE = 105;
    public String imagen = "";
    public String current_colonia, id_usu;
    private String dir = "N/A";
    public String  tipo,  colonia_bundle="", post_id_bundle="";
    public String set_imagen, _nombre_negocio,_horario,_celular, _direccion, _descripcion_b, _elemento1,
            _elemento2, _elemento3, _elemento4, _elemento5, _elemento6;
    public boolean new_img_upload = false;
    //String currentPhotoPath;
    StorageReference storageReference;
    private DocumentReference usuRef, postRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formato_pub_serv);

        getSupportActionBar().hide();
        nombre_negcom= findViewById(R.id.nombre_negserv);
        horarios=findViewById(R.id.horarios_negserv);
        descripcion=findViewById(R.id.desc_negservicio);
        edtdireccion = findViewById(R.id.dir_negservicio);
        inicio=findViewById(R.id.btn_ini3);
        contac_negcom = findViewById(R.id.contac_negservicio);


        addpic = findViewById(R.id.imgbtn_add_pic_servicio);
        pic = findViewById(R.id.img_add_servicio);
        guardar = findViewById(R.id.btn_formato_servicio);
        actualizar = findViewById(R.id.btn_actualizar_servicios);
        actualizar.setVisibility(View.GONE);
        s1 = findViewById(R.id.servicio1);
        s2 = findViewById(R.id.servicio2);
        s3 = findViewById(R.id.servicio3);
        s4= findViewById(R.id.servicio4);
        s5 = findViewById(R.id.servicio5);
        s6 = findViewById(R.id.servicio6);

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


    private void EditPost(String c_colonia, String post_id){
        inicio.setVisibility(View.GONE);
        guardar.setVisibility(View.GONE);
        actualizar.setVisibility(View.VISIBLE);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando");
        progressDialog.show();
        //Toast.makeText(this, post_id_bundle, Toast.LENGTH_LONG).show();
        if(c_colonia.equals("1")){
            db.collection("servicios_las_hadas").document(post_id)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if (documentSnapshot.exists()) {
                                Map<String, Object> post = documentSnapshot.getData();
                                _nombre_negocio = post.get("name").toString().trim();
                                _horario = post.get("horario").toString().trim();
                                _celular = post.get("telefono").toString().trim();
                                _direccion = post.get("direccion").toString().trim();
                                _descripcion_b = post.get("descripcion").toString().trim();
                                set_imagen = post.get("imagen").toString().trim();
                                _elemento1 = post.get("elemento1").toString().trim();
                                _elemento2 = post.get("elemento2").toString().trim();
                                _elemento3 = post.get("elemento3").toString().trim();
                                _elemento4 = post.get("elemento4").toString().trim();
                                _elemento5 = post.get("elemento5").toString().trim();
                                _elemento6 = post.get("elemento6").toString().trim();

                                nombre_negcom.setText(_nombre_negocio);
                                horarios.setText(_horario);
                                contac_negcom.setText(_celular);
                                edtdireccion.setText(_direccion);
                                descripcion.setText(_descripcion_b);
                                Glide.with(Formato_pub_serv.this).load(set_imagen).into(pic);
                                s1.setText(_elemento1);
                                s2.setText(_elemento2);
                                s3.setText(_elemento3);
                                s4.setText(_elemento4);
                                s5.setText(_elemento5);
                                s6.setText(_elemento6);
                                progressDialog.dismiss();

                            }else{
                                Toast.makeText(Formato_pub_serv.this,"No se reconoció el id del post",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Formato_pub_serv.this,"Error!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else if(c_colonia.equals("2")){

            db.collection("servicios_mision_anahuac").document(post_id)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if (documentSnapshot.exists()) {
                                Map<String, Object> post = documentSnapshot.getData();
                                _nombre_negocio = post.get("name").toString().trim();
                                _horario = post.get("horario").toString().trim();
                                _celular = post.get("telefono").toString().trim();
                                _direccion = post.get("direccion").toString().trim();
                                _descripcion_b = post.get("descripcion").toString().trim();
                                set_imagen = post.get("imagen").toString().trim();
                                _elemento1 = post.get("elemento1").toString().trim();
                                _elemento2 = post.get("elemento2").toString().trim();
                                _elemento3 = post.get("elemento3").toString().trim();
                                _elemento4 = post.get("elemento4").toString().trim();
                                _elemento5 = post.get("elemento5").toString().trim();
                                _elemento6 = post.get("elemento6").toString().trim();

                                nombre_negcom.setText(_nombre_negocio);
                                horarios.setText(_horario);
                                contac_negcom.setText(_celular);
                                edtdireccion.setText(_direccion);
                                descripcion.setText(_descripcion_b);
                                Glide.with(Formato_pub_serv.this).load(set_imagen).into(pic);
                                s1.setText(_elemento1);
                                s2.setText(_elemento2);
                                s3.setText(_elemento3);
                                s4.setText(_elemento4);
                                s5.setText(_elemento5);
                                s6.setText(_elemento6);
                                progressDialog.dismiss();

                            }else{
                                Toast.makeText(Formato_pub_serv.this,"No se reconoció el id del post",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Formato_pub_serv.this,"Error!", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    //Solo se llama si se está editando el post
    private void Actualizar_Edited(String uid, String colonia_id, String post_id) {
        String nombre_negcom = this.nombre_negcom.getText().toString().trim();
        String horarios_negcom = this.horarios.getText().toString().trim();
        String contac_negcom = this.contac_negcom.getText().toString().trim();
        String desc_negcom = this.descripcion.getText().toString().trim();
        String direccion = this.edtdireccion.getText().toString().trim();
        String url = set_imagen;
        String platillo1 = this.s1.getText().toString().trim();
        String platillo2 = this.s2.getText().toString().trim();
        String platillo3 = this.s3.getText().toString().trim();
        String platillo4 = this.s4.getText().toString().trim();
        String platillo5 = this.s5.getText().toString().trim();
        String platillo6 = this.s6.getText().toString().trim();

        if (nombre_negcom != _nombre_negocio || horarios_negcom != _horario ||
                contac_negcom != _celular || desc_negcom != _descripcion_b || direccion!= _direccion || platillo1 != _elemento1
                || platillo2 != _elemento2 || platillo3 != _elemento3 || platillo4 != _elemento4 || platillo5 != _elemento5
                || platillo6 != _elemento6 || imagen != url) {
            if(colonia_id.equals("1")){
                postRef = db.collection("servicios_las_hadas").document(post_id);
            }else if(colonia_id.equals("2")){
                postRef = db.collection("servicios_mision_anahuac").document(post_id);
            }


            if (new_img_upload){
                url = imagen;
            }
            postRef.update("name", nombre_negcom);
            postRef.update("horario", horarios_negcom);
            postRef.update("telefono", contac_negcom);
            postRef.update("descripcion", desc_negcom);
            postRef.update("direccion", direccion);
            postRef.update("imagen", url);
            postRef.update("elemento1", platillo1);
            postRef.update("elemento2", platillo2);
            postRef.update("elemento3", platillo3);
            postRef.update("elemento4", platillo4);
            postRef.update("elemento5", platillo5);
            postRef.update("elemento6", platillo6);

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
        String horarios_negcom = this.horarios.getText().toString().trim();
        String contac_negcom = this.contac_negcom.getText().toString().trim();
        String desc_negcom = this.descripcion.getText().toString().trim();
        String direccion_ser = this.edtdireccion.getText().toString().trim();
        String url = "";
        String servicio1 =this.s1.getText().toString().trim();
        String servicio2 =this.s2.getText().toString().trim();
        String servicio3 =this.s3.getText().toString().trim();
        String servicio4 =this.s4.getText().toString().trim();
        String servicio5 =this.s5.getText().toString().trim();
        String servicio6 =this.s6.getText().toString().trim();


        if (nombre_negcom.isEmpty() || horarios_negcom.isEmpty() ||
                contac_negcom.isEmpty() || desc_negcom.isEmpty()) {
            Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            if (pic.getDrawable()!=null){
                url = imagen;}

            if(direccion_ser.isEmpty()){
                direccion_ser = dir;
            }

            HashMap<String, Object> data = new HashMap<>();
            data.put("timestamp", System.currentTimeMillis());
            data.put("name", nombre_negcom);
            data.put("horario", horarios_negcom);
            data.put("telefono", contac_negcom);
            data.put("descripcion", desc_negcom);
            data.put("direccion", direccion_ser);
            data.put("id_usu", id_usu);
            if (servicio1.isEmpty()){data.put("elemento1", "");}else {
                data.put("elemento1", servicio1);
            }
            if (servicio2.isEmpty()){data.put("elemento2", "");}else {
                data.put("elemento2", servicio2);
            }
            if (servicio3.isEmpty()){data.put("elemento3", "");}else {
                data.put("elemento3", servicio3);
            }
            if (servicio4.isEmpty()){data.put("elemento4", "");}else {
                data.put("elemento4", servicio4);
            }
            if (servicio5.isEmpty()){data.put("elemento5", "");}else {
                data.put("elemento5", servicio5);
            }
            if (servicio6.isEmpty()){data.put("elemento6", "");}else {
                data.put("elemento6", servicio6);
            }

            data.put("imagen", url);
            // Este campo es importante, hará que no se muestre en la app hasta su aprobación.
            data.put("visible", 0);

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Cargando");
            dialog.show();

            if (colonia.equals("1")){
                db.collection("servicios_las_hadas")
                        .add(data)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                dialog.dismiss();

                                if (task.isSuccessful()) {
                                    Formato_pub_serv.this.nombre_negcom.getText().clear();
                                    Formato_pub_serv.this.horarios.getText().clear();
                                    Formato_pub_serv.this.contac_negcom.getText().clear();
                                    Formato_pub_serv.this.descripcion.getText().clear();
                                    Formato_pub_serv.this.edtdireccion.getText().clear();
                                    Formato_pub_serv.this.s1.getText().clear();
                                    Formato_pub_serv.this.s2.getText().clear();
                                    Formato_pub_serv.this.s3.getText().clear();
                                    Formato_pub_serv.this.s4.getText().clear();
                                    Formato_pub_serv.this.s5.getText().clear();
                                    Formato_pub_serv.this.s6.getText().clear();

                                    Toast.makeText(Formato_pub_serv.this,
                                            "Su publicación fue exitosa",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Formato_pub_serv.this,
                                            "Algo no fue bien, intenten de nuevo más tarde",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }else if (colonia.equals("2")){
                db.collection("servicios_mision_anahuac")
                        .add(data)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                dialog.dismiss();

                                if (task.isSuccessful()) {
                                    Formato_pub_serv.this.nombre_negcom.getText().clear();
                                    Formato_pub_serv.this.horarios.getText().clear();
                                    Formato_pub_serv.this.contac_negcom.getText().clear();
                                    Formato_pub_serv.this.descripcion.getText().clear();
                                    Formato_pub_serv.this.s1.getText().clear();
                                    Formato_pub_serv.this.s2.getText().clear();
                                    Formato_pub_serv.this.s3.getText().clear();
                                    Formato_pub_serv.this.s4.getText().clear();
                                    Formato_pub_serv.this.s5.getText().clear();
                                    Formato_pub_serv.this.s6.getText().clear();

                                    Toast.makeText(Formato_pub_serv.this,
                                            "Su publicación fue exitosa",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Formato_pub_serv.this,
                                            "Algo no fue bien, intenten de nuevo más tarde",
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
        final StorageReference image = storageReference.child("logos_servicios/" + name);
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
                Toast.makeText(Formato_pub_serv.this, "Imagen Recibida", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Formato_pub_serv.this, "Upload Failled.", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Formato_pub_serv.this,"No existe el id_colonia del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Formato_pub_serv.this,"Error!", Toast.LENGTH_SHORT).show();
                    }
                });
        return current_colonia;
    }

    public void RegresarInicio(){
        Intent intent = new Intent(this, Inicio.class);
        startActivity(intent);
        finish();
    }
}