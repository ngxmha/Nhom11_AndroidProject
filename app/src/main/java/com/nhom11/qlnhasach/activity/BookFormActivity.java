package com.nhom11.qlnhasach.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom11.qlnhasach.R;
import com.nhom11.qlnhasach.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookFormActivity extends AppCompatActivity {

    private EditText edMaSach, edTenSach, edTacGia, edGia;
    private Button btnSubmit;
    private ImageButton btnBack;
    private Spinner spinnerNhaSach;

    // Danh sách tạm thời lưu trữ sách - chỉ để thông báo cho fragment có sách mới
    public static List<Book> bookList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_form);

        // Ánh xạ các view từ layout
        edMaSach = findViewById(R.id.edMaSach);
        edTenSach = findViewById(R.id.edTenSach);
        edTacGia = findViewById(R.id.edTacGia);
        edGia = findViewById(R.id.edGia);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);
        spinnerNhaSach = findViewById(R.id.spinnerNhaSach);

        // Chuẩn bị dữ liệu cho spinner nhà sách
        setupNhaSachSpinner();

        // Xử lý nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Xử lý nút thêm sách
        btnSubmit.setOnClickListener(v -> {
            String maSach = edMaSach.getText().toString().trim();
            String tenSach = edTenSach.getText().toString().trim();
            String tacGia = edTacGia.getText().toString().trim();
            String giaStr = edGia.getText().toString().trim();
            String maNhaSach = getSelectedNhaSachId();

            if (maSach.isEmpty() || tenSach.isEmpty() || tacGia.isEmpty() || giaStr.isEmpty() || maNhaSach == null) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            double gia = 0;
            try {
                gia = Double.parseDouble(giaStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá phải là một số hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            Book newBook = new Book(maSach, tenSach, tacGia, gia);

            // Lưu sách vào cơ sở dữ liệu
            long result = MainActivity.databaseManager.addBook(newBook, maNhaSach);

            if (result > 0) {
                // Thêm vào danh sách tạm để thông báo BookFragment
                bookList.add(newBook);
                Toast.makeText(this, "Đã thêm sách: " + newBook.getTenSach(), Toast.LENGTH_SHORT).show();
                finish(); // Quay lại BookFragment
            } else {
                Toast.makeText(this, "Lỗi khi thêm sách, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Thiết lập dữ liệu cho spinner nhà sách
    private void setupNhaSachSpinner() {
        // Lấy danh sách nhà sách từ database
        List<String> nhaSachNames = new ArrayList<>();
        List<String> nhaSachIds = new ArrayList<>();

        // Thêm nhà sách mặc định nếu không có dữ liệu
        if (MainActivity.databaseManager.getAllNhaSach().isEmpty()) {
            nhaSachNames.add("Chọn nhà sách");
            nhaSachIds.add("");
        } else {
            // Lấy danh sách nhà sách từ cơ sở dữ liệu
            for (com.nhom11.qlnhasach.model.NhaSach nhaSach : MainActivity.databaseManager.getAllNhaSach()) {
                nhaSachNames.add(nhaSach.getTenNhaSach());
                nhaSachIds.add(nhaSach.getMaNhaSach());
            }
        }

        // Tạo adapter cho spinner
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nhaSachNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNhaSach.setAdapter(adapter);
    }

    // Lấy mã nhà sách được chọn từ spinner
    private String getSelectedNhaSachId() {
        int position = spinnerNhaSach.getSelectedItemPosition();
        List<com.nhom11.qlnhasach.model.NhaSach> nhaSachList = MainActivity.databaseManager.getAllNhaSach();

        if (position >= 0 && position < nhaSachList.size()) {
            return nhaSachList.get(position).getMaNhaSach();
        }

        return null;
    }
}