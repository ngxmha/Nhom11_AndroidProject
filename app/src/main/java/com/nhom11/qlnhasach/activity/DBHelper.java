package com.nhom11.qlnhasach.activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nhom11.qlnhasach.model.Bill;
import com.nhom11.qlnhasach.model.Book;
import com.nhom11.qlnhasach.model.NhaSach;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BookShopManagementSystem.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "Users";
    public static final String TABLE_BOOKSTORES = "Bookstores";
    public static final String TABLE_BOOKS = "Books";
    public static final String TABLE_INVOICES = "Invoices";
    public static final String TABLE_INVOICE_BOOKS = "InvoiceBooks";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Bảng người dùng
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "full_name TEXT, " +
                "email TEXT UNIQUE, " +
                "password TEXT)";
        db.execSQL(CREATE_USER_TABLE);

        // Bảng Bookstores
        String CREATE_BOOKSTORE_TABLE = "CREATE TABLE " + TABLE_BOOKSTORES + " (" +
                "maNhaSach TEXT PRIMARY KEY, " +
                "tenNhaSach TEXT, " +
                "diaChi TEXT, " +
                "iconUri TEXT)";
        db.execSQL(CREATE_BOOKSTORE_TABLE);

        // Bảng Books
        String CREATE_BOOK_TABLE = "CREATE TABLE " + TABLE_BOOKS + " (" +
                "maSach TEXT PRIMARY KEY, " +
                "tenSach TEXT, " +
                "tacGia TEXT, " +
                "gia REAL)";
        db.execSQL(CREATE_BOOK_TABLE);

        // Bảng Invoices
        String CREATE_INVOICE_TABLE = "CREATE TABLE " + TABLE_INVOICES + " (" +
                "soHD TEXT PRIMARY KEY, " +
                "idNS TEXT, " +
                "totalMoney REAL, " +
                "ngayHD TEXT, " +
                "FOREIGN KEY(idNS) REFERENCES " + TABLE_BOOKSTORES + "(maNhaSach))";
        db.execSQL(CREATE_INVOICE_TABLE);

        // Bảng trung gian InvoiceBooks
//        String CREATE_INVOICE_BOOKS_TABLE = "CREATE TABLE " + TABLE_INVOICE_BOOKS + " (" +
//                "invoice_id_fk INTEGER, " +
//                "book_id_fk INTEGER, " +
//                "title TEXT, " +
//                "quantity INTEGER, " +
//                "price REAL, " +
//                "PRIMARY KEY(invoice_id_fk, book_id_fk), " +
//                "FOREIGN KEY(invoice_id_fk) REFERENCES " + TABLE_INVOICES + "(invoice_id), " +
//                "FOREIGN KEY(book_id_fk) REFERENCES " + TABLE_BOOKS + "(book_id))";
//        db.execSQL(CREATE_INVOICE_BOOKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa các bảng cũ nếu tồn tại
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICE_BOOKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKSTORES);

        // Tạo lại các bảng
        onCreate(db);
    }

    public void createSampleData() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Kiểm tra xem đã có dữ liệu trong bảng NhaSach chưa
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_BOOKSTORES, null);
        cursor.moveToFirst();
        int nhaSachCount = cursor.getInt(0);
        cursor.close();

        // Nếu chưa có dữ liệu nhà sách
        if (nhaSachCount == 0) {
            // Thêm nhà sách mẫu
            values.clear();
            values.put("maNhaSach", "NS001");
            values.put("tenNhaSach", "Nhà Sách Phương Nam");
            values.put("diaChi", "123 Nguyễn Du, Hà Nội");
            values.put("iconUri", "");
            db.insert(TABLE_BOOKSTORES, null, values);

            values.clear();
            values.put("maNhaSach", "NS002");
            values.put("tenNhaSach", "Nhà Sách Cá Chép");
            values.put("diaChi", "456 Trần Hưng Đạo, Hà Nội");
            values.put("iconUri", "");
            db.insert(TABLE_BOOKSTORES, null, values);

            values.clear();
            values.put("maNhaSach", "NS003");
            values.put("tenNhaSach", "Nhà Sách Fahasa");
            values.put("diaChi", "789 Lê Lợi, TP.HCM");
            values.put("iconUri", "");
            db.insert(TABLE_BOOKSTORES, null, values);
        }

        // Kiểm tra xem đã có dữ liệu trong bảng Book chưa
        cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_BOOKS, null);
        cursor.moveToFirst();
        int bookCount = cursor.getInt(0);
        cursor.close();

        // Nếu chưa có dữ liệu sách
        if (bookCount == 0) {
            // Thêm sách mẫu
            values.clear();
            values.put("maSach", "B001");
            values.put("tenSach", "Lập trình Android");
            values.put("tacGia", "Nguyễn Văn A");
            values.put("gia", 85000);
            db.insert(TABLE_BOOKS, null, values);

            values.clear();
            values.put("maSach", "B002");
            values.put("tenSach", "Cấu trúc dữ liệu");
            values.put("tacGia", "Trần Thị B");
            values.put("gia", 95000);
            db.insert(TABLE_BOOKS, null, values);

            values.clear();
            values.put("maSach", "B003");
            values.put("tenSach", "Lập trình Python");
            values.put("tacGia", "Nguyễn Văn C");
            values.put("gia", 95000);
            db.insert(TABLE_BOOKS, null, values);

            values.clear();
            values.put("maSach", "B004");
            values.put("tenSach", "Cấu trúc dữ liệu nâng cao");
            values.put("tacGia", "Trần Thị D");
            values.put("gia", 100000);
            db.insert(TABLE_BOOKS, null, values);

            values.clear();
            values.put("maSach", "B005");
            values.put("tenSach", "Lập trình Java");
            values.put("tacGia", "Lê Thị E");
            values.put("gia", 80000);
            db.insert(TABLE_BOOKS, null, values);
        }

         //Kiểm tra xem đã có dữ liệu trong bảng Bill chưa
        cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_INVOICES, null);
        cursor.moveToFirst();
        int billCount = cursor.getInt(0);
        cursor.close();

        // Nếu chưa có dữ liệu hóa đơn
        if (billCount == 0) {
            // Thêm hóa đơn mẫu
            values.clear();
            values.put("soHD", "HD001");
            values.put("idNS", "NS001");
            values.put("totalMoney", 123000);
            values.put("ngayHD", "01/01/2025 - 12:00");
            db.insert(TABLE_INVOICES, null, values);

            values.clear();
            values.put("soHD", "HD002");
            values.put("idNS", "NS002");
            values.put("totalMoney", 150000);
            values.put("ngayHD", "02/01/2025 - 15:30");
            db.insert(TABLE_INVOICES, null, values);

            values.clear();
            values.put("soHD", "HD003");
            values.put("idNS", "NS003");
            values.put("totalMoney", 56000);
            values.put("ngayHD", "03/01/2025 - 09:15");
            db.insert(TABLE_INVOICES, null, values);
        }
            // Thêm chi tiết hóa đơn mẫu
