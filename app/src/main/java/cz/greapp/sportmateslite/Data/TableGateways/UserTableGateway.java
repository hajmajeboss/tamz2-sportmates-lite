package cz.greapp.sportmateslite.Data.TableGateways;

import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import cz.greapp.sportmateslite.Data.Models.User;
import cz.greapp.sportmateslite.Data.OnFirebaseQueryResultListener;
import cz.greapp.sportmateslite.Data.QueryResultObserver;

public class UserTableGateway extends TableGateway {

    public UserTableGateway() {
        super();
    }

    public void getUserByEmail(OnFirebaseQueryResultListener listener, String email, final int requestCode) {
        QueryResultObserver.getInstance().attachListener(listener);
        db.collection("users").whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QueryResultObserver.getInstance().firebaseQueryResult(RESULT_OK, requestCode, task.getResult());
                }
                else {
                    QueryResultObserver.getInstance().firebaseQueryResult(RESULT_ERR, requestCode, null);
                }
            }
        });
    }

    /*
    public void getUserById(OnFirebaseQueryResultListener listener, String id, final int requestCode) {
        QueryResultObserver.getInstance().attachListener(listener);
        db.collection("users").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    QueryResultObserver.getInstance().firebaseQueryResult(RESULT_OK, requestCode, task.getResult());
                }
                else {
                    QueryResultObserver.getInstance().firebaseQueryResult(RESULT_ERR, requestCode, null);
                }
            }
        });
    }*/

    public void putUser(OnFirebaseQueryResultListener listener, User u, final int requestCode) {
        QueryResultObserver.getInstance().attachListener(listener);
        Map<String, Object> user = new HashMap<>();
        user.put("name", u.getName());
        user.put("email", u.getEmail());

        db.collection("users").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
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

    public void loadUserImage(User user, final ImageView imageView, final ProgressBar progressBar)
    {
        FirebaseStorage storageRef = FirebaseStorage.getInstance();

        StorageReference gsReference = storageRef.getReferenceFromUrl("gs://sportmateslite.appspot.com/" + user.getId() +  ".jpg");

        imageView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        try {
            final File localFile = File.createTempFile("images", "jpg");
            gsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    imageView.setImageBitmap(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
                    imageView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    imageView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
        catch (IOException e) {

        }

    }

}
