package com.example.myapplication.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.Repository;
import com.example.myapplication.entities.Excursion;
import com.example.myapplication.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;

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
}
