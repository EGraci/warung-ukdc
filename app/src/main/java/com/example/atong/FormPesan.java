package com.example.atong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.Locale;

public class FormPesan extends AppCompatActivity {
    private TextView menu, harga, total;
    private ImageView gambar_menu;
    private EditText qty;
    private  int id_menu, tagihan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pesan);
        Intent data = getIntent();
        menu = (TextView) findViewById(R.id.nama_menu);
        harga = (TextView) findViewById(R.id.harga_menu);
        gambar_menu = (ImageView) findViewById(R.id.gambar_menu);
        total = (TextView) findViewById(R.id.total);
        qty = (EditText) findViewById(R.id.jumlah_menu);

        if(data.hasExtra("menu") && data.hasExtra("harga") && data.hasExtra("gambar")){
            Log.d("Pesan", data.getStringExtra("id_menu"));
            id_menu = Integer.parseInt(data.getStringExtra("id_menu"));
            menu.setText(data.getStringExtra("menu").toString());
            harga.setText(data.getStringExtra("harga"));
            Glide.with(this).load(data.getStringExtra("gambar")).into(gambar_menu);
        }
        qty.setText("0");
        qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    qty.removeTextChangedListener(this);
                    qty.setText("0");
                    qty.setSelection(qty.getText().length());
                    qty.addTextChangedListener(this);
                }
                tagihan = idrToInt((String) harga.getText()) * Integer.parseInt(String.valueOf(qty.getText()));
                total.setText(intToIDR(tagihan));
            }
        });
    }
    public static int idrToInt(String idrString) {
        String[] parts = idrString.split(",");
        String numericString = parts[0].replaceAll("[^\\d]", ""); // Remove non-numeric characters
        try {
            return Integer.parseInt(numericString);
        } catch (NumberFormatException e) {
            System.err.println("Invalid format for integer conversion: " + e.getMessage());
            return -1;
        }
    }
    public static String intToIDR(int amount) {
        Locale indonesianLocale = new Locale("in", "ID");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(indonesianLocale);
        return currencyFormatter.format(amount);
    }
}