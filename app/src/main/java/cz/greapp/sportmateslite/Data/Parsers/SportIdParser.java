package cz.greapp.sportmateslite.Data.Parsers;

import cz.greapp.sportmateslite.Data.Models.Sport;

public class SportIdParser {

    public Sport getSportFromKey(String key) {
        if (key.equals("badminton")) return new Sport("Badminton");
        if (key.equals("gym")) return new Sport("Posilování");
        if (key.equals("squash")) return new Sport("Squash");
        if (key.equals("tennis")) return new Sport("Tenis");
        if (key.equals("hiking")) return new Sport("Horolezectví");
        if (key.equals("running")) return new Sport("Běh");
        if (key.equals("cycling")) return new Sport("Cyklistika");
        return new Sport("Neznámý sport");
    }

    public String getKeyFromSport(Sport sport) {
        if (sport.getName().equals("Badminton")) return "badminton";
        if (sport.getName().equals("Posilování")) return "gym";
        if (sport.getName().equals("Squash")) return "squash";
        if (sport.getName().equals("Tenis")) return "tennis";
        if (sport.getName().equals("Horolezectví")) return "hiking";
        if (sport.getName().equals("Běh")) return "running";
        if (sport.getName().equals("Cyklistika")) return "cycling";
        return null;
    }
}
