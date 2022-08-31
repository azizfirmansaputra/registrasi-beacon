package com.basarnas.registrasibeacon.mainmenu.sertifikat;

public class Sertifikat {
    private final String IDBeacon, callSign, tanggalBeacon, tanggalKadaluarsa, keteranganStatus;

    public Sertifikat(String IDBeacon, String callSign, String tanggalBeacon, String tanggalKadaluarsa, String keteranganStatus) {
        this.IDBeacon           = IDBeacon;
        this.callSign           = callSign;
        this.tanggalBeacon      = tanggalBeacon;
        this.tanggalKadaluarsa  = tanggalKadaluarsa;
        this.keteranganStatus   = keteranganStatus;
    }

    public String getIDBeacon() {
        return IDBeacon;
    }

    public String getCallSign() {
        return callSign;
    }

    public String getTanggalBeacon() {
        return tanggalBeacon;
    }

    public String getTanggalKadaluarsa() {
        return tanggalKadaluarsa;
    }

    public String getKeteranganStatus() {
        return keteranganStatus;
    }
}