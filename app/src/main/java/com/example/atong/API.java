package com.example.atong;

public class API {
    private String menu;
    private String peasan;
    private String nota;

    public API() {
            this.menu = "http://android.dcinfor.org/menu";
            this.peasan = "http://android.dcinfor.org/pesan";
            this.nota = "http://android.dcinfor.org/nota";
    }

    public String getMenu() {
        return menu;
    }

    public String getPeasan() {
        return peasan;
    }

    public String getNota() {
        return nota;
    }
}
