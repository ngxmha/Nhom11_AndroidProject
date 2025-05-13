package com.nhom11.qlnhasach.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom11.qlnhasach.R;
import com.nhom11.qlnhasach.model.Book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookBillAdapter extends RecyclerView.Adapter<BookBillAdapter.BookBillViewHolder> {
    private List<Book> bookList;
    private Context context;
    private OnQuantityChangedListener quantityChangedListener;
    private Map<String, Integer> quantityMap = new HashMap<>();

    public interface OnQuantityChangedListener {
        void onQuantityChanged();
    }

    public void setOnQuantityChangedListener(OnQuantityChangedListener listener) {
        this.quantityChangedListener = listener;
    }

    public BookBillAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;

        // Khởi tạo số lượng ban đầu là 0 cho mỗi cuốn sách
        for (Book book : bookList) {
            quantityMap.put(book.getMaSach(), 0);
        }
    }

    public int getQuantity(String bookId) {
        Integer quantity = quantityMap.get(bookId);
        return quantity != null ? quantity : 0;
    }

    public double getTotalAmount() {
        double total = 0;
        for (Book book : bookList) {
            int quantity = getQuantity(book.getMaSach());
            total += quantity * book.getGia();
        }
        return total;
    }

    @NonNull
    @Override
    public BookBillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_bill, parent, false);
        return new BookBillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookBillViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.tvMaSach.setText(book.getMaSach());
        holder.tvTenSach.setText(book.getTenSach());
        holder.tvTacgia.setText(book.getTacGia());
        holder.tvDongia.setText(formatCurrency(book.getGia()));

        // Hiển thị số lượng đã lưu
        String bookId = book.getMaSach();
        int quantity = getQuantity(bookId);
        holder.tvNumber.setText(String.valueOf(quantity));

        holder.btnPlus.setOnClickListener(v -> {
            int count = Integer.parseInt(holder.tvNumber.getText().toString());
            count++;
            // Lưu số lượng mới
            quantityMap.put(bookId, count);
            holder.tvNumber.setText(String.valueOf(count));

            if (quantityChangedListener != null) {
                quantityChangedListener.onQuantityChanged();
            }
        });

        holder.btnMinus.setOnClickListener(v -> {
            int count = Integer.parseInt(holder.tvNumber.getText().toString());
            if (count > 0) {
                count--;
                // Lưu số lượng mới
                quantityMap.put(bookId, count);
                holder.tvNumber.setText(String.valueOf(count));

                if (quantityChangedListener != null) {
                    quantityChangedListener.onQuantityChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList == null ? 0 : bookList.size();
    }

    public class BookBillViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMaSach, tvTenSach, tvTacgia, tvDongia, tvNumber;
        public ImageView btnPlus, btnMinus;

        public BookBillViewHolder(View itemView) {
            super(itemView);
            tvMaSach = itemView.findViewById(R.id.tvMaSach);
            tvTenSach = itemView.findViewById(R.id.tvTenSach);
            tvTacgia = itemView.findViewById(R.id.tvTacGia);
            tvDongia = itemView.findViewById(R.id.tvDonGia);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
        }
    }

    private String formatCurrency(double amount) {
        java.text.NumberFormat formatter = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));
        return formatter.format(amount) + " VNĐ";
    }
}