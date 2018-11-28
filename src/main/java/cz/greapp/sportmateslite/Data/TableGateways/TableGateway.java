package cz.greapp.sportmateslite.Data.TableGateways;

import com.google.firebase.firestore.FirebaseFirestore;

public class TableGateway {

    protected FirebaseFirestore db;

    public TableGateway() {
        db = FirebaseFirestore.getInstance();
    }
}
