package cz.greapp.sportmateslite.Data.TableGateways;

import com.google.firebase.firestore.FirebaseFirestore;

public class TableGateway {

    public static final int RESULT_OK = 100;
    public static final int RESULT_ERR = 200;

    protected FirebaseFirestore db;

    public TableGateway() {
        db = FirebaseFirestore.getInstance();
    }
}
