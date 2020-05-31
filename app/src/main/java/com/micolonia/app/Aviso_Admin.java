package com.micolonia.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.HashMap;

public class Aviso_Admin extends AppCompatActivity {

    EditText contenido;
    Button publicar_aviso_admin, regresoIni;
    Switch act_comentarios;
    TextView fecha;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    public String comentarios_acts = "no";
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviso__admin);

        db= FirebaseFirestore.getInstance();

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        act_comentarios = findViewById(R.id.switch_comentarios);
        contenido = findViewById(R.id.contenido_aviso);
        fecha = findViewById(R.id.fecha_aviso);

        getFecha();

        publicar_aviso_admin = findViewById(R.id.btn_publicar_aviso_admin);
        regresoIni = findViewById(R.id.rgr_inicio);

        act_comentarios.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    comentarios_acts = "si";
                }
                else{
                    comentarios_acts = "no";
                }
            }
        });

        regresoIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regreso_Inicio();
            }
        });


        publicar_aviso_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String current_colonia = getIntent().getStringExtra("colonia");
                if (current_colonia.equals("Las Hadas")){
                    final String colonia ="1";
                    publicarAdmin(colonia);
                }else if(current_colonia.equals("Mision Anahuac")){
                    final String colonia ="2";
                    publicarAdmin(colonia);
                }

            }
        });


    }

    private void getFecha(){
        fecha.setText("Fecha: " + date);
        //Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
    }
    private void publicarAdmin(String c_colonia) {
        String contenido_aviso = this.contenido.getText().toString().trim();
        String fecha_aviso = this.date;


        if (contenido_aviso.isEmpty() || fecha_aviso.isEmpty()) {
            Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> data = new HashMap<>();
            data.put("timestamp", System.currentTimeMillis());
            data.put("name", contenido_aviso);
            data.put("fecha", fecha_aviso);
            data.put("id_colonia", c_colonia);
            data.put("comentarios", comentarios_acts);
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Cargando");
            dialog.show();

            if (c_colonia.equals("1")){
                db.collection("avisos_las_hadas")
                        .add(data)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                dialog.dismiss();

                                if (task.isSuccessful()) {
                                    Aviso_Admin.this.contenido.getText().clear();



                                    Toast.makeText(Aviso_Admin.this,
                                            "Su publicación estará en inicio",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Aviso_Admin.this,
                                            "Algo no salió bien, intente de nuevo más tarde",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }else if(c_colonia.equals("2")){
                db.collection("avisos_mision_anahuac")
                        .add(data)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                dialog.dismiss();

                                if (task.isSuccessful()) {
                                    Aviso_Admin.this.contenido.getText().clear();


                                    Toast.makeText(Aviso_Admin.this,
                                            "Su publicación estará en inicio",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Aviso_Admin.this,
                                            "Algo no salió bien, intente de nuevo más tarde",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }

        }
    }
private void regreso_Inicio(){
    Intent int1 = new Intent(this, Inicio.class);
    startActivity(int1);
    finish();
}
}
