package com.nhom11.qlnhasach.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

    // Danh sách tạm thời lưu trữ sách
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
        btnBack = findViewById(R.id.btnBack); // ánh xạ đúng chỗ

        // Xử lý nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Xử lý nút thêm sách
        btnSubmit.setOnClickListener(v -> {
            String maSach = edMaSach.getText().toString().trim();
            String tenSach = edTenSach.getText().toString().trim();
            String tacGia = edTacGia.getText().toString().trim();
            String giaStr = edGia.getText().toString().trim();

            if (maSach.isEmpty() || tenSach.isEmpty() || tacGia.isEmpty() || giaStr.isEmpty()) {
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
            bookList.add(newBook);
            Toast.makeText(this, "Đã thêm sách: " + newBook.getTenSach(), Toast.LENGTH_SHORT).show();

            finish(); // Quay lại BookFragment
        });
    }
}
