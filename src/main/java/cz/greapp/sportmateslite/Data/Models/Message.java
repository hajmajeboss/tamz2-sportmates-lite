package cz.greapp.sportmateslite.Data.Models;

public class Message {
    private String id;
    private String gameId;
    private String senderId;
    private String text;
    private String dateTime;

    private Game game;
    private User sender;

    public Message() {}

    public Message(String id, String gameId, String senderId, String text, String dateTime) {
        this.id = id;
        this.gameId = gameId;
        this.senderId = senderId;
        this.text = text;
        this.dateTime = dateTime;
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
}
