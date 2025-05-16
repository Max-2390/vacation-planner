package com.example.myapplication.database;

import android.app.Application;

import com.example.myapplication.dao.ExcursionDAO;
import com.example.myapplication.dao.VacationDAO;
import com.example.myapplication.entities.Excursion;
import com.example.myapplication.entities.Vacation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private ExcursionDAO xExcursionDAO;
    private VacationDAO xVacationDAO;

    private List<Vacation> xAllVacations;
    private List<Excursion> xAllExcursions;

    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        VacationDatabaseBuilder db = VacationDatabaseBuilder.getDatabase(application);
        xExcursionDAO = db.excursionDAO();
        xVacationDAO = db.vacationDAO();
    }

    public List<Vacation> getxAllVacations() {
        databaseExecutor.execute(() -> {
            xAllVacations = xVacationDAO.getAllVacations();
        });