package com.sofudev.trackapptrl;

import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReportSalesActivity extends AppCompatActivity {

    PieChart pieChart;
    List<PieEntry> entryList = new ArrayList<>();
    Description description = new Description();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_sales);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        pieChart = findViewById(R.id.report_sales_piechart);
        description.setText("Sample Pie Chart");

//        pieChart.setUsePercentValues(true);
        pieChart.setUsePercentValues(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setDescription(description);
        pieChart.animateXY(1400, 1400);

        setItem();
    }

    private void setItem()
    {
        entryList.add(new PieEntry(100000f, "Januari", 0));
        entryList.add(new PieEntry(200000f, "Pebruari", 1));
        entryList.add(new PieEntry(120000f, "Maret", 2));
        entryList.add(new PieEntry(150000f, "April", 3));
        entryList.add(new PieEntry(143000f, "Mei", 4));
        entryList.add(new PieEntry(175000f, "Juni", 5));
        entryList.add(new PieEntry(190000f, "Juli", 6));
        entryList.add(new PieEntry(230000f, "Agustus", 7));
        entryList.add(new PieEntry(122000f, "September", 8));
        entryList.add(new PieEntry(150000f, "Oktober", 9));
        entryList.add(new PieEntry(163000f, "November", 10));
        entryList.add(new PieEntry(122000f, "Desember", 11));

        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                Locale locId = new Locale("in", "ID");
//                NumberFormat idr = NumberFormat.getCurrencyInstance(locId);
//                DecimalFormat idr = (DecimalFormat) DecimalFormat.getCurrencyInstance();
//                DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
//
//                formatRp.setCurrencySymbol("Rp. ");
//                formatRp.setMonetaryDecimalSeparator(',');
//                formatRp.setGroupingSeparator('.');
//
//                idr.setDecimalFormatSymbols(formatRp);



//                return "Rp. " + super.getFormattedValue(value);
//                return idr.format(value);
//                return idr.format(value);

                String strFormat ="#,###";
                DecimalFormat df = new DecimalFormat(strFormat,new DecimalFormatSymbols(Locale.GERMAN));
                return "Rp. " + df.format(value);
            }
        };



        PieDataSet pieSet = new PieDataSet(entryList, "");
        pieSet.setColors(NEW_COLORS);
        PieData pieData = new PieData(pieSet);
//        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieData.setValueFormatter(formatter);
        pieData.setValueTextSize(15f);
        pieData.setValueTextColor(Color.WHITE);

        pieChart.setData(pieData);
    }

    public static final int[] NEW_COLORS = {
            Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31), Color.rgb(179, 100, 53), Color.rgb(192, 255, 140),
            Color.rgb(255, 247, 140), Color.rgb(255, 208, 140),  Color.rgb(140, 234, 255),
            Color.rgb(255, 140, 157), Color.rgb(106, 167, 134), Color.rgb(53, 194, 209)
    };
}
