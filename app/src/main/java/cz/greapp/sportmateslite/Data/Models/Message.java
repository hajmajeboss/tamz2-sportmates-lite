package cz.greapp.sportmateslite.Data.Models;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Comparable<Message> {
    private String id;
    private String gameId;
    private String senderId;
    private String text;
    private String dateTime;
    private String senderEmail;
    private String senderName;

    private Game game;
    private User sender;

    public Message() {
    }

    public Message(String id, String gameId, String senderId, String senderEmail, String senderName, String text, String dateTime) {
        this.id = id;
        this.gameId = gameId;
        this.senderId = senderId;
        this.senderEmail = senderEmail;
        this.senderName = senderName;
        this.text = text;
        this.dateTime = dateTime;
    }

    public Message(String message, User user, String dateTime) {
        this.text = message;
        this.senderId = user.getId();
        this.senderEmail = user.getEmail();
        this.senderName = user.getName();
        this.dateTime = dateTime;
    }

    public Message(String message, User user, String gameId, String dateTime) {
        this.text = message;
        this.senderId = user.getId();
        this.senderEmail = user.getEmail();
        this.senderName = user.getName();
        this.dateTime = dateTime;
        this.gameId = gameId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getGameId() {
        return gameId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getText() {
        return text;
    }

    public User getSender() {
        return sender;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setGame(Game game) {
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @Override
    public int compareTo(@NonNull Message m) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        try {
            Date d = sdf.parse(this.dateTime);
            Date d1 = sdf.parse(m.getDateTime());

            return d1.compareTo(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}

