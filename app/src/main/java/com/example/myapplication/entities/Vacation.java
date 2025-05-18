package com.example.myapplication.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vacations")
public class Vacation {

    @PrimaryKey(autoGenerate = true)
    public int vacationID;
    private String vacationTitle;
    private String vacationHotel;
    private String startDate;
    private String endDate;

    public Vacation() {}


    public Vacation(int vacationId, String vacationTitle, String vacationHotel, String startDate, String endDate) {
        this.vacationID = vacationId;
        this.vacationTitle = vacationTitle;
        this.vacationHotel = vacationHotel;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setVacationTitle(String vacationTitle) {
        this.vacationTitle = vacationTitle;
    }

    public String getVacationTitle() {
        return vacationTitle;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationId(int vacationId) {
        this.vacationID = vacationId;
    }


    public String getVacationHotel() {
        return vacationHotel;
    }

    public void setVacationHotel(String vacationHotel) {
        this.vacationHotel = vacationHotel;
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
