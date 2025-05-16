package com.nhom11.qlnhasach.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.nhom11.qlnhasach.activity.DBHelper;
import com.nhom11.qlnhasach.adapter.BillAdapter;
import com.nhom11.qlnhasach.model.Bill;

import java.util.ArrayList;
import java.util.List;

public class BillFragment extends Fragment {
    private RecyclerView recyclerBill;
    private BillAdapter adapter;
    private List<Bill> billList;
    private FloatingActionButton fab;
    private int selectedItem = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);

        recyclerBill = view.findViewById(R.id.recyclerBill);
        fab = view.findViewById(R.id.fab);

        billList = new ArrayList<>();
        adapter = new BillAdapter(requireContext(), billList);

        recyclerBill.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerBill.setAdapter(adapter);

//        Fake data để test
//        loadBillData();

        // Đăng ký menu context cho recyclerBill
//        registerForContextMenu(recyclerBill);

        recyclerBill.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
                registerForContextMenu(view);
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                unregisterForContextMenu(view);
            }
        });

        // Xử lí nút FAB
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CreateBillActivity.class);
            startActivity(intent);
        });
        return view;
    }

    private void loadBillData() {
//        billList.add(new Bill("Nhà sách PTIT", 123000, "01/01/2025 - 12:00"));
//        billList.add(new Bill("Nhà sách HUST", 150000, "01/01/2025 - 12:00"));
//        billList.add(new Bill( "Nhà sách VNU", 56000, "01/01/2025 - 12:00"));
//        billList.add(new Bill( "Nhà sách KMA", 56000, "01/01/2025 - 12:00"));

        billList.addAll(new DBHelper(requireContext()).getAllBills());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.context_menu, menu);
        selectedItem = recyclerBill.getChildAdapterPosition(v);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_delete && selectedItem != -1){
            Bill bill = billList.get(selectedItem);
            new DBHelper(requireContext()).deleteBill(bill.getSoHD());
            billList.remove(selectedItem);
            adapter.notifyItemRemoved(selectedItem);
            selectedItem = -1;
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        billList.clear();
        loadBillData();
    }
}
