package com.example.myapplication.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vacations")
public class Vacation {


    @PrimaryKey(autoGenerate = true)
    public int vacID;
    private String vacTitle;
    private String vacHotel;
    private String startDate;
    private String endDate;

    public Vacation(int vacationId, String vacationTitle, String vacationHotel, String startDate, String endDate) {
        this.vacID = vacationId;
        this.vacTitle = vacationTitle;
        this.vacHotel = vacationHotel;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String toString() {
        return vacTitle;
    }

    public int getVacationId() {
        return vacID;
    }

    public void setVacationId(int vacationId) {
        this.vacID = vacationId;
    }

    public String getVacationTitle() {
        return vacTitle;
    }

    public void setVacationTitle(String vacationTitle) {
        this.vacTitle = vacationTitle;
    }

    public String getVacationHotel() {
        return vacHotel;
    }

    public void setVacationHotel(String vacationHotel) {
        this.vacHotel = vacationHotel;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}

