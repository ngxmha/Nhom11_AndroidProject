package com.nhom11.qlnhasach.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom11.qlnhasach.R;
import com.nhom11.qlnhasach.model.NhaSach;

public class ThemNhaSachActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private EditText edtMa;
    private EditText edtTen;
    private EditText edtDiaChi;
    private ImageView imgIcon;
    private Button btnThem;
    private Button btnHuy;

    private Uri selectedImageUri = null;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nha_sach);

        btnBack = findViewById(R.id.btnBack);
        edtMa = findViewById(R.id.edtMa);
        edtTen = findViewById(R.id.edtTen);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        imgIcon = findViewById(R.id.imgIcon);
        btnThem = findViewById(R.id.btnThem);
        btnHuy = findViewById(R.id.btnHuy);

        // Xử lý nút quay lại
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Xử lý chọn ảnh từ thư viện
        imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
            }
        });

        // Xử lý nút thêm nhà sách
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maNhaSach = edtMa.getText().toString().trim();
                String tenNhaSach = edtTen.getText().toString().trim();
                String diaChi = edtDiaChi.getText().toString().trim();
                String iconUriString = selectedImageUri != null ? selectedImageUri.toString() : null;

                if (!maNhaSach.isEmpty() && !tenNhaSach.isEmpty() && !diaChi.isEmpty()) {
                    NhaSach nhaSachMoi = new NhaSach(maNhaSach, tenNhaSach, diaChi, iconUriString);

                    // Thêm nhà sách mới vào DB
                    new DBHelper(ThemNhaSachActivity.this).addBookStore(nhaSachMoi);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("nhaSachMoi", nhaSachMoi); // Truyền đối tượng NhaSach
                    setResult(RESULT_OK, resultIntent); // Thiết lập kết quả OK
                    finish(); // Quay lại MainActivity
                } else {
                    Toast.makeText(ThemNhaSachActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý nút hủy
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED); // Thiết lập kết quả là hủy
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            imgIcon.setImageURI(selectedImageUri);
        }
    }
}