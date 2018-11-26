package cz.greapp.sportmateslite;

import java.io.Serializable;

public class Game implements Serializable {
    private Sport sport;
    private String place;
    private String date;
    private String timeFrom;
    private String timeTo;

    public Game(Sport sport, String place, String date, String timeFrom, String timeTo) {
        this.sport = sport;
        this.place = place;
        this.date = date;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }

    public Sport getSport() {
        return sport;
    }

    public String getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

}
