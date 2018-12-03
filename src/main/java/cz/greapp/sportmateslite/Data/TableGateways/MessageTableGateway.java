package cz.greapp.sportmateslite.Data.TableGateways;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.greapp.sportmateslite.Data.Models.Game;
import cz.greapp.sportmateslite.Data.Models.Message;
import cz.greapp.sportmateslite.Data.Models.User;
import cz.greapp.sportmateslite.Data.OnFirebaseQueryResultListener;
import cz.greapp.sportmateslite.Data.Parsers.SportIdParser;
import cz.greapp.sportmateslite.Data.QueryResultObserver;

public class MessageTableGateway extends TableGateway {

    public void getGameMesasges(final OnFirebaseQueryResultListener listener, final int requestCode, Game g) {
        if (listener != null) {
            QueryResultObserver.getInstance().attachListener(listener);
        }

        db.collection("messages").whereEqualTo("gameId", g.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QueryResultObserver.getInstance().firebaseQueryResult(RESULT_OK, requestCode, task.getResult());
                }
                else {
                    QueryResultObserver.getInstance().firebaseQueryResult(RESULT_ERR, requestCode, task.getResult());
                }
            }
        });
    }

    public void putMessage(final OnFirebaseQueryResultListener listener, final int requestCode, Game g, Message m) {
        if (listener != null) {
            QueryResultObserver.getInstance().attachListener(listener);
        }
        Map<String, Object> message = new HashMap<>();

        message.put("gameId", g.getId());
        message.put("senderId", m.getSenderId());
        message.put("senderEmail", m.getSenderEmail());
        message.put("senderName", m.getSenderName());
        String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        message.put("dateSent", timeStamp);
        message.put("text", m.getText());

        db.collection("messages").add(message).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    QueryResultObserver.getInstance().firebaseQueryResult(RESULT_OK, requestCode, null);
                }
                else {
                    QueryResultObserver.getInstance().firebaseQueryResult(RESULT_ERR, requestCode, null);
                }
            }
        });
    }

}
