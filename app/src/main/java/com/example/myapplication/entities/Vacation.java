package com.example.myapplication.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vacations")
public class Vacation implements Parcelable {

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

    protected Vacation(Parcel in) {
        vacationID = in.readInt();
        vacationTitle = in.readString();
        vacationHotel = in.readString();
        startDate = in.readString();
        endDate = in.readString();
    }

    public static final Creator<Vacation> CREATOR = new Creator<Vacation>() {
        @Override
        public Vacation createFromParcel(Parcel in) {
            return new Vacation(in);
        }

        @Override
        public Vacation[] newArray(int size) {
            return new Vacation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(vacationID);
        parcel.writeString(vacationTitle);
        parcel.writeString(vacationHotel);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
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
