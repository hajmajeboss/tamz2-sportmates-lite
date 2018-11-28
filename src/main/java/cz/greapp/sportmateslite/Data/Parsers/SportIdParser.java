package cz.greapp.sportmateslite.Data.Parsers;

import cz.greapp.sportmateslite.Data.Models.Sport;

public class SportIdParser {

    public Sport getSportFromKey(String key) {
        if (key.equals("badminton")) return new Sport("Badminton");
        if (key.equals("gym")) return new Sport("Posilování");
        return new Sport("Neznámý sport");
    }

    public String getKeyFromSport(Sport sport) {
        if (sport.getName().equals("Badminton")) return "badminton";
        if (sport.getName().equals("Posilování")) return "gym";
        return null;
    }
}
