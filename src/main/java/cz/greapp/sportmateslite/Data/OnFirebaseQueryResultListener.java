package cz.greapp.sportmateslite.Data;

import com.google.firebase.firestore.QuerySnapshot;

public interface OnFirebaseQueryResultListener {
    void onFirebaseQueryResult(final int resultCode, final int requestCode, QuerySnapshot result);
}
