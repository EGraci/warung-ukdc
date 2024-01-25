package com.example.atong;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.Inflater;

public class Nota extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_CODE = 23;
    private TextView idNota, namaPesan, jumlah, totalBayar, kembali;
    private Button genrateNota, pesanLagi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);

        idNota = (TextView) findViewById(R.id.idNota);
        namaPesan = (TextView) findViewById(R.id.namaPesanan);
        jumlah = (TextView) findViewById(R.id.jumlah);
        totalBayar = (TextView) findViewById(R.id.totalBayar);
        kembali = (TextView) findViewById(R.id.kembali);
        genrateNota = (Button) findViewById(R.id.downloadNotaButton);
        pesanLagi = (Button) findViewById(R.id.pesanLagiButton);

        Intent data = getIntent();

        Log.d("tot", data.getStringExtra("id")+data.getStringExtra("menu")+data.getStringExtra("qty")+data.getStringExtra("total")+data.getStringExtra("kembali"));
        idNota.setText(idNota.getText() + data.getStringExtra("id"));
        namaPesan.setText(namaPesan.getText() + data.getStringExtra("menu"));
        jumlah.setText(jumlah.getText() + data.getStringExtra("qty"));
        totalBayar.setText(totalBayar.getText() + data.getStringExtra("total"));
        kembali.setText(kembali.getText() + data.getStringExtra("kembali"));

        genrateNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkStoragePermissions()){
                    View content = findViewById(R.layout.activity_nota);
                    content.requestLayout();
                    content.invalidate();
                    content.post(new Runnable() {
                        @Override
                        public void run() {
                            LayoutInflater inflater = (LayoutInflater) Nota.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View baru = inflater.inflate(R.layout.activity_nota, null);
                            generatePDF(baru, Nota.this);
//                            createPdfWithUpdatedText(Nota.this, R.layout.activity_nota, "NotaWarungUKDC.pdf", 0.5f);
                        }
                    });
                }else{
                    requestForStoragePermissions();
                }
            }
        });
        pesanLagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pindah = new Intent(Nota.this, MainActivity.class);
                startActivity(pindah);
            }
        });

    }
    public void generatePDF(View view, Context context) {
        // Create a Bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        // Set up a PdfDocument
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        // Draw the Bitmap onto the PDF
        page.getCanvas().drawBitmap(bitmap, 0, 0, null);
        pdfDocument.finishPage(page);

        // Write the PDF to a file
        File pdfFile = new File(Environment.getExternalStorageDirectory(), "nota.pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(pdfFile));
            Toast.makeText(context, "PDF saved at: " + pdfFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Close the document and recycle the bitmap
        pdfDocument.close();
        bitmap.recycle();
    }
//    public void createPdfWithUpdatedText(Context context, int layoutResId, String pdfFileName, float scaleFactor) {
//
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View content = inflater.inflate(layoutResId, null);
//
//        content.setScaleX(scaleFactor);
//        content.setScaleY(scaleFactor);
//
//        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(595, View.MeasureSpec.EXACTLY);
//        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(842, View.MeasureSpec.EXACTLY);
//        content.measure(widthMeasureSpec, heightMeasureSpec);
//        content.layout(0, 0, content.getMeasuredWidth(), content.getMeasuredHeight());
//
//        Bitmap bitmap = Bitmap.createBitmap(content.getWidth(), content.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        content.draw(canvas);
//
//        PdfDocument pdfDocument = new PdfDocument();
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(content.getMeasuredWidth(), content.getMeasuredHeight(), 1).create();
//        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
//
////        Canvas canvas = page.getCanvas();
////        content.draw(canvas);
//
//        pdfDocument.finishPage(page);
//
//        File pdfFile = new File(Environment.getExternalStorageDirectory(), pdfFileName);
//        try {
//            pdfDocument.writeTo(new FileOutputStream(pdfFile));
//            Toast.makeText(context, "PDF saved at: " + pdfFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(context, "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//        pdfDocument.close();
//    }

    public boolean checkStoragePermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        }
        return true;
    }
    private void requestForStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                storageActivityResultLauncher.launch(intent);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                storageActivityResultLauncher.launch(intent);
            }
        }
    }
    private ActivityResultLauncher<Intent> storageActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>(){

                        @Override
                        public void onActivityResult(ActivityResult o) {
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                                //Android is 11 (R) or above
                                if(Environment.isExternalStorageManager()){
                                    //Manage External Storage Permissions Granted
                                    Log.d("Permisi", "onActivityResult: Manage External Storage Permissions Granted");
                                }else{
                                    Toast.makeText(Nota.this, "Storage Permissions Denied", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

}