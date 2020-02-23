package com.micolonia.app;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class InicioSesion extends AppCompatActivity {
    private FirebaseAuth mAuth;
   // private FirebaseInstanceId firebaseInstanceId;
    private ProgressBar cargando;
    private Button ingresar, registro;
    public EditText correo, contraseña;
    private TextView olvide;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
       // firebaseInstanceId= FirebaseInstanceId.getInstance();

        olvide = findViewById(R.id.olvide);
        cargando = findViewById(R.id.cargandoinicio);
        correo = findViewById(R.id.email_log);
        contraseña = findViewById(R.id.contras_log);
        ingresar = (Button) findViewById(R.id.btn_ingresar);
        registro = (Button) findViewById(R.id.btn_aregistrarse);

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mandar a validar usuario y contraseña
                //saber que usuario es
                //  Log.d("Las Hadas", "Bienvenid@");//agregar nombre de usuario en Bienvenido
                autenticar();
            }
        });

        registro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                llevar_a_registro();
            }

        });

        olvide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                olvidepssd();
            }
        });

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando");
        dialog.show();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        iniciodesesion();
                    } else {
                        dialog.dismiss();
                    }
                }
            });
        } else {
            dialog.dismiss();
        }
    }

    //VOID PARA REDIRECCIONAR A Inicio
    public void autenticar() {
        final String usuario = correo.getText().toString().trim();
        final String contras = this.contraseña.getText().toString().trim();
        if (usuario.length() == 0) {
            correo.setError("Ingresa tu correo");
        } else {
            mAuth.signInWithEmailAndPassword(usuario, contras).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information

                        FirebaseUser user = mAuth.getCurrentUser();
                        // user.getDisplayName();
                        iniciodesesion();


                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(InicioSesion.this, "Autenticacion fallida",
                                Toast.LENGTH_SHORT).show();

                    }

                    // ...
                }
            });

        }

    }

    private void llevar_a_registro() {
        Intent intent1 = new Intent(this, MainActivity.class);
        startActivity(intent1);
        finish();
    }

    private void iniciodesesion() {
        Intent intentinicio = new Intent(this, Inicio.class);
        startActivity(intentinicio);
        finish();
    }

    private void olvidepssd() {
        Intent intentolvide = new Intent(this, Olvido_pssd.class);
        startActivity(intentolvide);
        finish();
    }



}
