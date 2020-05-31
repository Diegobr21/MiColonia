package com.micolonia.app;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class GeneradorQR extends AppCompatActivity {
    private Button QRgenerator, compartir;
    private ImageView codigo;
    private TextView fecha;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userID;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private AlertDialog dialog;
    private Uri urig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generador_qr);
        QRgenerator = findViewById(R.id.btn_generar_Qr);
        compartir = findViewById(R.id.btn_compartirqr);
        //guardar = findViewById(R.id.btn_guardarqr);
        codigo = findViewById(R.id.img_QRcode);
        calendar = Calendar.getInstance();

        compartir.setVisibility(View.GONE);
        //guardar.setVisibility(View.GONE);

        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        fecha = findViewById(R.id.txt_fecha_qr);
        //Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();


        compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                compartirimg();
            }
        });

        /*
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoalerta();
            }
        });


         */
        QRgenerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    generate(userID);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    //Generador de QR
    private void generate(final String uid) throws WriterException {

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = multiFormatWriter.encode(uid + " " + date, BarcodeFormat.QR_CODE, 350, 300, null);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
        codigo.setImageBitmap(bitmap);

        fecha.setText("Validez: " + date);

        compartir.setVisibility(View.VISIBLE);
        //guardar.setVisibility(View.VISIBLE);

        urig = saveImage(bitmap);
    }
/*
    private void guardarimg(){
        FileOutputStream fileOutputStream = null;
        File file = getDisc();

        if(!file.exists() && !file.mkdirs()){
            Toast.makeText(this, "No se pudo crear carpeta para guardar la imagen", Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
        String date = simpleDateFormat.format(new Date());
        String name = "img" + date + ".jpg";
        String file_name = file.getAbsolutePath() + "/" + name;
        File new_file = new File(file_name);
        try {
            fileOutputStream = new FileOutputStream(new_file);
            Bitmap bitmap = viewToBitmap(codigo,codigo.getWidth(), codigo.getHeight());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            Toast.makeText(this, "Imagen guardada", Toast.LENGTH_SHORT).show();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        refreshGallery(new_file);
    }

    public void refreshGallery(File file){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }
    private File getDisc(){
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "Imagen QR");
    }

 */
    private void compartirimg() {

       // Resources resources = this.getResources();

        /*Uri uri = Uri.parse("android.resource://"+ GeneradorQR.this.getPackageName() +"/drawable/casa");
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getResources().getResourcePackageName(R.drawable.casa)
                + '/' + getResources().getResourceTypeName(R.drawable.casa)
                + '/' + getResources().getResourceEntryName(R.drawable.casa) );
        try {
            InputStream stream = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
         */
        Uri uri = null;
        if(urig != null){
            uri = urig;
        }else{
            Toast.makeText(this, "Error al compartir", Toast.LENGTH_LONG).show();
        }
        
        Intent shareint = new Intent(android.content.Intent.ACTION_SEND);
        shareint.putExtra(Intent.EXTRA_STREAM, uri);
        shareint.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareint.setType("image/png");
        startActivity(Intent.createChooser(shareint, "Compartir QR"));


    }

    private Uri saveImage(Bitmap image) {
        //TODO - Should be processed in another thread
        File imagesFolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imagesFolder.mkdirs();
            File file = new File(imagesFolder, "shared_image.png");

            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(this, "com.mydomain.fileprovider", file);

        } catch (IOException e) {
            Toast.makeText(this, "Error para compartir: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return uri;
    }

/*
    public static Bitmap viewToBitmap(View view, int width, int height){
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void dialogoalerta() {
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Guardar Imagen");
        builder.setMessage("¿Quieres guardar la imagen?");
        //listeners de los botones
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                guardarimg();
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

 */
}
