package com.example.atong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FormPesan extends AppCompatActivity {
    private TextView menu, harga, total;
    private ImageView gambar_menu;
    private EditText qty;
    private Button bayar;
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
        bayar = (Button) findViewById(R.id.pesan);

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

        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPesan(id_menu, Integer.parseInt(String.valueOf(qty.getText())), idrToInt((String) total.getText()), idrToInt((String) harga.getText()), (String) menu.getText());
            }
        });

    }
    public void sendPesan(int id, int qty, int harga, int total, String menu){
        RequestQueue queue = Volley.newRequestQueue(this);
        API api = new API();
        JSONObject dataPesan = new JSONObject();
        try {
            dataPesan.put("id", id);
            dataPesan.put("qty", qty);
            dataPesan.put("total", total);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest pesan = new JsonObjectRequest(Request.Method.POST, api.getPeasan(), dataPesan,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            int idPesan = response.getInt("id_pesan");
                            String message = response.getString("message");
                            Log.d("Pesan", "ID Pesan: " + idPesan + ", Message: " + message);
                            Intent pindah = new Intent(FormPesan.this, Bayar.class);
                            pindah.putExtra("id", String.valueOf(idPesan));
                            startActivity(pindah);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle the JSON exception
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("pesan error", error.toString());
                        Toast.makeText(getApplicationContext(), "Server Erorr 500", Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(pesan);
    }
    public static int idrToInt(String idrString) {
        String[] parts = idrString.split(",");
        String numericString = parts[0].replaceAll("[^\\d]", "");
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