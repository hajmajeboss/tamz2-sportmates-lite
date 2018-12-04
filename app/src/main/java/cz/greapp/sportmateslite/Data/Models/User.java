package cz.greapp.sportmateslite.Data.Models;

import java.io.File;
import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String name;
    private String email;
    private File profileImage;


    public User() { }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public File getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(File profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }


}
