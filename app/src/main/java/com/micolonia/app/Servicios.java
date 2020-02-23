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

public class Servicios extends AppCompatActivity {
    public static final int REQUEST_CALL = 1;
    private TextView descgeneral;
    private TextView Nom_negocio;
    private TextView horarios;
    private TextView contacto;
    private ImageView imagenser;
    private ImageView img_tel;
    private ImageView wha;
    private ImageView back;
    //  protected ArrayList<String> platillos= new ArrayList<String>();
    //  protected ArrayList<String> descripciones= new ArrayList<String>();
    //  protected ArrayList<Integer> precios= new ArrayList<Integer>();
    private double ratings[]= new double[6];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios);

        getSupportActionBar().hide();

        back=findViewById(R.id.backbtnserv);
        wha= findViewById(R.id.wha);
        img_tel = findViewById(R.id.img_tel);
        descgeneral= findViewById(R.id.desc_negser);
        Nom_negocio=findViewById(R.id.titulo_servicios);
        horarios=findViewById(R.id.horarios_negser);
        contacto=findViewById(R.id.contacto_negser);
        imagenser=findViewById(R.id.imagen_negocioser);

        NegocioSer servicio = getIntent().getParcelableExtra("servicio");

        Nom_negocio.setText(servicio.getName());

        if(servicio.getImagen()!=null){
            Glide.with(this).load(servicio.getImagen()).circleCrop().into(imagenser);
        }else{
            imagenser.setImageResource(R.drawable.wreck);
        }

        horarios.setText("Horario Activo: "+servicio.getHorario());
        descgeneral.setText(servicio.getDescripcion());
        img_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamada();
            }
        });
        String telefono =servicio.getTelefono().trim();
        contacto.setText(": "+telefono );
        contacto.setOnClickListener(new View.OnClickListener() {
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backserv();
            }
        });

       // Toast.makeText(this, servicio.getName(), Toast.LENGTH_SHORT).show();

        // Crea el fragmento
        FragmentoNegSer fragment = new FragmentoNegSer();
        // Crea los argumentos
        Bundle args = new Bundle();
        // Añade, en este caso, el objeto NegocioCom en los argumentos
        args.putParcelable("servicio", servicio);
        // Agrega esos argumentos al fragmento
        fragment.setArguments(args);
        // Finalmente carga el fragmento
        loadFragment(fragment);


    }
    private void backserv(){
        Intent intentb = new Intent(this, seccion_servicios.class);
        startActivity(intentb);
    }
    private void llamada(){
        NegocioSer servicio = getIntent().getParcelableExtra("servicio");
        if(ContextCompat.checkSelfPermission(Servicios.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Servicios.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }else{
            String telefono =servicio.getTelefono().trim();
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

    public void whatsapp(View view) {

        NegocioSer servicio = getIntent().getParcelableExtra("servicio");
        String numero = servicio.getTelefono().trim();
        String text ="Hola!";


        try {


            Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+numero +"&text="+text));
            startActivity(intent);

        } catch (Exception e){
            e.printStackTrace();

        }

    }

    /* public void verplatillos(View view){
             fragmentplatillos= new FragmentoNegCom();
             loadFragment(fragmentplatillos);
         }


         protected double ratingenerator(){

             //comando SQL PARA TRAER LOS ULTIMOS 6 RATINGS DE EL RESTAURANTE CON EL ID
             double rating= (ratings[0]+ratings[1]+ratings[2]+ratings[3]+ratings[4]+ratings[5])/6 ;
             return rating;
         }

      */
    private void loadFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction= manager.beginTransaction();
        transaction.replace(R.id.fragment_neg_servicios, fragment);
        transaction.commit();
    }

}