//            values.clear();
//            values.put("soHD", "HD001");
//            values.put("maSach", "B001");
//            values.put("soLuong", 1);
//            values.put("donGia", 85000);
//            db.insert(TABLE_BILL_DETAIL, null, values);
//
//            values.clear();
//            values.put("soHD", "HD001");
//            values.put("maSach", "B002");
//            values.put("soLuong", 2);
//            values.put("donGia", 95000);
//            db.insert(TABLE_BILL_DETAIL, null, values);
//
//            values.clear();
//            values.put("soHD", "HD002");
//            values.put("maSach", "B003");
//            values.put("soLuong", 3);
//            values.put("donGia", 95000);
//            db.insert(TABLE_BILL_DETAIL, null, values);
//
//            values.clear();
//            values.put("soHD", "HD003");
//            values.put("maSach", "B004");
//            values.put("soLuong", 1);
//            values.put("donGia", 100000);
//            db.insert(TABLE_BILL_DETAIL, null, values);
//        }
    }
    //================== NHÀ SÁCH ==================
    public List<NhaSach> getAllBookstores() {
        List<NhaSach> nhaSachList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKSTORES, null, null, null, null, null, null);

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

    public long addBookStore(NhaSach nhaSach) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maNhaSach", nhaSach.getMaNhaSach());
        values.put("tenNhaSach", nhaSach.getTenNhaSach());
        values.put("diaChi", nhaSach.getDiaChi());
        values.put("iconUri", nhaSach.getIconUri());
        long id = db.insert(DBHelper.TABLE_BOOKSTORES, null, values);
        db.close();
        return id;
    }

    public boolean updateBookStore(NhaSach nhaSach) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenNhaSach", nhaSach.getTenNhaSach());
        values.put("diaChi", nhaSach.getDiaChi());
        values.put("iconUri", nhaSach.getIconUri());
        int rows = db.update(TABLE_BOOKSTORES, values, "maNhaSach = ?",
                new String[]{nhaSach.getMaNhaSach()});
        db.close();
        return rows > 0;
    }

    public boolean deleteBookStore(String maNhaSach) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE_BOOKSTORES, "maNhaSach = ?", new String[]{maNhaSach});
        db.close();
        return rows > 0;
    }

    //====================== Sách =====================
    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_BOOKS, null, null, null, null, null, null);

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

    public long addBook(Book book) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maSach", book.getMaSach());
        values.put("tenSach", book.getTenSach());
        values.put("tacGia", book.getTacGia());
        values.put("gia", book.getGia());
        long id = db.insert(TABLE_BOOKS, null, values);
        db.close();
        return id;
    }

    //=================== Hóa đơn ===================
    public List<Bill> getAllBills() {
        List<Bill> billList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_INVOICES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int soHDIndex = cursor.getColumnIndex("soHD");
                int maNhaSachIndex = cursor.getColumnIndex("idNS");
                int totalMoneyIndex = cursor.getColumnIndex("totalMoney");
                int ngayHDIndex = cursor.getColumnIndex("ngayHD");

                if (soHDIndex != -1 && maNhaSachIndex != -1 && ngayHDIndex != -1 && totalMoneyIndex != -1) {
                    String soHD = cursor.getString(soHDIndex);
                    String maNhaSach = cursor.getString(maNhaSachIndex);
                    double totalMoney = cursor.getDouble(totalMoneyIndex);
                    String ngayHD = cursor.getString(ngayHDIndex);

                    Bill bill = new Bill(soHD, maNhaSach, totalMoney, ngayHD);
                    billList.add(bill);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return billList;
    }

    public long addBill(Bill bill) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("soHD", bill.getSoHD());
        values.put("idNS", bill.getIdNS());
        values.put("totalMoney", bill.getTotalMoney());
        values.put("ngayHD", bill.getNgayHD());
        long id = db.insert(TABLE_INVOICES, null, values);
        db.close();
        return id;
    }
}
