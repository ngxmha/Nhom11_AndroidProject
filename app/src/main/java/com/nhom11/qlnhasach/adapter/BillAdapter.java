package com.nhom11.qlnhasach.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom11.qlnhasach.R;
import com.nhom11.qlnhasach.model.Bill;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {
    private List<Bill> billList;

    public BillAdapter(List<Bill> billList) {
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
        holder.tvTenNS.setText(bill.getTenNS());
        holder.tvTotalMoney.setText(bill.getTotalMoney());
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
