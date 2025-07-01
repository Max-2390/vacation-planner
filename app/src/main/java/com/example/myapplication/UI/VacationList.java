package com.example.myapplication.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.Repository;
import com.example.myapplication.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class VacationList extends AppCompatActivity {
    private Repository repository;
    private List<Vacation> currentFilteredVacations;
    private EditText startDateEditText, endDateEditText;
    private VacationAdapter vacationAdapter;
    private Button generateReportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(VacationList.this, VacationDetails.class);
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        repository = new Repository(getApplication());
        List<Vacation> allVacations = repository.getxAllVacations();

        vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);

        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        Button searchButton = findViewById(R.id.searchButton);
        generateReportButton = findViewById(R.id.generateReportButton);

        generateReportButton.setEnabled(true);

        startDateEditText.setOnClickListener(v -> showDatePickerDialog(startDateEditText));
        endDateEditText.setOnClickListener(v -> showDatePickerDialog(endDateEditText));

        searchButton.setOnClickListener(v -> {
            String startDate = startDateEditText.getText().toString().trim();
            String endDate = endDateEditText.getText().toString().trim();

            if (startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(this, "Please enter both start and end dates", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidDate(startDate) || !isValidDate(endDate)) {
                Toast.makeText(this, "Dates must be in MM/dd/yyyy format", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                List<Vacation> filteredVacations = repository.getVacationsByDateRange(startDate, endDate);
                runOnUiThread(() -> {
                    currentFilteredVacations = filteredVacations;
                    vacationAdapter.setVacations(filteredVacations);
                    generateReportButton.setEnabled(!filteredVacations.isEmpty());
                });
            }).start();
        });

        generateReportButton.setOnClickListener(v -> {
            if (currentFilteredVacations == null || currentFilteredVacations.isEmpty()) {
                Toast.makeText(this, "No vacations to generate a report for.", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(VacationList.this, ReportActivity.class);
            intent.putParcelableArrayListExtra("filteredVacations", new ArrayList<>(currentFilteredVacations));
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentFilteredVacations != null && !currentFilteredVacations.isEmpty()) {
            vacationAdapter.setVacations(currentFilteredVacations);
            generateReportButton.setEnabled(true);
        } else {
            List<Vacation> allVacations = repository.getxAllVacations();
            vacationAdapter.setVacations(allVacations);
            generateReportButton.setEnabled(false);
        }
    }

    private void showDatePickerDialog(EditText targetEditText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    String formattedDate = String.format(Locale.US, "%02d/%02d/%04d", selectedMonth + 1, selectedDay, selectedYear);
                    targetEditText.setText(formattedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
