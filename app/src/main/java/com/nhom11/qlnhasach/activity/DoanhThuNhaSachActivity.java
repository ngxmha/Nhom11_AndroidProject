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
import java.util.Arrays;
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
        String sql = "SELECT\n" +
                "    a.idNS AS store_id,\n" +
                "    b.tenNhaSach AS store_name,\n" +
                "    SUM(a.totalMoney) AS revenue,\n" +
                "    CASE\n" +
                "        WHEN CAST(substr(a.ngayHD, 4, 2) AS INTEGER) BETWEEN 1 AND 3 THEN 1\n" +
                "        WHEN CAST(substr(a.ngayHD, 4, 2) AS INTEGER) BETWEEN 4 AND 6 THEN 2\n" +
                "        WHEN CAST(substr(a.ngayHD, 4, 2) AS INTEGER) BETWEEN 7 AND 9 THEN 3\n" +
                "        ELSE 4\n" +
                "    END AS quarter\n" +
                "FROM Invoices a\n" +
                "JOIN Bookstores b ON a.idNS = b.maNhaSach\n" +
                "WHERE substr(a.ngayHD, 7, 4) = ?\n" +
                "GROUP BY a.idNS, quarter\n" +
                "ORDER BY a.idNS, quarter;";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(namHienTai)});

        // 2. Xử lý dữ liệu
        Map<String, String> storeNames = new HashMap<>();
        Map<String, float[]> revenueData = new HashMap<>();

        while (cursor.moveToNext()) {
            String storeId = cursor.getString(0);
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
        for (Map.Entry<String, float[]> entry : revenueData.entrySet()) {
            String storeId = entry.getKey();
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
        data.setValueFormatter(new CurrencyFormatter());

        // 5. Cấu hình biểu đồ
        float barWidth = 0.1f;
        float groupSpace = 0.3f;
        float barSpace = 0.05f;

        barChart.setData(data);
        barChart.getBarData().setBarWidth(barWidth);
        barChart.groupBars(-0.5f, groupSpace, barSpace); // Điều chỉnh groupBars bắt đầu từ 0

        // Kích hoạt cuộn ngang
        barChart.setDragEnabled(true); // Cho phép kéo
        barChart.setScaleEnabled(false); // Tắt phóng to/thu nhỏ
        barChart.setVisibleXRangeMaximum(2); // Hiển thị tối đa 2 nhà sách cùng lúc (giảm để dễ thấy hiệu ứng cuộn)
        barChart.setVisibleXRangeMinimum(1); // Đảm bảo khung nhìn tối thiểu
        barChart.moveViewToX(0); // Bắt đầu từ vị trí đầu tiên

        // Cấu hình trục X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(storeLabels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f); // Khoảng cách giữa các nhãn
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