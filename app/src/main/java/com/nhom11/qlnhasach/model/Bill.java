package com.nhom11.qlnhasach.model;

public class Bill {
    private String soHD;
    private String tenNS;
    private String totalMoney;
    private String ngayHD;

    public Bill(String soHD, String tenNS, String totalMoney, String ngayHD) {
        this.soHD = soHD;
        this.tenNS = tenNS;
        this.totalMoney = totalMoney;
        this.ngayHD = ngayHD;
    }

    public String getSoHD() {
        return soHD;
    }

    public String getTenNS() {
        return tenNS;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public String getNgayHD() {
        return ngayHD;
    }

    public void setSoHD(String soHD) {
        this.soHD = soHD;
    }

    public void setTenNS(String tenNS) {
        this.tenNS = tenNS;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public void setNgayHD(String ngayHD) {
        this.ngayHD = ngayHD;
    }
}
