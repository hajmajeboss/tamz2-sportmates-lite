package cz.greapp.sportmateslite.Data.Models;

import java.io.Serializable;

public class Sport implements Serializable {
    private String name;

    public Sport(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
