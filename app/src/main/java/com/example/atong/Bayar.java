package com.example.atong;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        cek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_nota(id_pesan);
            }
        });
    }
    public void get_nota(int id){
        RequestQueue queue = Volley.newRequestQueue(this); // Consider using a singleton pattern for RequestQueue
        API api = new API();
        String link = api.getNota()+"?id="+String.valueOf(id);
        StringRequest request = new StringRequest(Request.Method.GET, link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) { // Check if the array is not empty
                                JSONObject firstItem = jsonArray.getJSONObject(0);

                                int idTagih = firstItem.getInt("id_nota");
                                int hargaMenu = firstItem.getInt("harga");
                                int bayarPesan = firstItem.getInt("bayar");
                                int kembaliNota = firstItem.getInt("kembali");
                                int totalPesan = firstItem.getInt("total");
                                int qtyPesan = firstItem.getInt("qty");
                                String namaMenu = firstItem.getString("menu");

                                Intent pindah = new Intent(Bayar.this, Nota.class);
                                pindah.putExtra("id", String.valueOf(idTagih));
                                pindah.putExtra("harga", String.valueOf(hargaMenu));
                                pindah.putExtra("bayar", String.valueOf(bayarPesan));
                                pindah.putExtra("kembali", String.valueOf(kembaliNota));
                                pindah.putExtra("total", String.valueOf(totalPesan));
                                pindah.putExtra("qty", String.valueOf(qtyPesan));
                                pindah.putExtra("menu", namaMenu);
                                startActivity(pindah);
                            } else {
                                Toast.makeText(getApplicationContext(), "Belum Melakukan Pembayaran", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("nota", e.toString());
                            // Handle the JSON exception
                            Toast.makeText(getApplicationContext(), "Belum Melakukan Pembayaran", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("pesan error", error.toString());
                        Toast.makeText(getApplicationContext(), "Belum Melakukan Pembayaran", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(request);
    }
}