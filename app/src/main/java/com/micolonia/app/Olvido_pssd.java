package com.micolonia.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Olvido_pssd extends AppCompatActivity {

    private EditText email_confirmacion;
    private Button enviar_correo;
    private ImageView back;
    private ProgressBar cargando;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvido_pssd);
        getSupportActionBar().hide();

        email_confirmacion = findViewById(R.id.editTextf);
        enviar_correo= findViewById(R.id.enviar_correo);
        back= findViewById(R.id.backbtnf);
        cargando= findViewById(R.id.cargando_f);

        auth = FirebaseAuth.getInstance();



        enviar_correo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargando.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(email_confirmacion.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        cargando.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            Toast.makeText(Olvido_pssd.this, "Correo enviado", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(Olvido_pssd.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backiniciarsesion();
            }
        });

    }

    private void backiniciarsesion(){
        Intent iback = new Intent(this, InicioSesion.class);
        startActivity(iback);
        finish();
    }
}
