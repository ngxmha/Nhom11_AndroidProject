package com.nhom11.qlnhasach.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhom11.qlnhasach.R;
import com.nhom11.qlnhasach.activity.DoanhThuNhaSachActivity;


public class ThongKeFragment extends Fragment {

    public ThongKeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_thongke, container, false);

        // Nút "Thống kê theo Nhà Sách"
        view.findViewById(R.id.btnTheoNhaSach).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DoanhThuNhaSachActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
