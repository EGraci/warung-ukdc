package com.example.atong;

public class DtoPesan {
    private  int id_menu;
    private int qty;
    private int total;

    public int getId_menu() {
        return id_menu;
    }

    public void setId_menu(int id_menu) {
        this.id_menu = id_menu;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = idrToInt(qty);
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = idrToInt(total);
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
}
