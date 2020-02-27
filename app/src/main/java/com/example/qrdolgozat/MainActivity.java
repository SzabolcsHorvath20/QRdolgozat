package com.example.qrdolgozat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button btnScan, bntKiir;
    private TextView tvEredmeny;
    private String qrcodecontent = "teszt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("QR Code Scanner by HSzabolcs");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
        bntKiir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    kiiras(qrcodecontent);
                }
                catch (IOException e)
                {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null)
        {
            tvEredmeny.setText("Qr Code eredmény: " + result.getContents());
            qrcodecontent = result.getContents();
        }
        else
        {
            Toast.makeText(this, "Kiléptünk a scannelésből.", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static void kiiras(String content) throws IOException
    {
        String allapot, szovegesAdat;
        File file;
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String formatedDate = dateFormat.format(date);
        szovegesAdat = content + "," + formatedDate + "," + "\r\n";

        allapot = Environment.getExternalStorageState();
        if (allapot.equals(Environment.MEDIA_MOUNTED))
        {
            file = new File(Environment.getExternalStorageDirectory(), "scannedCodes.csv");
            try
            {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true), 1024);
                bufferedWriter.append(szovegesAdat);
                bufferedWriter.close();
            }
            catch(IOException e)
            {

            }
        }
    }

    public void init()
    {
        btnScan = findViewById(R.id.btnScan);
        bntKiir = findViewById(R.id.btnKiír);
        tvEredmeny = findViewById(R.id.tvEredmeny);
    }
}
