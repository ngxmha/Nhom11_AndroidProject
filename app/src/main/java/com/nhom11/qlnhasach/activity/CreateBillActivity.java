package com.nhom11.qlnhasach.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom11.qlnhasach.R;
import com.nhom11.qlnhasach.adapter.BookBillAdapter;
import com.nhom11.qlnhasach.model.Book;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CreateBillActivity extends AppCompatActivity {
    private RecyclerView recyclerBook;
    private List<Book> bookList;
    private List<Book> filteredBookList;
    private BookBillAdapter adapter;
    private AppCompatButton btnCreateBill;
    private EditText edSearch;
    private ImageView btnClear;
    private TextView tvTotalMoney;
    private Spinner spinnerNS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Gán layout create_bill.xml
        setContentView(R.layout.create_bill);

        // Khởi tạo các view
        recyclerBook = findViewById(R.id.recyclerBook);
        edSearch = findViewById(R.id.edSearch);
        btnClear = findViewById(R.id.btnClear);
        tvTotalMoney = findViewById(R.id.tvTotalMoney);
        btnCreateBill = findViewById(R.id.btnCreateBill);
        spinnerNS = findViewById(R.id.spinnerNS);

        // Khởi tạo dữ liệu
        bookList = new ArrayList<>();
        filteredBookList = new ArrayList<>();

        // Thiết lập RecyclerView
        recyclerBook.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookBillAdapter(this, filteredBookList);
        recyclerBook.setAdapter(adapter);

        // Tải dữ liệu
        loadBookData();

        // Đăng ký listener cho sự kiện thay đổi số lượng
        adapter.setOnQuantityChangedListener(this::calculateTotal);

        // Thiết lập spinner nhà sách
        setupBookstoreSpinner();

        // Xử lý tìm kiếm
        setupSearchFunctionality();

        // Xử lý nút Clear
        btnClear.setOnClickListener(v -> {
            edSearch.setText("");
        });

        // Xử lý nút Lập hóa đơn
        btnCreateBill.setOnClickListener(v -> {
            if (isAnyBookSelected()) {
                // TODO: Thực hiện tạo hóa đơn
                Toast.makeText(this, "Đã tạo hóa đơn thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Vui lòng chọn ít nhất một cuốn sách", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSearchFunctionality() {
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterBooks(s.toString());
            }
        });
    }

    private void filterBooks(String query) {
        filteredBookList.clear();

        if (query.isEmpty()) {
            filteredBookList.addAll(bookList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Book book : bookList) {
                if (book.getMaSach().toLowerCase().contains(lowerCaseQuery) ||
                        book.getTenSach().toLowerCase().contains(lowerCaseQuery) ||
                        book.getTacGia().toLowerCase().contains(lowerCaseQuery)) {
                    filteredBookList.add(book);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    private boolean isAnyBookSelected() {
        // Kiểm tra xem có bất kỳ sách nào được chọn không dựa trên dữ liệu từ adapter
        for (int i = 0; i < filteredBookList.size(); i++) {
            Book book = filteredBookList.get(i);
            if (adapter.getQuantity(book.getMaSach()) > 0) {
                return true;
            }
        }
        return false;
    }

    private void calculateTotal() {
        // Sử dụng hàm trợ giúp từ adapter để tính tổng tiền
        double total = adapter.getTotalAmount();

        // Hiển thị tổng tiền
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvTotalMoney.setText(formatter.format(total) + " đ");
    }

    private void loadBookData() {
        // Thêm dữ liệu mẫu (có thể thay thế bằng việc lấy từ cơ sở dữ liệu)
        bookList.add(new Book("B001", "Đắc Nhân Tâm", "Dale Carnegie", 150000));
        bookList.add(new Book("B002", "Nhà Giả Kim", "Paulo Coelho", 120000));
        bookList.add(new Book("B003", "Cây Cam Ngọt Của Tôi", "José Mauro de Vasconcelos", 85000));
        bookList.add(new Book("B004", "Tôi Tài Giỏi, Bạn Cũng Thế", "Adam Khoo", 110000));
        bookList.add(new Book("B005", "Tuổi Trẻ Đáng Giá Bao Nhiêu", "Rosie Nguyễn", 75000));
        bookList.add(new Book("B006", "Người Giàu Có Nhất Thành Babylon", "George S. Clason", 65000));
        bookList.add(new Book("B007", "Mặc Kệ Thiên Hạ, Sống Như Người Nhật", "Mari Tamagawa", 60000));
        bookList.add(new Book("B008", "Hành Trình Về Phương Đông", "Baird T. Spalding", 80000));

        // Khởi tạo danh sách lọc với tất cả sách
        filteredBookList.addAll(bookList);
        adapter.notifyDataSetChanged();
    }

    private void setupBookstoreSpinner() {
        // Danh sách nhà sách (có thể lấy từ cơ sở dữ liệu)
        List<String> bookstores = new ArrayList<>();
        bookstores.add("Nhà sách Phương Nam");
        bookstores.add("Nhà sách Fahasa");
        bookstores.add("Nhà sách Kim Đồng");

        // Thiết lập adapter cho Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, bookstores);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNS.setAdapter(spinnerAdapter);
    }
}