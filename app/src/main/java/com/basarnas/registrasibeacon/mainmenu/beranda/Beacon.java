package com.basarnas.registrasibeacon.mainmenu.beranda;

@SuppressWarnings("unused")
public class Beacon {
    private String ID, IDBeacon, status;
    private final String reg, callSign, noSertifikat, jenisData, kategori, berlakuSampai, kadaluarsaBaterai, isVerifikasi, statusUji, formulir, hasil;

    public Beacon(String ID, String reg, String callSign, String noSertifikat, String IDBeacon, String jenisData, String kategori,
                  String berlakuSampai, String kadaluarsaBaterai, String status, String isVerifikasi, String statusUji, String formulir, String hasil) {
        this.ID                 = ID;
        this.reg                = reg;
        this.callSign           = callSign;
        this.noSertifikat       = noSertifikat;
        this.IDBeacon           = IDBeacon;
        this.jenisData          = jenisData;
        this.kategori           = kategori;
        this.berlakuSampai      = berlakuSampai;
        this.kadaluarsaBaterai  = kadaluarsaBaterai;
        this.status             = status;
        this.isVerifikasi       = isVerifikasi;
        this.statusUji          = statusUji;
        this.formulir           = formulir;
        this.hasil              = hasil;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public String getReg() {
        return reg;
    }

    public String getCallSign() {
        return callSign;
    }

    public String getNoSertifikat() {
        return noSertifikat;
    }

    public void setIDBeacon(String IDBeacon) {
        this.IDBeacon = IDBeacon;
    }

    public String getIDBeacon() {
        return IDBeacon;
    }

    public String getJenisData() {
        return jenisData;
    }

    public String getKategori() {
        return kategori;
    }

    public String getBerlakuSampai() {
        return berlakuSampai;
    }

    public String getKadaluarsaBaterai() {
        return kadaluarsaBaterai;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getIsVerifikasi() {
        return isVerifikasi;
    }

    public String getStatusUji() {
        return statusUji;
    }

    public String getFormulir() {
        return formulir;
    }

    public String getHasil() {
        return hasil;
    }
}