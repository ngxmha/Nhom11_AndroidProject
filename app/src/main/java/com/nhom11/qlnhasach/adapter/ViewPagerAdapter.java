package com.nhom11.qlnhasach.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.nhom11.qlnhasach.fragment.BillFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new BillFragment();
            case 1:
                return new BillFragment();
            case 2:
                return new BillFragment();
            case 4:
                return new BillFragment();
            default:
                return new BillFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Số lượng Fragment bạn có
    }
}
