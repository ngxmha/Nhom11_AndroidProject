package com.nhom11.qlnhasach.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nhom11.qlnhasach.R;
import com.nhom11.qlnhasach.adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNav;
    private ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kiểm tra trạng thái đăng nhập
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            // Nếu chưa đăng nhập, chuyển về LoginActivity
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }

        // Đã đăng nhập → load giao diện
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        bottomNav = findViewById(R.id.bottomNav);

        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        // Chuyển fragment theo bottom nav
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navNhaSach) {
                viewPager.setCurrentItem(0);
                return true;
            } else if (item.getItemId() == R.id.navSach) {
                viewPager.setCurrentItem(1);
                return true;
            }
            else {
                viewPager.setCurrentItem(2);
                return true;
            }
        });

        bottomNav.setSelectedItemId(R.id.navNhaSach);
    }
}