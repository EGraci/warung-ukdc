package com.example.atong;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.BreakIterator;
import java.util.List;

public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.TampilanSet>{
    private List<DtoMenu> menuItems;
    private Context ct;

    public AdapterMenu(List<DtoMenu> menuItems, Context tx) {
        this.menuItems = menuItems;
        this.ct = tx;
    }

    @NonNull
    @Override
    public TampilanSet onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater isi =LayoutInflater.from(ct);
        View lihat= isi.inflate(R.layout.menu_item, parent,  false);
        return new TampilanSet(lihat);
    }

    @Override
    public void onBindViewHolder(TampilanSet holder, int position) {
        DtoMenu menuItem = menuItems.get(position);
        Glide.with(ct).load(menuItem.getGambar()).into(holder.gambar);
        holder.menu.setText(menuItem.getMenu());
        holder.harga.setText(String.valueOf(menuItem.getHarga()));
        holder.rowItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(ct,FormPesan.class);
                pindah.putExtra("menu", menuItem.getMenu());
                pindah.putExtra("gambar", menuItem.getGambar());
                pindah.putExtra("harga", menuItem.getHarga());
                pindah.putExtra("id_menu", Integer.toString(menuItem.getId()));
                ct.startActivity(pindah);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }
    public class TampilanSet extends RecyclerView.ViewHolder{
        public TextView menu;
        public TextView harga;
        public ImageView gambar;
        public  View rowItem;
        public TampilanSet(@NonNull View itemView) {
            super(itemView);
            menu = itemView.findViewById(R.id.nama_menu);
            harga = itemView.findViewById(R.id.harga_menu);
            gambar = itemView.findViewById(R.id.gambar_menu);
            rowItem = itemView;
        }
    }
}
