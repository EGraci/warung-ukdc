package com.example.atong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView home;
    RecyclerView konten;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMenu();
        setContentView(R.layout.activity_main);
    }

    public void getMenu(){

        RequestQueue queue = Volley.newRequestQueue(this);
        List<DtoMenu> menu = new ArrayList<>();
        API api = new API();
        StringRequest request = new StringRequest(Request.Method.GET, api.getMenu(),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                DtoMenu menuItem = new DtoMenu();
                                menuItem.setGambar(jsonObject.getString("gambar"));
                                menuItem.setHarga(jsonObject.getInt("harga"));
                                menuItem.setId(jsonObject.getInt("id"));
                                menuItem.setMenu(jsonObject.getString("menu"));
                                menu.add(menuItem);
                            }
                            AdapterMenu tampilanMenu = new AdapterMenu(menu, MainActivity.this);
                            konten = (RecyclerView) findViewById(R.id.list_menu);
                            konten.setAdapter(tampilanMenu);
                            konten.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        } catch (Exception e) {
                            Log.e("menu", e.toString());
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
            }
        });

        queue.add(request);
    }
}