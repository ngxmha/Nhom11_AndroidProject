package com.nhom11.qlnhasach.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom11.qlnhasach.R;
import com.nhom11.qlnhasach.model.NhaSach;

public class SuaNhaSachActivity extends AppCompatActivity {

    private ImageView imageViewBack;
    private EditText editTextMaNhaSach;
    private EditText editTextTenNhaSach;
    private EditText editTextDiaChi;
    private ImageView imageViewIcon;
    private Button buttonChooseIcon;
    private Button buttonSave;
    private Button buttonCancel;

    private NhaSach nhaSachCanSua;
    private Uri selectedImageUri = null;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_nha_sach);

        imageViewBack = findViewById(R.id.imageViewBack);
        editTextMaNhaSach = findViewById(R.id.editTextMaNhaSach);
        editTextTenNhaSach = findViewById(R.id.editTextTenNhaSach);
        editTextDiaChi = findViewById(R.id.editTextDiaChi);
        imageViewIcon = findViewById(R.id.imageViewIcon);
        buttonChooseIcon = findViewById(R.id.buttonChooseIcon);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);

        nhaSachCanSua = (NhaSach) getIntent().getSerializableExtra("nhaSach");

        if (nhaSachCanSua != null) {
            editTextMaNhaSach.setText(nhaSachCanSua.getMaNhaSach());
            editTextTenNhaSach.setText(nhaSachCanSua.getTenNhaSach());
            editTextDiaChi.setText(nhaSachCanSua.getDiaChi());
            if (nhaSachCanSua.getIconUri() != null) {
                selectedImageUri = Uri.parse(nhaSachCanSua.getIconUri());
                imageViewIcon.setImageURI(selectedImageUri);
            } else {
                imageViewIcon.setImageResource(R.drawable.ic_launcher_background);
            }
            editTextMaNhaSach.setEnabled(false);
        }

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonChooseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nhaSachCanSua != null) {
                    String tenNhaSachMoi = editTextTenNhaSach.getText().toString().trim();
                    String diaChiMoi = editTextDiaChi.getText().toString().trim();
                    String iconUriMoi = selectedImageUri != null ? selectedImageUri.toString() : nhaSachCanSua.getIconUri();

                    if (!tenNhaSachMoi.isEmpty() && !diaChiMoi.isEmpty()) {
                        nhaSachCanSua.setTenNhaSach(tenNhaSachMoi);
                        nhaSachCanSua.setDiaChi(diaChiMoi);
                        nhaSachCanSua.setIconUri(iconUriMoi);

                        // Sửa nhà sách trong DB
                        new DBHelper(SuaNhaSachActivity.this).updateBookStore(nhaSachCanSua);

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("nhaSachDaSua", nhaSachCanSua); // Truyền đối tượng đã sửa
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Toast.makeText(SuaNhaSachActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            imageViewIcon.setImageURI(selectedImageUri);
        }
    }
}