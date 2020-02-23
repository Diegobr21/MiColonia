package com.micolonia.app;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class Comida extends AppCompatActivity {
    public static final int REQUEST_CALL = 1;
    private TextView descgeneral;
    private TextView Nom_negocio;
    private TextView horarios;
    private TextView contacto;
    private ImageView imagencom;
    private ImageView img_tel;
    private ImageView wha;
    private ImageView back;
    //  protected ArrayList<String> platillos= new ArrayList<String>();
    //  protected ArrayList<String> descripciones= new ArrayList<String>();
    //  protected ArrayList<Integer> precios= new ArrayList<Integer>();
    private double ratings[] = new double[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comida);
        getSupportActionBar().hide();

        back=findViewById(R.id.backbtncom);
        descgeneral = findViewById(R.id.desc_negcom);
        Nom_negocio = findViewById(R.id.titulo_comida);
        horarios = findViewById(R.id.horarios_negcom);
        imagencom= findViewById(R.id.imagen_negociocom);
        contacto = findViewById(R.id.contacto_negcom);
        img_tel= findViewById(R.id.img_tel);
        wha=findViewById(R.id.wha);
        NegocioCom comida = getIntent().getParcelableExtra("comida");

        Nom_negocio.setText(comida.getName());

        if(comida.getImagen()!=null){
            Glide.with(this).load(comida.getImagen()).circleCrop().into(imagencom);
        }else{
            imagencom.setImageResource(R.drawable.food);
        }

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

        Toast.makeText(this, comida.getName(), Toast.LENGTH_SHORT).show();

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

   private void loadFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_negociocomida, fragment);
        transaction.commit();
    }
}