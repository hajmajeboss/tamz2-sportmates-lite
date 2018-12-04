package cz.greapp.sportmateslite.Data.Parsers;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import cz.greapp.sportmateslite.Data.Models.Game;
import cz.greapp.sportmateslite.Data.Models.Message;
import cz.greapp.sportmateslite.Data.Models.User;

public class MessageSnapshotParser {
    public List<Message> parseQuerySnapshot(QuerySnapshot snapshot) {

        List<Message> messages = new ArrayList<>();

        for (DocumentSnapshot snap : snapshot.getDocuments()) {
            Message message = parseDocumentSnapshot(snap);
            messages.add(message);
        }
        return messages;
    }

    public Message parseDocumentSnapshot (DocumentSnapshot snapshot) {
        Message message = new Message(
                snapshot.getId(),
                snapshot.getString("gameId"),
                snapshot.getString("senderId"),
                snapshot.getString("senderEmail"),
                snapshot.getString("senderName"),
                snapshot.getString("text"),
                snapshot.getString("dateSent"));

        return message;
    }
}
