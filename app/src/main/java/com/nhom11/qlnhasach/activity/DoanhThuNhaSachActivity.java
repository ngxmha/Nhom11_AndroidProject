package com.nhom11.qlnhasach.activity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.nhom11.qlnhasach.R;
import com.nhom11.qlnhasach.activity.DBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DoanhThuNhaSachActivity extends AppCompatActivity {

    private TextView tvNam;
    private int namHienTai = Calendar.getInstance().get(Calendar.YEAR); // Lấy năm hiện tại
    private BarChart barChart;
    private SQLiteDatabase db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doanh_thu_nha_sach);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Doanh thu nhà sách");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvNam = findViewById(R.id.tvNam);
        barChart = findViewById(R.id.barChart);
        ImageView btnPrevYear = findViewById(R.id.btnPrevYear);
        ImageView btnNextYear = findViewById(R.id.btnNextYear);

        tvNam.setText(String.valueOf(namHienTai));

        btnPrevYear.setOnClickListener(v -> {
            namHienTai--;
            tvNam.setText(String.valueOf(namHienTai));
            loadBarChartData();
        });

        btnNextYear.setOnClickListener(v -> {
            namHienTai++;
            tvNam.setText(String.valueOf(namHienTai));
            loadBarChartData();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = new DBHelper(this).getReadableDatabase();
        loadBarChartData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadBarChartData() {
        barChart.clear();
        barChart.setNoDataText("Không có dữ liệu doanh thu cho năm " + namHienTai);
        barChart.setNoDataTextColor(Color.RED);

        // 1. Truy vấn dữ liệu từ database
        String sql = "SELECT " +
                "a.store_id_fk AS store_id, " +
                "b.store_name, " +
                "SUM(a.total_price) AS revenue, " + // Sử dụng total_price từ bảng Invoices
                "CASE " +
                "   WHEN strftime('%m', a.date) BETWEEN '01' AND '03' THEN 1 " +
                "   WHEN strftime('%m', a.date) BETWEEN '04' AND '06' THEN 2 " +
                "   WHEN strftime('%m', a.date) BETWEEN '07' AND '09' THEN 3 " +
                "   ELSE 4 " +
                "END AS quarter " +
                "FROM Invoices a " +
                "JOIN Bookstores b ON a.store_id_fk = b.store_id " +
                "WHERE strftime('%Y', a.date) = ? " +
                "GROUP BY a.store_id_fk, quarter " +
                "ORDER BY a.store_id_fk, quarter";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(namHienTai)});

        // 2. Xử lý dữ liệu
        Map<Integer, String> storeNames = new HashMap<>();
        Map<Integer, float[]> revenueData = new HashMap<>();

        while (cursor.moveToNext()) {
            int storeId = cursor.getInt(0);
            String storeName = cursor.getString(1);
            float revenue = cursor.getFloat(2);
            int quarter = cursor.getInt(3);

            if (!storeNames.containsKey(storeId)) {
                storeNames.put(storeId, storeName);
            }

            if (!revenueData.containsKey(storeId)) {
                revenueData.put(storeId, new float[]{0, 0, 0, 0});
            }

            revenueData.get(storeId)[quarter - 1] = revenue;
        }
        cursor.close();

        // 3. Tạo dữ liệu biểu đồ
        if (revenueData.isEmpty()) {
            barChart.invalidate();
            return;
        }

        List<BarEntry> q1Entries = new ArrayList<>();
        List<BarEntry> q2Entries = new ArrayList<>();
        List<BarEntry> q3Entries = new ArrayList<>();
        List<BarEntry> q4Entries = new ArrayList<>();
        List<String> storeLabels = new ArrayList<>();

        int index = 0;
        for (Map.Entry<Integer, float[]> entry : revenueData.entrySet()) {
            int storeId = entry.getKey();
            float[] quarterlyRevenue = entry.getValue();

            storeLabels.add(storeNames.get(storeId));
            q1Entries.add(new BarEntry(index, quarterlyRevenue[0]));
            q2Entries.add(new BarEntry(index, quarterlyRevenue[1]));
            q3Entries.add(new BarEntry(index, quarterlyRevenue[2]));
            q4Entries.add(new BarEntry(index, quarterlyRevenue[3]));
            index++;
        }

        // 4. Tạo dataset
        BarDataSet setQ1 = createDataSet(q1Entries, "Quý 1", Color.parseColor("#FFA000")); // Cam
        BarDataSet setQ2 = createDataSet(q2Entries, "Quý 2", Color.parseColor("#4CAF50")); // Xanh lá
        BarDataSet setQ3 = createDataSet(q3Entries, "Quý 3", Color.parseColor("#2196F3")); // Xanh dương
        BarDataSet setQ4 = createDataSet(q4Entries, "Quý 4", Color.parseColor("#F44336")); // Đỏ

        BarData data = new BarData(setQ1, setQ2, setQ3, setQ4);
        data.setValueFormatter(new CurrencyFormatter()); // Định dạng tiền tệ

        // 5. Cấu hình biểu đồ
        float barWidth = 0.1f;
        float groupSpace = 0.4f;
        float barSpace = 0.05f;
        float groupStart = 0f; // Định nghĩa groupStart

        barChart.setData(data);
        barChart.getBarData().setBarWidth(barWidth);
        barChart.groupBars(-0.5f, groupSpace, barSpace); // Nhóm cột với groupStart

        // Cấu hình trục X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(storeLabels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f); // Một nhãn cho mỗi nhóm
        xAxis.setLabelCount(storeLabels.size());
        xAxis.setLabelRotationAngle(-45);


        // Cấu hình trục Y
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setValueFormatter(new CurrencyFormatter());
        leftAxis.setAxisMinimum(0f);
        barChart.getAxisRight().setEnabled(false);

        // Cấu hình chung
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        barChart.setExtraOffsets(0, 0, 0, 16);
        barChart.animateY(1000);
        barChart.invalidate();
    }

    private BarDataSet createDataSet(List<BarEntry> entries, String label, int color) {
        BarDataSet dataSet = new BarDataSet(entries, label);
        dataSet.setColor(color);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(10f);
        dataSet.setHighlightEnabled(true);
        return dataSet;
    }

    // Lớp định dạng hiển thị tiền tệ
    public class CurrencyFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.format(Locale.getDefault(), "%,.0f đ", value);
        }
    }
}