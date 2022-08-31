package com.basarnas.registrasibeacon.mainmenu.armada;

public class Armada {
    private String ID, tipe;
    private final String namaPenggunaan, spesifik;

    public Armada(String ID, String namaPenggunaan, String spesifik, String tipe) {
        this.ID             = ID;
        this.namaPenggunaan = namaPenggunaan;
        this.spesifik       = spesifik;
        this.tipe           = tipe;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public String getNamaPenggunaan() {
        return namaPenggunaan;
    }

    public String getSpesifik() {
        return spesifik;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getTipe() {
        return tipe;
    }
}