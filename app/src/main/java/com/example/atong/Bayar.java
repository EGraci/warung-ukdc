package com.example.atong;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Bayar extends AppCompatActivity {
    private TextView no_antrian;
    private int id_pesan;
    private Button cek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayar);
        no_antrian = (TextView) findViewById(R.id.id_pesan);
        cek = (Button) findViewById(R.id.nota);
        Intent data = getIntent();
        if(data.hasExtra("id")){
            Log.d("bayar", data.getStringExtra("id"));
            id_pesan = Integer.parseInt(data.getStringExtra("id"));
            no_antrian.setText(no_antrian.getText()+data.getStringExtra("id"));
        }
    }
}