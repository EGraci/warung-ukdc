package com.example.atong;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class DtoMenu {
    private int id;
    private String menu;
    private int harga;
    private String gambar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getHarga() {
        return intToIDR(harga);
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
    public static String intToIDR(int amount) {
        Locale indonesianLocale = new Locale("in", "ID");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(indonesianLocale);
        return currencyFormatter.format(amount);
    }
}
