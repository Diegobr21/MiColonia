package com.micolonia.app;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GeneradorQR extends AppCompatActivity {
    private Button QRgenerator;
    private ImageView codigo;
    private TextView fecha;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userID;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generador_qr);
        QRgenerator = findViewById(R.id.btn_generar_Qr);
        codigo = findViewById(R.id.img_QRcode);
        calendar = Calendar.getInstance();

        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        fecha = findViewById(R.id.txt_fecha_qr);
        //Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

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

    private void generate(final String uid) throws WriterException {

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = multiFormatWriter.encode(uid + " " + date, BarcodeFormat.QR_CODE, 350, 300, null);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
        codigo.setImageBitmap(bitmap);

        fecha.setText("Validez: " + date);
    }
}
