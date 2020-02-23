package com.micolonia.app;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public EditText nombre, email,
            contras, re_contras;
    public Spinner colonias_spinner;
    public int id_colonia;



    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public Button btn_registrarse, btn_reg_inicio;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportActionBar().hide();

        //  Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");


        nombre = findViewById(R.id.nombre);
        email = findViewById(R.id.email);
        colonias_spinner = (Spinner) findViewById(R.id.colonias_spinner);
        colonias_spinner.setOnItemSelectedListener(new CustomOnItemSelectedSpinner());

        contras = findViewById(R.id.contras);
        re_contras = findViewById(R.id.re_contras);
        btn_registrarse = findViewById(R.id.btn_registrarse);
        btn_reg_inicio = findViewById(R.id.btn_reg_inicio);

        btn_reg_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Regresar_inicio();
            }
        });

        btn_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registrar();
                // iniciarsesion();

            }
        });

        if (re_contras.getText() != contras.getText()) {
            re_contras.setError("La contraseña no coincide");
        }


        String colonia = String.valueOf(colonias_spinner.getSelectedItem());

        if (colonia == "Las Hadas"){
            id_colonia=1;
        }
        else if(colonia == "Mision Anahuac"){
            id_colonia=2;
        }

    }

    //  Hay que validar----------------------------------------------------------
    private void Registrar() {


        final String nombre = this.nombre.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
        final String contras = this.contras.getText().toString().trim();
        final String re_contras = this.contras.getText().toString().trim();
        final int id_colonia = this.id_colonia;
        final int tipo=1;

//TODO: final id_colonia gets the correct input from the int



        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Ingrese un correo", Toast.LENGTH_SHORT).show();
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Ingrese un correo válido", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(this, "Ingrese un nombre de ususario", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(contras)) {
            Toast.makeText(this, "Ingrese una contraseña valida", Toast.LENGTH_SHORT).show();
            return;
        }
        if(contras.length()<6){
            Toast.makeText(this, "Contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(re_contras)) {
            Toast.makeText(this, "Ingrese una contraseña valida", Toast.LENGTH_SHORT).show();
            return;
        }



        mAuth.createUserWithEmailAndPassword(email, contras)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // TODO: Inicio de sesión
                            // Una vez se crea la cuenta, se procede a crear sus datos en la db
                            // Usamos la colección usuarios, y su documento será el uid del usuario,
                            // que es su id unico en firebase.
                            // si no se pueden crear los datos en la db, se debería eliminar la cuenta
                            // creada

                            // Sign in success, update UI with the signed-in user's information
                            //btn_registrarse.setVisibility(View.GONE);

                            HashMap<String, Object> datos = new HashMap<>();
                            datos.put("nombre", nombre);
                            datos.put("email", email);
                            datos.put("colonia", id_colonia);
                            datos.put("tipo", tipo);



                            FirebaseUser user = mAuth.getCurrentUser();
                            createUserData(user, datos);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Verifique sus datos",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                });

    }
    //validacion numerica y de correo

    private void createUserData(final FirebaseUser user, HashMap<String, Object> datos) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando");
        progressDialog.show();

        db.collection("usuarios")
                .document(user.getUid())
                .set(datos)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Se ha registrado exitosamente",
                                    Toast.LENGTH_SHORT).show();

                            //   cargando.setVisibility(View.GONE);

                            iniciarsesion();
                        } else {
                            progressDialog.dismiss();
                            user.delete();

                            Toast.makeText(MainActivity.this, "Fallo el registro, verifique sus datos",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }


    //VOID PARA REDIRECCIONAR A InicioSesion
    public void iniciarsesion() {
        Intent int1 = new Intent(this, InicioSesion.class);

        startActivity(int1);
    }

    public void Regresar_inicio() {
        Intent int2 = new Intent(this, InicioSesion.class);

        startActivity(int2);
    }


}
