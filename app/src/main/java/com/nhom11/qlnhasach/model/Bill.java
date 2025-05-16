package com.nhom11.qlnhasach.model;

public class Bill {
    private String soHD;
    private String idNS;
    private double totalMoney;
    private String ngayHD;

    private static int nextInvoiceNumber = 4;

    public Bill(String idNS, double totalMoney, String ngayHD) {
        this.soHD = String.format("HD%03d", nextInvoiceNumber++);
        this.idNS = idNS;
        this.totalMoney = totalMoney;
        this.ngayHD = ngayHD;
    }

    public Bill(String soHD, String idNS, double totalMoney, String ngayHD) {
        this.soHD = soHD;
        this.idNS = idNS;
        this.totalMoney = totalMoney;
        this.ngayHD = ngayHD;
    }

    public String getSoHD() {
        return soHD;
    }

    public void setSoHD(String soHD) {
        this.soHD = soHD;
    }

    public String getIdNS() {
        return idNS;
    }

    public void setIdNS(String idNS) {
        this.idNS = idNS;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getNgayHD() {
        return ngayHD;
    }

    public void setNgayHD(String ngayHD) {
        this.ngayHD = ngayHD;
    }
}