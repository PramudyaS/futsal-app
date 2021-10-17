package com.example.lapanganfutsal.model;

import java.io.Serializable;

public class CabangFutsal implements Serializable {

    String namaCabang;
    Character kategoriLapangan;
    Integer jumlahLapangan;

    public CabangFutsal(String namaCabang,Character kategoriLapangan,Integer jumlahLapangan)
    {
        this.namaCabang         = namaCabang;
        this.kategoriLapangan   = kategoriLapangan;
        this.jumlahLapangan     = jumlahLapangan;
    }


    public String getNamaCabang() {
        return namaCabang;
    }

    public void setNamaCabang(String namaCabang) {
        this.namaCabang = namaCabang;
    }

    public Character getKategoriLapangan() {
        return kategoriLapangan;
    }

    public void setKategoriLapangan(Character kategoriLapangan) {
        this.kategoriLapangan = kategoriLapangan;
    }

    public Integer getJumlahLapangan() {
        return jumlahLapangan;
    }

    public void setJumlahLapangan(Integer jumlahLapangan) {
        this.jumlahLapangan = jumlahLapangan;
    }

}
