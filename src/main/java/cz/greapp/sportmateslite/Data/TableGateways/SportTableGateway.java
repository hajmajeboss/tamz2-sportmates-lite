package cz.greapp.sportmateslite.Data.TableGateways;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import cz.greapp.sportmateslite.Data.OnFirebaseQueryResultListener;
import cz.greapp.sportmateslite.Data.QueryResultObserver;

public class SportTableGateway extends TableGateway {

    public SportTableGateway() {
        super();
    }

    public void getSports(final OnFirebaseQueryResultListener listener, final int requestCode) {
        QueryResultObserver.getInstance().attachListener(listener);
        db.collection("sports").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
}
