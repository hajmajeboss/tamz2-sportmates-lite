package cz.greapp.sportmateslite;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.admin.v1beta1.Progress;

import cz.greapp.sportmateslite.Data.Models.User;
import cz.greapp.sportmateslite.Data.OnFirebaseQueryResultListener;
import cz.greapp.sportmateslite.Data.TableGateways.TableGateway;
import cz.greapp.sportmateslite.Data.TableGateways.UserTableGateway;

public class UserProfileActivity extends AppCompatActivity implements OnFirebaseQueryResultListener {

    Toolbar toolbar;
    Context ctx;
    Activity activity;

    TextView userNameText;
    TextView userEmailText;
    ImageView userAvatar;

    Button sendEmailButton;

    ProgressBar progressBar;

    User user;

    public static final int REQUEST_PLAYER_AVATAR = 4096;
    public static final int REQUEST_PLAYER_BY_EMAIL = 4092;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        activity = this;
        ctx = this;

        toolbar = findViewById(R.id.playerProfileToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userNameText = findViewById(R.id.playerUsername);
        userEmailText = findViewById(R.id.playerEmail);

        userAvatar = findViewById(R.id.playerAvatar);

        progressBar = findViewById(R.id.progressBar);

        sendEmailButton = findViewById(R.id.sendEmailToPlayerButton);

        user = (User) getIntent().getExtras().getSerializable("user");
        if (user != null) {
            userNameText.setText(user.getName());
            userEmailText.setText(user.getEmail());

            sendEmailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShareCompat.IntentBuilder.from(activity)
                                .setType("message/rfc822")
                                .addEmailTo(user.getEmail())
                                .setSubject("Zpráva z aplikace Sportmates")
                                .setChooserTitle("Vyberte emailového klienta:")
                                .startChooser();

                }
            });
        }

        UserTableGateway gw = new UserTableGateway();
        gw.getUserByEmail(this, user.getEmail(), REQUEST_PLAYER_BY_EMAIL);

    }

    @Override
    public void onFirebaseQueryResult(int resultCode, int requestCode, QuerySnapshot result) {
        if (requestCode == REQUEST_PLAYER_BY_EMAIL) {
            if (resultCode == TableGateway.RESULT_OK) {
                UserTableGateway gw = new UserTableGateway();
                user.setId(result.getDocuments().get(0).getId());
                gw.loadUserImage(user, userAvatar, progressBar);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
