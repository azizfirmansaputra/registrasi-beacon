package com.basarnas.registrasibeacon.mainmenu.ujifungsi;

public class UjiFungsi {
    private final String ID, IDBeacon, statusUji, callSign, tanggalBeacon;

    public UjiFungsi(String ID, String IDBeacon, String statusUji, String callSign, String tanggalBeacon) {
        this.ID             = ID;
        this.IDBeacon       = IDBeacon;
        this.statusUji      = statusUji;
        this.callSign       = callSign;
        this.tanggalBeacon  = tanggalBeacon;
    }

    public String getID() {
        return ID;
    }

    public String getIDBeacon() {
        return IDBeacon;
    }

    public String getStatusUji() {
        return statusUji;
    }

    public String getCallSign() {
        return callSign;
    }

    public String getTanggalBeacon() {
        return tanggalBeacon;
    }
}