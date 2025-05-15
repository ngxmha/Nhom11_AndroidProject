package com.nhom11.qlnhasach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nhom11.qlnhasach.R;
import com.nhom11.qlnhasach.activity.CreateBillActivity;
import com.nhom11.qlnhasach.adapter.BillAdapter;
import com.nhom11.qlnhasach.model.Bill;

import java.util.ArrayList;
import java.util.List;

public class BillFragment extends Fragment {
    private RecyclerView recyclerBill;
    private BillAdapter adapter;
    private List<Bill> billList;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);

        recyclerBill = view.findViewById(R.id.recyclerBill);
        fab = view.findViewById(R.id.fab);

        billList = new ArrayList<>();
        adapter = new BillAdapter(billList);

        recyclerBill.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerBill.setAdapter(adapter);

//        Fake data để test
        loadBillData();

        // Xử lí nút FAB
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CreateBillActivity.class);
            startActivity(intent);
        });
        return view;
    }

    private void loadBillData() {
        billList.add(new Bill("HD001", "Nhà sách PTIT", "123000", "01/01/2025 - 12:00"));
        billList.add(new Bill("HD002", "Nhà sách HUST", "150000", "01/01/2025 - 12:00"));
        billList.add(new Bill("HD003", "Nhà sách KMA", "56000", "01/01/2025 - 12: 00"));
        adapter.notifyDataSetChanged();
    }
}
