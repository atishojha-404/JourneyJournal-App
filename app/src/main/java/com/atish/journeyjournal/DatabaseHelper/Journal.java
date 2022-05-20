package com.atish.journeyjournal.DatabaseHelper;

import android.os.Parcel;
import android.os.Parcelable;

public class Journal implements Parcelable {
    private String title;
    private String image;
    private String location;
    private String aboutj;
    private String datetime;
    private  String latlon;

    public Journal(){

    }

    public Journal(String title, String image, String location, String aboutj, String datetime, String latlon) {
        this.title = title;
        this.image = image;
        this.location = location;
        this.aboutj = aboutj;
        this.datetime = datetime;
        this.latlon = latlon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAboutj() {
        return aboutj;
    }

    public void setAboutj(String aboutj) {
        this.aboutj = aboutj;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
    public String getLatlon() {
        return latlon;
    }

    public void setLatlon(String latlon) {
        this.latlon = latlon;
    }


    protected Journal(Parcel in) {
        title = in.readString();
        image = in.readString();
        location = in.readString();
        aboutj = in.readString();
        datetime = in.readString();
        latlon = in.readString();
    }

    public static final Creator<Journal> CREATOR = new Creator<Journal>() {
        @Override
        public Journal createFromParcel(Parcel in) {
            return new Journal(in);
        }

        @Override
        public Journal[] newArray(int size) {
            return new Journal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(image);
        parcel.writeString(location);
        parcel.writeString(aboutj);
        parcel.writeString(datetime);
        parcel.writeString(latlon);
    }
}
