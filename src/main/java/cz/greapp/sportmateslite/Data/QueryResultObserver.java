package cz.greapp.sportmateslite.Data;

import android.app.Activity;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import cz.greapp.sportmateslite.FindGameFragment;

public class QueryResultObserver {

    private static QueryResultObserver instance = new QueryResultObserver();
    private List<OnFirebaseQueryResultListener> observables;

    private QueryResultObserver() {
        observables = new ArrayList<>();
    }

    public static QueryResultObserver getInstance() {
        return QueryResultObserver.instance;
    }

    public void firebaseQueryResult(int resultCode, int requestCode, QuerySnapshot result) {
        for (OnFirebaseQueryResultListener observable : observables) {
            observable.onFirebaseQueryResult(resultCode, requestCode, result);
        }
    }

    public void attachListener(OnFirebaseQueryResultListener listener) {
        if (!observables.contains(listener)) {
            observables.add(listener);
        }
    }

}
