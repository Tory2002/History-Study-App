package com.example.data;

public class StruktDate {
    private int id;
    private String date;
    private String event;

    public StruktDate(int id, String date, String event){
        this.id = id;
        this.date = date;
        this.event = event;
    }

    public int getId() {
        return id;
    }
    public String getDate() {
        return date;
    }
    public String getEvent() {
        return event;
    }
}
