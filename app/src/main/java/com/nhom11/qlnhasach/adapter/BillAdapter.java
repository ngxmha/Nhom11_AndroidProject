package com.nhom11.qlnhasach.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom11.qlnhasach.R;
import com.nhom11.qlnhasach.activity.DBHelper;
import com.nhom11.qlnhasach.model.Bill;
import com.nhom11.qlnhasach.model.NhaSach;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {
    Context context;
    private List<Bill> billList;

    public BillAdapter(Context context, List<Bill> billList) {
        this.context = context;
        this.billList = billList;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Bill bill = billList.get(position);
        holder.tvSoHD.setText(bill.getSoHD());
        String tenNS = "";
        for(NhaSach ns : new DBHelper(context).getAllBookstores()){
            if(ns.getMaNhaSach().equals(bill.getIdNS())){
                holder.tvTenNS.setText(ns.getTenNhaSach());
                break;
            }
        }
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        holder.tvTotalMoney.setText(formatter.format(bill.getTotalMoney()) + " VNĐ");
        holder.tvNgayHD.setText(bill.getNgayHD());
    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    public class BillViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSoHD, tvTenNS, tvTotalMoney, tvNgayHD;
        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSoHD = itemView.findViewById(R.id.tvSoHD);
            tvTenNS = itemView.findViewById(R.id.tvTenNS);
            tvTotalMoney = itemView.findViewById(R.id.tvTotalMoney);
            tvNgayHD = itemView.findViewById(R.id.tvNgayHD);
        }
    }
}
