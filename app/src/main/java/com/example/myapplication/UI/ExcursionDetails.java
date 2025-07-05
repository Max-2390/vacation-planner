package com.example.myapplication.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.database.Repository;
import com.example.myapplication.entities.Excursion;
import com.example.myapplication.entities.Vacation;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ExcursionDetails extends AppCompatActivity {

    String title;
    int id;
    int vacationID;
    EditText editTitle;
    Repository repository;
    Excursion currentExcursion;
    TextView editExcursionDate;
    DatePickerDialog.OnDateSetListener excursionDate;
    final Calendar myCalendarDate = Calendar.getInstance();
    String setDate;
    Random rand = new Random();
    int numAlert = rand.nextInt(99999);
    String myFormat = "MM/dd/yy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);

        repository = new Repository(getApplication());
        title = getIntent().getStringExtra("excursionTitle");
        editTitle = findViewById(R.id.excursionTitle);
        editTitle.setText(title);
        id = getIntent().getIntExtra("excursionID", -1);
        vacationID = getIntent().getIntExtra("vacationID", -1);
        setDate = getIntent().getStringExtra("excursionDate");

        if (setDate != null) {
            try {
                Date excursionDate = sdf.parse(setDate);
                myCalendarDate.setTime(excursionDate);
            } catch (ParseException e) {
                Log.e("ExcursionDetails", "Failed to parse excursion date in onCreate", e);
            }
        }

        editExcursionDate = findViewById(R.id.excursionDate);
        editExcursionDate.setText(sdf.format(myCalendarDate.getTime()));

        editExcursionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = editExcursionDate.getText().toString();
                if (info.equals("")) info = setDate;
                try {
                    myCalendarDate.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    Log.e("ExcursionDetails", "Failed to parse excursion date on click", e);
                }
                new DatePickerDialog(ExcursionDetails.this, excursionDate, myCalendarDate
                        .get(Calendar.YEAR), myCalendarDate.get(Calendar.MONTH),
                        myCalendarDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        excursionDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarDate.set(Calendar.YEAR, year);
                myCalendarDate.set(Calendar.MONTH, month);
                myCalendarDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editExcursionDate.setText(sdf.format(myCalendarDate.getTime()));
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        if (item.getItemId() == R.id.excursionsave) {
            String excursionDateString = sdf.format(myCalendarDate.getTime());
            Vacation vacation = null;
            List<Vacation> vacations = repository.getxAllVacations();
            for (Vacation vac : vacations) {
                if (vac.getVacationID() == vacationID) {
                    vacation = vac;
                    break;
                }
            }

            if (vacation != null) {
                try {
                    Date excursionDate = sdf.parse(excursionDateString);
                    Date startDate = sdf.parse(vacation.getStartDate());
                    Date endDate = sdf.parse(vacation.getEndDate());

                    if (excursionDate.before(startDate) || excursionDate.after(endDate)) {
                        Toast.makeText(this, "Excursion Date must be within the Vacation's Start and End dates", Toast.LENGTH_LONG).show();
                        return true;
                    }

                    Excursion excursion;
                    if (id == -1) {
                        excursion = new Excursion(0, editTitle.getText().toString(), vacationID, excursionDateString);
                        repository.insert(excursion);
                        Toast.makeText(this, "Excursion saved successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        excursion = new Excursion(id, editTitle.getText().toString(), vacationID, excursionDateString);
                        repository.update(excursion);
                        Toast.makeText(this, "Excursion updated successfully!", Toast.LENGTH_SHORT).show();
                    }
                    this.finish();

                } catch (ParseException e) {
                    Log.e("ExcursionDetails", "Failed to parse excursion date in save", e);
                }
            }
            return true;
        }

        if (item.getItemId() == R.id.excursiondelete) {
            for (Excursion excursion : repository.getxAllExcursions()) {
                if (excursion.getId() == id) currentExcursion = excursion;
            }
            if (currentExcursion != null) {
                repository.delete(currentExcursion);
                Log.i("ExcursionDetails", "Deleted excursion: " + currentExcursion.getTitle());
                Toast.makeText(this, currentExcursion.getTitle() + " was deleted", Toast.LENGTH_LONG).show();
            }
            this.finish();
            return true;
        }

        if (item.getItemId() == R.id.excursionalert) {
            String dateFromScreen = editExcursionDate.getText().toString();
            String alert = "Excursion " + editTitle.getText().toString() + " is today";

            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                Log.e("ExcursionDetails", "Failed to parse date for alert", e);
            }

            if (myDate != null) {
                Long trigger = myDate.getTime();
                Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
                intent.putExtra("key", alert);
                PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                numAlert = rand.nextInt(99999);
                Log.i("ExcursionDetails", "Alarm set for excursion alert: " + alert);
                Toast.makeText(this, "Excursion alert set for " + dateFromScreen, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to set alert: invalid date", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        editExcursionDate.setText(sdf.format(myCalendarDate.getTime()));
    }
}
