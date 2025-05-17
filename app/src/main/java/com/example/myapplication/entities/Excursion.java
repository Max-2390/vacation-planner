package com.example.myapplication.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "excursions")
public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int exID;
    private String exTitle;
    private int vacID;
    private String date;

    public Excursion(int excursionID, String excursionTitle, int vacationID, String excursionDate) {
        this.exID = excursionID;
        this.exTitle = excursionTitle;
        this.vacID = vacationID;
        this.date = excursionDate;
    }

    public int getExcursionID() {
        return exID;
    }

    public void setExcursionID(int excursionID) {
        this.exID = excursionID;
    }

    public String getExcursionTitle() {
        return exTitle;
    }

    public void setExcursionTitle(String excursionTitle) {
        this.exTitle = excursionTitle;
    }

    public int getVacationID() {
        return vacID;
    }

    public void setVacationID(int vacationID) {
        this.vacID = vacationID;
    }

    public String getExcursionDate() {
        return date;
    }

    public void setExcursionDate(String excursionDate) {
        this.date = excursionDate;
    }
}


