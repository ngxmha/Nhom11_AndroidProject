package com.nhom11.qlnhasach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nhom11.qlnhasach.activity.BookFormActivity;
import com.nhom11.qlnhasach.R;
import com.nhom11.qlnhasach.activity.DBHelper;
import com.nhom11.qlnhasach.adapter.BookAdapter;
import com.nhom11.qlnhasach.activity.MainActivity;
import com.nhom11.qlnhasach.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookFragment extends Fragment {

    private ListView recyclerBook;
    private EditText edSearch;
    private ImageView btnClear;
    private FloatingActionButton fab;
    private BookAdapter adapter;
    private List<Book> bookList = new ArrayList<>();
    private List<Book> filteredList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        recyclerBook = view.findViewById(R.id.recyclerBook);
        edSearch = view.findViewById(R.id.edSearch);
        btnClear = view.findViewById(R.id.btnClear);
        fab = view.findViewById(R.id.fab); // ánh xạ lại fab

//        loadDummyData();
        bookList = new DBHelper(requireContext()).getAllBooks();

        filteredList.addAll(bookList);
        adapter = new BookAdapter(requireContext(), filteredList);
        recyclerBook.setAdapter(adapter);

        btnClear.setOnClickListener(v -> {
            edSearch.setText("");
            filteredList.clear();
            filteredList.addAll(bookList);
            adapter.notifyDataSetChanged();
        });

        edSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        edSearch.setSingleLine(true);

        edSearch.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                String query = edSearch.getText().toString().trim();
                filterBooks(query);
                return true;
            }
            return false;
        });

        // 👉 FAB mở BookFormActivity
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), BookFormActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Cập nhật dữ liệu từ cơ sở dữ liệu
        loadBooksFromDatabase();

        // Kiểm tra xem có sách mới được thêm vào từ BookFormActivity không
        if (!BookFormActivity.bookList.isEmpty()) {
            BookFormActivity.bookList.clear();  // Đã được lưu trong DB và tải lại ở trên
        }
    }

    private void filterBooks(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(bookList);
        } else {
            for (Book book : bookList) {
                if (book.getTenSach().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(book);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void loadBooksFromDatabase() {
        // Xóa dữ liệu cũ
        bookList.clear();
        filteredList.clear();

        // Lấy dữ liệu mới từ database
        List<Book> booksFromDB = MainActivity.databaseManager.getAllBooks();
        bookList.addAll(booksFromDB);
        filteredList.addAll(bookList);

        // Cập nhật adapter
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}