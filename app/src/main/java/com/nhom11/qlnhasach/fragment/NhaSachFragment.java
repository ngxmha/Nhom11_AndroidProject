package com.nhom11.qlnhasach.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nhom11.qlnhasach.R;
import com.nhom11.qlnhasach.activity.SuaNhaSachActivity;
import com.nhom11.qlnhasach.activity.ThemNhaSachActivity;
import com.nhom11.qlnhasach.adapter.NhaSachAdapter;
import com.nhom11.qlnhasach.model.NhaSach;

import java.util.ArrayList;
import java.util.List;

public class NhaSachFragment extends Fragment implements NhaSachAdapter.OnItemClickListener {

    private RecyclerView recyclerViewNhaSach;
    private NhaSachAdapter adapter;
    private List<NhaSach> nhaSachList;

    private FloatingActionButton fabAdd;
    private Button buttonEdit;
    private Button buttonDelete;

    private NhaSach selectedNhaSach = null;
    private static final int REQUEST_ADD_NHASACH = 1;
    private static final int REQUEST_EDIT_NHASACH = 2;

    public NhaSachFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nhasach, container, false);

        recyclerViewNhaSach = view.findViewById(R.id.recyclerViewNhaSach);
        recyclerViewNhaSach.setLayoutManager(new LinearLayoutManager(getContext()));

        nhaSachList = new ArrayList<>();
        nhaSachList.add(new NhaSach("MS001", "Nhà Sách Phương Nam", "123 Nguyễn Du", null));
        nhaSachList.add(new NhaSach("MS002", "Nhà Sách Cá Chép", "456 Trần Hưng Đạo", null));

        adapter = new NhaSachAdapter(getContext(), nhaSachList);
        adapter.setOnItemClickListener(this);
        recyclerViewNhaSach.setAdapter(adapter);

        fabAdd = view.findViewById(R.id.fabAdd);
        buttonEdit = view.findViewById(R.id.buttonEdit);
        buttonDelete = view.findViewById(R.id.buttonDelete);

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ThemNhaSachActivity.class);
            startActivityForResult(intent, REQUEST_ADD_NHASACH);
        });

        buttonEdit.setOnClickListener(v -> {
            if (selectedNhaSach != null) {
                Intent intent = new Intent(getActivity(), SuaNhaSachActivity.class);
                intent.putExtra("nhaSach", selectedNhaSach);
                startActivityForResult(intent, REQUEST_EDIT_NHASACH);
            } else {
                Toast.makeText(getContext(), "Vui lòng chọn một nhà sách để sửa", Toast.LENGTH_SHORT).show();
            }
        });

        buttonDelete.setOnClickListener(v -> {
            if (selectedNhaSach != null) {
                nhaSachList.remove(selectedNhaSach);
                adapter.notifyDataSetChanged();
                selectedNhaSach = null;
                buttonEdit.setEnabled(false);
                buttonDelete.setEnabled(false);
                Toast.makeText(getContext(), "Đã xóa nhà sách", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Vui lòng chọn một nhà sách để xóa", Toast.LENGTH_SHORT).show();
            }
        });

        // Initially disable edit & delete until an item is selected
        buttonEdit.setEnabled(false);
        buttonDelete.setEnabled(false);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_ADD_NHASACH) {
                NhaSach nhaSachMoi = (NhaSach) data.getSerializableExtra("nhaSachMoi");
                if (nhaSachMoi != null) {
                    nhaSachList.add(nhaSachMoi);
                    adapter.notifyDataSetChanged();
                }
            } else if (requestCode == REQUEST_EDIT_NHASACH) {
                NhaSach nhaSachDaSua = (NhaSach) data.getSerializableExtra("nhaSachDaSua");
                if (nhaSachDaSua != null) {
                    for (int i = 0; i < nhaSachList.size(); i++) {
                        if (nhaSachList.get(i).getMaNhaSach().equals(nhaSachDaSua.getMaNhaSach())) {
                            nhaSachList.set(i, nhaSachDaSua);
                            adapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onItemClick(NhaSach nhaSach) {
        selectedNhaSach = nhaSach;
        buttonEdit.setEnabled(true);
        buttonDelete.setEnabled(true);
    }
}
