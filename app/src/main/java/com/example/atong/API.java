package com.example.atong;

public class API {
    private String menu;
    private String peasan;
    private String nota;

    public API() {
            this.menu = "https://android.dcinfor.org/menu";
            this.peasan = "https://android.dcinfor.org/pesan";
            this.nota = "https://android.dcinfor.org/nota";
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
