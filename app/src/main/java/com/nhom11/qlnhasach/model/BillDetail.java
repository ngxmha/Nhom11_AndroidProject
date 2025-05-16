package com.nhom11.qlnhasach.model;

public class BillDetail {
    private int id;
    private String soHD;
    private String maSach;
    private int soLuong;
    private double donGia;
    private String tenSach; // Thêm tên sách để hiển thị

    public BillDetail(int id, String soHD, String maSach, int soLuong, double donGia) {
        this.id = id;
        this.soHD = soHD;
        this.maSach = maSach;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public BillDetail(int id, String soHD, String maSach, String tenSach, int soLuong, double donGia) {
        this.id = id;
        this.soHD = soHD;
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSoHD() {
        return soHD;
    }

    public void setSoHD(String soHD) {
        this.soHD = soHD;
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public double getThanhTien() {
        return soLuong * donGia;
    }
}