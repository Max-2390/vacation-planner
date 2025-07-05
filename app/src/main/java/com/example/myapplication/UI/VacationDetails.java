package com.example.myapplication.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ParseException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.Repository;
import com.example.myapplication.entities.Excursion;
import com.example.myapplication.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class VacationDetails extends AppCompatActivity {
    String vacationTitle;
    String vacationHotel;
    int vacationID;
    String setStartDate;
    String setEndDate;

    EditText editTitle;
    EditText editHotel;
    TextView editStartDate;
    TextView editEndDate;
    Repository repository;
    Vacation currentVacation;
    int numExcursions;
    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;
    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();

    List<Excursion> filteredExcursions = new ArrayList<>();
    private com.example.myapplication.UI.ExcursionAdapter excursionAdapter;  // class-level adapter

    Random rand = new Random();
    int numAlert = rand.nextInt(99999);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);

        repository = new Repository(getApplication());

        editTitle = findViewById(R.id.titletext);
        editHotel = findViewById(R.id.hoteltext);
        vacationID = getIntent().getIntExtra("vacationID", -1);
        vacationTitle = getIntent().getStringExtra("vacationTitle");
        vacationHotel = getIntent().getStringExtra("vacationHotel");
        setStartDate = getIntent().getStringExtra("startDate");
        setEndDate = getIntent().getStringExtra("endDate");
        editTitle.setText(vacationTitle);
        editHotel.setText(vacationHotel);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTitle.getText().toString().trim();
                String hotel = editHotel.getText().toString().trim();

                if (title.isEmpty() || hotel.isEmpty()) {
                    Toast.makeText(VacationDetails.this, "Please enter a vacation title and hotel name first.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                String startDateString = sdf.format(myCalendarStart.getTime());
                String endDateString = sdf.format(myCalendarEnd.getTime());

                if (vacationID == -1) {
                    Vacation newVacation = new Vacation(0, title, hotel, startDateString, endDateString);
                    repository.insert(newVacation);
                    List<Vacation> allVacations = repository.getxAllVacations();
                    vacationID = allVacations.get(allVacations.size() - 1).getVacationID();
                } else {
                    Vacation updatedVacation = new Vacation(vacationID, title, hotel, startDateString, endDateString);
                    repository.update(updatedVacation);
                }

                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("vacationID", vacationID);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        excursionAdapter = new com.example.myapplication.UI.ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadExcursionsForVacation();

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (setStartDate != null) {
            try {
                Date startDate = sdf.parse(setStartDate);
                Date endDate = sdf.parse(setEndDate);
                myCalendarStart.setTime(startDate);
                myCalendarEnd.setTime(endDate);
            } catch (ParseException | java.text.ParseException e) {
                e.printStackTrace();
            }
        }

        editStartDate = findViewById(R.id.startDate);
        editEndDate = findViewById(R.id.endDate);

        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = editStartDate.getText().toString();
                if (info.equals("")) info = setStartDate;
                try {
                    myCalendarStart.setTime(new SimpleDateFormat(myFormat, Locale.US).parse(info));
                } catch (ParseException | java.text.ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, startDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, month);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editStartDate.setText(new SimpleDateFormat(myFormat, Locale.US).format(myCalendarStart.getTime()));
            }
        };

        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = editEndDate.getText().toString();
                if (info.equals("")) info = setEndDate;
                try {
                    myCalendarEnd.setTime(new SimpleDateFormat(myFormat, Locale.US).parse(info));
                } catch (ParseException | java.text.ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, endDate, myCalendarEnd
                        .get(Calendar.YEAR), myCalendarEnd.get(Calendar.MONTH),
                        myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarEnd.set(Calendar.YEAR, year);
                myCalendarEnd.set(Calendar.MONTH, month);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editEndDate.setText(new SimpleDateFormat(myFormat, Locale.US).format(myCalendarEnd.getTime()));
            }
        };
    }

    private void loadExcursionsForVacation() {
        filteredExcursions.clear();
        List<Excursion> allExcursions = repository.getxAllExcursions();
        for (Excursion e : allExcursions) {
            if (e.getVacationID() == vacationID) {
                filteredExcursions.add(e);
            }
        }
        excursionAdapter.setmExcursions(filteredExcursions);
        excursionAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExcursionsForVacation();

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editStartDate.setText(sdf.format(myCalendarStart.getTime()));
        editEndDate.setText(sdf.format(myCalendarEnd.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        if (item.getItemId() == R.id.vacationsave) {
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            String startDateString = sdf.format(myCalendarStart.getTime());
            String endDateString = sdf.format(myCalendarEnd.getTime());

            try {
                Date startDate = sdf.parse(startDateString);
                Date endDate = sdf.parse(endDateString);
                if (endDate.before(startDate)) {
                    Toast.makeText(this, "The end date should come after the start date.", Toast.LENGTH_LONG).show();
                } else {
                    Vacation vacation;
                    if (vacationID == -1) {
                        vacation = new Vacation(0, editTitle.getText().toString(), editHotel.getText().toString(), startDateString, endDateString);
                        repository.insert(vacation);
                        Toast.makeText(this, "Vacation saved successfully!", Toast.LENGTH_SHORT).show();
                        this.finish();
                    } else {
                        vacation = new Vacation(vacationID, editTitle.getText().toString(), editHotel.getText().toString(), startDateString, endDateString);
                        repository.update(vacation);
                        Toast.makeText(this, "Vacation updated successfully!", Toast.LENGTH_SHORT).show();
                        this.finish();
                    }
                }
            } catch (ParseException | java.text.ParseException e) {
                e.printStackTrace();
            }
            return true;
        }

        if (item.getItemId() == R.id.vacationdelete) {
            for (Vacation vac : repository.getxAllVacations()) {
                if (vac.getVacationID() == vacationID) currentVacation = vac;
            }
            numExcursions = 0;
            for (Excursion excursion : repository.getxAllExcursions()) {
                if (excursion.getVacationID() == vacationID) ++numExcursions;
            }
            if (numExcursions == 0) {
                repository.delete(currentVacation);
                Toast.makeText(VacationDetails.this, currentVacation.getVacationTitle() + " was deleted", Toast.LENGTH_LONG).show();
                VacationDetails.this.finish();
            } else {
                Toast.makeText(VacationDetails.this, "A vacation with excursions cannot be removed.", Toast.LENGTH_LONG).show();
            }
            return true;
        }

        if (item.getItemId() == R.id.alertstart) {
            String dateFromScreen = editStartDate.getText().toString();
            String alert = "Vacation " + vacationTitle + " is starting";
            alertPicker(dateFromScreen, alert);
            Toast.makeText(this, "Start date alert set for " + dateFromScreen, Toast.LENGTH_SHORT).show();
            return true;
        }

        if (item.getItemId() == R.id.alertend) {
            String dateFromScreen = editEndDate.getText().toString();
            String alert = "Vacation " + vacationTitle + " is ending";
            alertPicker(dateFromScreen, alert);
            Toast.makeText(this, "End date alert set for " + dateFromScreen, Toast.LENGTH_SHORT).show();
            return true;
        }

        if (item.getItemId() == R.id.alertfull) {
            String dateFromScreen = editStartDate.getText().toString();
            String alert = "Vacation " + vacationTitle + " is starting";
            alertPicker(dateFromScreen, alert);
            dateFromScreen = editEndDate.getText().toString();
            alert = "Vacation " + vacationTitle + " is ending";
            alertPicker(dateFromScreen, alert);
            Toast.makeText(this, "Alerts set for start and end dates.", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (item.getItemId() == R.id.share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "I'm excited to share my travel plans for the upcoming vacation!");

            StringBuilder message = new StringBuilder();
            message.append("Vacation title: ").append(editTitle.getText().toString()).append("\n");
            message.append("Hotel name: ").append(editHotel.getText().toString()).append("\n");
            message.append("Start Date: ").append(editStartDate.getText().toString()).append("\n");
            message.append("End Date: ").append(editEndDate.getText().toString()).append("\n");

            for (int i = 0; i < filteredExcursions.size(); i++) {
                Excursion excursion = filteredExcursions.get(i);
                message.append("Excursion ").append(i + 1).append(": ").append(excursion.getTitle()).append("\n");
                message.append("Excursion ").append(i + 1).append(" Date: ").append(excursion.getDate()).append("\n");
            }

            shareIntent.putExtra(Intent.EXTRA_TEXT, message.toString());
            startActivity(Intent.createChooser(shareIntent, "Share Vacation Info"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void alertPicker(String dateFromScreen, String alert) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date myDate = null;
        try {
            myDate = sdf.parse(dateFromScreen);
        } catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
        }
        Long trigger = myDate.getTime();
        Intent intent = new Intent(VacationDetails.this, com.example.myapplication.UI.MyReceiver.class);
        intent.putExtra("key", alert);
        PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
        numAlert = rand.nextInt(99999);
        System.out.println("numAlert Vacation = " + numAlert);
    }
}
