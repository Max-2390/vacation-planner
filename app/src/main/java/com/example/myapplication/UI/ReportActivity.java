package com.example.myapplication.UI;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.database.Repository;
import com.example.myapplication.entities.Excursion;
import com.example.myapplication.entities.Vacation;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    private Repository repository;
    private LinearLayout reportContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        repository = new Repository(getApplication());
        reportContainer = findViewById(R.id.reportContainer);

        loadReport();
    }

    private void loadReport() {
        ArrayList<Vacation> vacations = getIntent().getParcelableArrayListExtra("filteredVacations");

        if (vacations == null || vacations.isEmpty()) {
            Toast.makeText(this, "No vacations found to display.", Toast.LENGTH_LONG).show();
            return;
        }

        reportContainer.removeAllViews();

        for (Vacation v : vacations) {
            LinearLayout vacationCard = new LinearLayout(this);
            vacationCard.setOrientation(LinearLayout.VERTICAL);
            vacationCard.setPadding(24, 24, 24, 24);
            vacationCard.setBackgroundColor(Color.parseColor("#009688"));  // Teal green color

            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            cardParams.setMargins(0, 0, 0, 24);
            vacationCard.setLayoutParams(cardParams);

            // Vacation info
            TextView title = new TextView(this);
            title.setText(v.getVacationTitle());
            title.setTextSize(20);
            title.setTypeface(null, Typeface.BOLD);
            title.setPadding(0, 0, 0, 8);
            title.setTextColor(Color.WHITE);  // Better contrast on teal background
            vacationCard.addView(title);

            TextView hotel = new TextView(this);
            hotel.setText("Hotel: " + v.getVacationHotel());
            hotel.setPadding(0, 0, 0, 4);
            hotel.setTextColor(Color.WHITE);
            vacationCard.addView(hotel);

            TextView dates = new TextView(this);
            dates.setText("Dates: " + v.getStartDate() + " to " + v.getEndDate());
            dates.setPadding(0, 0, 0, 12);
            dates.setTextColor(Color.WHITE);
            vacationCard.addView(dates);

            // Excursions
            List<Excursion> excursions = repository.getAssociatedExcursions(v.getVacationID());
            if (excursions.isEmpty()) {
                TextView noExcursions = new TextView(this);
                noExcursions.setText("No excursions.");
                noExcursions.setTypeface(null, Typeface.ITALIC);
                noExcursions.setPadding(0, 0, 0, 4);
                noExcursions.setTextColor(Color.WHITE);
                vacationCard.addView(noExcursions);
            } else {
                for (Excursion e : excursions) {
                    TextView excursionView = new TextView(this);
                    excursionView.setText("• " + e.getTitle() + " (" + e.getDate() + ")");
                    excursionView.setPadding(12, 0, 0, 4);
                    excursionView.setTextColor(Color.WHITE);
                    vacationCard.addView(excursionView);
                }
            }

            reportContainer.addView(vacationCard);
        }
    }
    // Force apk build
}
