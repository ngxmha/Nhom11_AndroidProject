package com.nhom11.qlnhasach.activity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.nhom11.qlnhasach.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DoanhThuSachActivity extends AppCompatActivity {

    private TextView tvYear;
    private Switch switchAll;
    private BarChart barChart;
    private int currentYear = Calendar.getInstance().get(Calendar.YEAR); // Năm hiện tại
    private SQLiteDatabase db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doanh_thu_sach);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Doanh thu sách");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvYear = findViewById(R.id.tv_year);
        switchAll = findViewById(R.id.switch_all);
        barChart = findViewById(R.id.bar_chart);
        ImageButton btnPrevYear = findViewById(R.id.btn_prev_year);
        ImageButton btnNextYear = findViewById(R.id.btn_next_year);

        tvYear.setText(String.valueOf(currentYear));

        // Sự kiện thay đổi năm
        btnPrevYear.setOnClickListener(v -> {
            currentYear--;
            tvYear.setText(String.valueOf(currentYear));
            loadBarChartData();
        });

        btnNextYear.setOnClickListener(v -> {
            currentYear++;
            tvYear.setText(String.valueOf(currentYear));
            loadBarChartData();
        });

        // Sự kiện Switch
        switchAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
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
        barChart.setNoDataText("Không có dữ liệu doanh thu cho năm " + currentYear);
        barChart.setNoDataTextColor(Color.RED);

        List<String> bookTitles = new ArrayList<>();
        List<Float> revenues = new ArrayList<>();

        if (switchAll.isChecked()) {
            // Truy vấn tổng doanh thu tất cả các năm
            String sql = "SELECT ib.title AS book_title, " +
                    "SUM(ib.quantity * ib.price) AS revenue " +
                    "FROM InvoiceBooks ib " +
                    "JOIN Invoices i ON ib.invoice_id_fk = i.invoice_id " +
                    "GROUP BY ib.title " +
                    "ORDER BY revenue DESC";

            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                bookTitles.add(cursor.getString(0));
                revenues.add(cursor.getFloat(1));
            }
            cursor.close();
        } else {
            // Truy vấn doanh thu theo năm
            String sql =  "SELECT ib.title AS book_title, " +
                    "SUM(ib.quantity * ib.price) AS revenue " +
                    "FROM InvoiceBooks ib " +
                    "JOIN Invoices i ON ib.invoice_id_fk = i.invoice_id " +
                    "WHERE strftime('%Y', i.date) = ? " +
                    "GROUP BY ib.title " +
                    "ORDER BY revenue DESC";

            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(currentYear)});
            while (cursor.moveToNext()) {
                bookTitles.add(cursor.getString(0));
                revenues.add(cursor.getFloat(1));
            }
            cursor.close();
        }

        // Tạo dữ liệu biểu đồ
        if (bookTitles.isEmpty()) {
            barChart.invalidate();
            return;
        }

        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < revenues.size(); i++) {
            entries.add(new BarEntry(i, revenues.get(i)));
        }

        // Tạo dataset
        BarDataSet dataSet = new BarDataSet(entries, switchAll.isChecked() ? "Tổng doanh thu" : "Doanh thu năm " + currentYear);
        dataSet.setColor(Color.parseColor("#3F51B5")); // Xanh đậm
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(10f);
        dataSet.setHighlightEnabled(true);

        BarData data = new BarData(dataSet);
        data.setValueFormatter(new CurrencyFormatter());
        data.setBarWidth(0.7f);

        // Cấu hình biểu đồ
        barChart.setData(data);

        // Cấu hình trục X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(bookTitles));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(bookTitles.size());
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

    // Lớp định dạng hiển thị tiền tệ
    public class CurrencyFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.format(Locale.getDefault(), "%,.0f đ", value);
        }
    }
}