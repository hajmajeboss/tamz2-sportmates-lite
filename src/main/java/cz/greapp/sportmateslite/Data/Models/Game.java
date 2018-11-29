package cz.greapp.sportmateslite.Data.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {
    private String id;
    private Sport sport;
    private String place;
    private String date;
    private String timeFrom;
    private String timeTo;
    private List<User> players;

    public Game() {

    }

    public Game(Sport sport, String place, String date, String timeFrom, String timeTo) {
        this.sport = sport;
        this.place = place;
        this.date = date;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.players = new ArrayList<User>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<User> getPlayers() {
        return players;
    }

    public void setPlayers(List<User> players) {
        this.players = players;
    }

    public void addPlayer(User player) {
        players.add(player);
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
