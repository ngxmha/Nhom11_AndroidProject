package com.nhom11.qlnhasach.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nhom11.qlnhasach.R;
import com.nhom11.qlnhasach.model.Book;

import java.util.List;

public class BookAdapter extends BaseAdapter {

    private Context context;
    private List<Book> bookList;

    public BookAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView tvMaSach, tvTenSach, tvTacGia, tvGia;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
            holder = new ViewHolder();
            holder.tvMaSach = convertView.findViewById(R.id.tvMaSach);
            holder.tvTenSach = convertView.findViewById(R.id.tvTenSach);
            holder.tvTacGia = convertView.findViewById(R.id.tvTacGia);
            holder.tvGia = convertView.findViewById(R.id.tvGia);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Book book = bookList.get(position);
        holder.tvMaSach.setText(book.getMaSach());
        holder.tvTenSach.setText(book.getTenSach());
        holder.tvTacGia.setText(book.getTacGia());
        holder.tvGia.setText(formatCurrency(book.getGia())); // ✅ đặt sau khi đã gán holder.tvGia

        return convertView;
    }

    private String formatCurrency(double amount) {
        java.text.NumberFormat formatter = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));
        return formatter.format(amount) + " VNĐ";
    }

}