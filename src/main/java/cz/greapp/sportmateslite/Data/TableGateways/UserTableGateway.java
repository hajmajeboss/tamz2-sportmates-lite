package cz.greapp.sportmateslite.Data.TableGateways;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import cz.greapp.sportmateslite.Data.Models.User;
import cz.greapp.sportmateslite.Data.QueryResultObserver;

public class UserTableGateway extends TableGateway {

    public static final int REQUEST_USER_BY_EMAIL = 1;
    public static final int REQUEST_UPDATE_USER_BY_EMAIL = 2;
    public static final int REQUEST_REGISTER_USER = 3;

    public UserTableGateway() {
        super();
    }

    public void getUserByEmail(String email) {
        db.collection("users").whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QueryResultObserver.getInstance().firebaseQueryResult(RESULT_OK, REQUEST_USER_BY_EMAIL, task.getResult());
                }
                else {
                    QueryResultObserver.getInstance().firebaseQueryResult(RESULT_ERR, REQUEST_USER_BY_EMAIL, null);
                }
            }
        });
    }

    public void putUser(User u) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", u.getName());
        user.put("email", u.getEmail());

        db.collection("users").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (!task.isSuccessful()) {
                    QueryResultObserver.getInstance().firebaseQueryResult(RESULT_OK, REQUEST_REGISTER_USER, null);
                }
                else {
                    QueryResultObserver.getInstance().firebaseQueryResult(RESULT_ERR, REQUEST_REGISTER_USER, null);
                }
            }
        });
    }

    public void deleteUser() {

    }

    public void updateUser() {

    }


}
