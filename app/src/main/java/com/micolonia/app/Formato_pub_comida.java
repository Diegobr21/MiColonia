package com.micolonia.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Formato_pub_comida extends AppCompatActivity {
   private EditText nombre_negcom, horarios, descripcion, contac_negcom;
    private Button inicio;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formato_pub_comida);
        nombre_negcom= findViewById(R.id.nombre_negcom);
        horarios=findViewById(R.id.horarios_negcom);
        descripcion=findViewById(R.id.desc_negcom);
        contac_negcom = findViewById(R.id.contac_negcom);
        inicio=findViewById(R.id.btn_3);

        db = FirebaseFirestore.getInstance();

        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RegresarInicio();
            }
        });
    }
    public void ToastGuardar(View view){
        String nombre_negcom = this.nombre_negcom.getText().toString().trim();
        String horarios_negcom = this.horarios.getText().toString().trim();
        String contac_negcom = this.contac_negcom.getText().toString().trim();
        String desc_negcom = this.descripcion.getText().toString().trim();

        if (nombre_negcom.isEmpty() || horarios_negcom.isEmpty() ||
                contac_negcom.isEmpty() || desc_negcom.isEmpty()) {
            Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> data = new HashMap<>();
            data.put("timestamp", System.currentTimeMillis());
            data.put("name", nombre_negcom);
            data.put("horario", horarios_negcom);
            data.put("telefono", contac_negcom);
            data.put("descripcion", desc_negcom);
            // Este campo es importante, hará que no se muestre en la app hasta su aprobación.
            data.put("visible", 0);

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Cargando");
            dialog.show();

            db.collection("comida")
                    .add(data)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            dialog.dismiss();

                            if (task.isSuccessful()) {
                                Formato_pub_comida.this.nombre_negcom.getText().clear();
                                Formato_pub_comida.this.horarios.getText().clear();
                                Formato_pub_comida.this.contac_negcom.getText().clear();
                                Formato_pub_comida.this.descripcion.getText().clear();

                                Toast.makeText(Formato_pub_comida.this,
                                        "Su publicación fue enviada para ser revisada",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Formato_pub_comida.this,
                                        "Algo no fue bien, intenten de nuevo más tarde",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
    public void RegresarInicio(){
        Intent intent = new Intent(this, Inicio.class);
        startActivity(intent);
    }
}
