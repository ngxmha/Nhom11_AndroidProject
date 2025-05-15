package com.nhom11.qlnhasach.model;

public class Book {
    private String maSach;
    private String tenSach;
    private String tacGia;
    private double gia;

    public Book(String maSach, String tenSach, String tacGia, double gia) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.gia = gia;
    }

    public String getMaSach() {
        return maSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public String getTacGia() {
        return tacGia;
    }

    public double getGia() {
        return gia;
    }
}
