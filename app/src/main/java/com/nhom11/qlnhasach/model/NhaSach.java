package com.nhom11.qlnhasach.model;

import java.io.Serializable;

public class NhaSach implements Serializable {
    private String maNhaSach;
    private String tenNhaSach;
    private String diaChi;
    private String iconUri;

    public NhaSach(String maNhaSach, String tenNhaSach, String diaChi, String iconUri) {
        this.maNhaSach = maNhaSach;
        this.tenNhaSach = tenNhaSach;
        this.diaChi = diaChi;
        this.iconUri = iconUri;
    }

    public String getMaNhaSach() {
        return maNhaSach;
    }

    public void setMaNhaSach(String maNhaSach) {
        this.maNhaSach = maNhaSach;
    }

    public String getTenNhaSach() {
        return tenNhaSach;
    }

    public void setTenNhaSach(String tenNhaSach) {
        this.tenNhaSach = tenNhaSach;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }
}