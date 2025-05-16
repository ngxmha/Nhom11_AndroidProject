package com.nhom11.qlnhasach.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nhom11.qlnhasach.activity.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private DBHelper dbHelper;

    public DatabaseManager(Context context) {
        dbHelper = new DBHelper(context);
    }

    // ============== USER OPERATIONS ==============
    public long addUser(String fullName, String email, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("full_name", fullName);
        values.put("email", email);
        values.put("password", password);
        long id = db.insert(DBHelper.TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"id"};
        String selection = "email = ? AND password = ?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(DBHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    // ============== NHÀ SÁCH OPERATIONS ==============
    public long addNhaSach(NhaSach nhaSach) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maNhaSach", nhaSach.getMaNhaSach());
        values.put("tenNhaSach", nhaSach.getTenNhaSach());
        values.put("diaChi", nhaSach.getDiaChi());
        values.put("iconUri", nhaSach.getIconUri());
        long id = db.insert(DBHelper.TABLE_NHA_SACH, null, values);
        db.close();
        return id;
    }

    public boolean updateNhaSach(NhaSach nhaSach) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenNhaSach", nhaSach.getTenNhaSach());
        values.put("diaChi", nhaSach.getDiaChi());
        values.put("iconUri", nhaSach.getIconUri());
        int rows = db.update(DBHelper.TABLE_NHA_SACH, values, "maNhaSach = ?",
                new String[]{nhaSach.getMaNhaSach()});
        db.close();
        return rows > 0;
    }

    public boolean deleteNhaSach(String maNhaSach) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(DBHelper.TABLE_NHA_SACH, "maNhaSach = ?", new String[]{maNhaSach});
        db.close();
        return rows > 0;
    }

    public List<NhaSach> getAllNhaSach() {
        List<NhaSach> nhaSachList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_NHA_SACH, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int maNhaSachIndex = cursor.getColumnIndex("maNhaSach");
                int tenNhaSachIndex = cursor.getColumnIndex("tenNhaSach");
                int diaChiIndex = cursor.getColumnIndex("diaChi");
                int iconUriIndex = cursor.getColumnIndex("iconUri");

                if (maNhaSachIndex != -1 && tenNhaSachIndex != -1 && diaChiIndex != -1 && iconUriIndex != -1) {
                    String maNhaSach = cursor.getString(maNhaSachIndex);
                    String tenNhaSach = cursor.getString(tenNhaSachIndex);
                    String diaChi = cursor.getString(diaChiIndex);
                    String iconUri = cursor.getString(iconUriIndex);

                    NhaSach nhaSach = new NhaSach(maNhaSach, tenNhaSach, diaChi, iconUri);
                    nhaSachList.add(nhaSach);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return nhaSachList;
    }

    public NhaSach getNhaSachById(String maNhaSach) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_NHA_SACH, null, "maNhaSach = ?",
                new String[]{maNhaSach}, null, null, null);

        NhaSach nhaSach = null;
        if (cursor.moveToFirst()) {
            int tenNhaSachIndex = cursor.getColumnIndex("tenNhaSach");
            int diaChiIndex = cursor.getColumnIndex("diaChi");
            int iconUriIndex = cursor.getColumnIndex("iconUri");

            if (tenNhaSachIndex != -1 && diaChiIndex != -1 && iconUriIndex != -1) {
                String tenNhaSach = cursor.getString(tenNhaSachIndex);
                String diaChi = cursor.getString(diaChiIndex);
                String iconUri = cursor.getString(iconUriIndex);
                nhaSach = new NhaSach(maNhaSach, tenNhaSach, diaChi, iconUri);
            }
        }

        cursor.close();
        db.close();
        return nhaSach;
    }

    // ============== BOOK OPERATIONS ==============
    public long addBook(Book book, String maNhaSach) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maSach", book.getMaSach());
        values.put("tenSach", book.getTenSach());
        values.put("tacGia", book.getTacGia());
        values.put("gia", book.getGia());
        values.put("maNhaSach", maNhaSach);
        long id = db.insert(DBHelper.TABLE_BOOK, null, values);
        db.close();
        return id;
    }

    public boolean updateBook(Book book, String maNhaSach) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenSach", book.getTenSach());
        values.put("tacGia", book.getTacGia());
        values.put("gia", book.getGia());
        values.put("maNhaSach", maNhaSach);
        int rows = db.update(DBHelper.TABLE_BOOK, values, "maSach = ?",
                new String[]{book.getMaSach()});
        db.close();
        return rows > 0;
    }

    public boolean deleteBook(String maSach) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(DBHelper.TABLE_BOOK, "maSach = ?", new String[]{maSach});
        db.close();
        return rows > 0;
    }

    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_BOOK, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int maSachIndex = cursor.getColumnIndex("maSach");
                int tenSachIndex = cursor.getColumnIndex("tenSach");
                int tacGiaIndex = cursor.getColumnIndex("tacGia");
                int giaIndex = cursor.getColumnIndex("gia");

                if (maSachIndex != -1 && tenSachIndex != -1 && tacGiaIndex != -1 && giaIndex != -1) {
                    String maSach = cursor.getString(maSachIndex);
                    String tenSach = cursor.getString(tenSachIndex);
                    String tacGia = cursor.getString(tacGiaIndex);
                    double gia = cursor.getDouble(giaIndex);

                    Book book = new Book(maSach, tenSach, tacGia, gia);
                    bookList.add(book);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return bookList;
    }

    public List<Book> getBooksByNhaSach(String maNhaSach) {
        List<Book> bookList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_BOOK, null, "maNhaSach = ?",
                new String[]{maNhaSach}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int maSachIndex = cursor.getColumnIndex("maSach");
                int tenSachIndex = cursor.getColumnIndex("tenSach");
                int tacGiaIndex = cursor.getColumnIndex("tacGia");
                int giaIndex = cursor.getColumnIndex("gia");

                if (maSachIndex != -1 && tenSachIndex != -1 && tacGiaIndex != -1 && giaIndex != -1) {
                    String maSach = cursor.getString(maSachIndex);
                    String tenSach = cursor.getString(tenSachIndex);
                    String tacGia = cursor.getString(tacGiaIndex);
                    double gia = cursor.getDouble(giaIndex);

                    Book book = new Book(maSach, tenSach, tacGia, gia);
                    bookList.add(book);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return bookList;
    }

    // ============== BILL OPERATIONS ==============
    public long addBill(Bill bill) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("soHD", bill.getSoHD());
        values.put("maNhaSach", bill.getTenNS());
        values.put("ngayHD", bill.getNgayHD());
        values.put("totalMoney", bill.getTotalMoney());
        long id = db.insert(DBHelper.TABLE_BILL, null, values);
        db.close();
        return id;
    }

    public List<Bill> getAllBills() {
        List<Bill> billList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_BILL, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int soHDIndex = cursor.getColumnIndex("soHD");
                int maNhaSachIndex = cursor.getColumnIndex("maNhaSach");
                int ngayHDIndex = cursor.getColumnIndex("ngayHD");
                int totalMoneyIndex = cursor.getColumnIndex("totalMoney");

                if (soHDIndex != -1 && maNhaSachIndex != -1 && ngayHDIndex != -1 && totalMoneyIndex != -1) {
                    String soHD = cursor.getString(soHDIndex);
                    String maNhaSach = cursor.getString(maNhaSachIndex);
                    String ngayHD = cursor.getString(ngayHDIndex);
                    String totalMoney = cursor.getString(totalMoneyIndex);

                    // Lấy tên nhà sách từ mã nhà sách
                    NhaSach nhaSach = getNhaSachById(maNhaSach);
                    String tenNS = nhaSach != null ? nhaSach.getTenNhaSach() : "";

                    Bill bill = new Bill(soHD, tenNS, totalMoney, ngayHD);
                    billList.add(bill);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return billList;
    }

    // ============== BILL DETAIL OPERATIONS ==============
    public long addBillDetail(String soHD, String maSach, int soLuong, double donGia) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("soHD", soHD);
        values.put("maSach", maSach);
        values.put("soLuong", soLuong);
        values.put("donGia", donGia);
        long id = db.insert(DBHelper.TABLE_BILL_DETAIL, null, values);
        db.close();
        return id;
    }

    // Phương thức này trả về chi tiết của một hóa đơn
    public Cursor getBillDetails(String billId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "soHD = ?";
        String[] selectionArgs = {billId};
        return db.query(DBHelper.TABLE_BILL_DETAIL, null, selection, selectionArgs, null, null, null);
    }

    // Lấy danh sách chi tiết hóa đơn theo mã hóa đơn
    public List<BillDetail> getBillDetailsList(String billId) {
        List<BillDetail> billDetails = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT bd.*, b.tenSach FROM " + DBHelper.TABLE_BILL_DETAIL + " bd " +
                "INNER JOIN " + DBHelper.TABLE_BOOK + " b ON bd.maSach = b.maSach " +
                "WHERE bd.soHD = ?";
        Cursor cursor = db.rawQuery(query, new String[]{billId});

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int soHDIndex = cursor.getColumnIndex("soHD");
                int maSachIndex = cursor.getColumnIndex("maSach");
                int soLuongIndex = cursor.getColumnIndex("soLuong");
                int donGiaIndex = cursor.getColumnIndex("donGia");
                int tenSachIndex = cursor.getColumnIndex("tenSach");

                if (idIndex != -1 && soHDIndex != -1 && maSachIndex != -1 &&
                        soLuongIndex != -1 && donGiaIndex != -1 && tenSachIndex != -1) {

                    int id = cursor.getInt(idIndex);
                    String soHD = cursor.getString(soHDIndex);
                    String maSach = cursor.getString(maSachIndex);
                    int soLuong = cursor.getInt(soLuongIndex);
                    double donGia = cursor.getDouble(donGiaIndex);
                    String tenSach = cursor.getString(tenSachIndex);

                    BillDetail detail = new BillDetail(id, soHD, maSach, tenSach, soLuong, donGia);
                    billDetails.add(detail);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return billDetails;
    }

    // Đóng kết nối cơ sở dữ liệu
    public void closeDB() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}