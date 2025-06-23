package com.example.myapplication.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.Repository;
import com.example.myapplication.entities.Excursion;
import com.example.myapplication.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;
    private List<Vacation> currentFilteredVacations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VacationList.this, com.example.myapplication.UI.VacationDetails.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView=findViewById(R.id.recyclerview);
        repository=new Repository(getApplication());
        List<Vacation> allVacations=repository.getxAllVacations();
        final com.example.myapplication.UI.VacationAdapter vacationAdapter=new com.example.myapplication.UI.VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);

        EditText startDateEditText = findViewById(R.id.startDateEditText);
        EditText endDateEditText = findViewById(R.id.endDateEditText);
        Button searchButton = findViewById(R.id.searchButton);
        Button generateReportButton = findViewById(R.id.generateReportButton);


        searchButton.setOnClickListener(v -> {
            String startDate = startDateEditText.getText().toString().trim();
            String endDate = endDateEditText.getText().toString().trim();

            if (startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(this, "Please enter both start and end dates", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                List<Vacation> filteredVacations = repository.getVacationsByDateRange(startDate, endDate);
                runOnUiThread(() -> {
                    currentFilteredVacations = filteredVacations;
                    vacationAdapter.setVacations(filteredVacations);
                });
            }).start();
        });

        generateReportButton.setOnClickListener(v -> {
            if (currentFilteredVacations == null || currentFilteredVacations.isEmpty()) {
                Toast.makeText(this, "No vacations to generate a report for.", Toast.LENGTH_SHORT).show();
                return;
            }

            String csvContent = generateVacationCSV(currentFilteredVacations);
            String fileName = "Vacation_Report_" + System.currentTimeMillis() + ".csv";
            File file = saveReportToCSVFile(csvContent, fileName);

            Uri fileUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/csv");
            intent.putExtra(Intent.EXTRA_STREAM, fileUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Share Report Via"));
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.sample){
            repository = new Repository(getApplication());
            Vacation vacation = new Vacation(0, "Miami", "Double Tree", "01/03/25", "01/04/25");
            repository.insert(vacation);
            vacation = new Vacation(0, "Japan", "Hilton", "01/01/25", "01/14/25");
            repository.insert(vacation);
            Excursion excursion = new Excursion(0, "Hiking", 1, "02/03/25");
            repository.insert(excursion);
            excursion = new Excursion(0, "Sky Diving", 1, "02/03/25");
            repository.insert(excursion);
            return true;
        }

        if(item.getItemId()==android.R.id.home){
            this.finish();
            return true;
        }
        return true;
    }
    @Override
    protected void onResume(){

        super.onResume();
        List<Vacation> allVacations=repository.getxAllVacations();
        RecyclerView recyclerView=findViewById(R.id.recyclerview);
        final com.example.myapplication.UI.VacationAdapter vacationAdapter=new com.example.myapplication.UI.VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);
    }

    private String generateVacationCSV(List<Vacation> vacationList) {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Vacation ID,Title,Hotel,Start Date,End Date\n");

        for (Vacation v : vacationList) {
            csvBuilder.append(String.format("%d,%s,%s,%s,%s\n",
                    v.getVacationID(),
                    v.getVacationTitle().replace(",", " "),
                    v.getVacationHotel().replace(",", " "),
                    v.getStartDate(),
                    v.getEndDate()));
        }

        return csvBuilder.toString();
    }

    private File saveReportToCSVFile(String csvContent, String fileName) {
        File file = new File(getExternalFilesDir(null), fileName);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(csvContent);
            writer.flush();
            Toast.makeText(this, "CSV saved to: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save CSV", Toast.LENGTH_SHORT).show();
        }

        return file;
    }

}
