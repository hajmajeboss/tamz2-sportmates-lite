package cz.greapp.sportmateslite;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QuerySnapshot;

import cz.greapp.sportmateslite.Data.Models.User;
import cz.greapp.sportmateslite.Data.OnFirebaseQueryResultListener;
import cz.greapp.sportmateslite.Data.QueryResultObserver;
import cz.greapp.sportmateslite.Data.TableGateways.TableGateway;
import cz.greapp.sportmateslite.Data.TableGateways.UserTableGateway;

public class RegisterActivity extends AppCompatActivity implements OnFirebaseQueryResultListener {

    public static final int REQUEST_REGISTER_USER = 4;

    FloatingActionButton registerButton;
    TextInputEditText nameField;
    TextInputEditText emailField;
    TextInputEditText passwordField;
    CheckBox gdprBox;
    CheckBox ageBox;
    Context ctx;

    Toolbar toolbar;

    ProgressDialog progressDialog;

    private FirebaseAuth auth;

    OnFirebaseQueryResultListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ctx = this;
        listener = this;
        auth = FirebaseAuth.getInstance();

        toolbar = (Toolbar) findViewById(R.id.registerToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        nameField = (TextInputEditText) findViewById(R.id.registerNameField);
        emailField = (TextInputEditText) findViewById(R.id.registerEmailField);
        passwordField = (TextInputEditText) findViewById(R.id.registerPasswordField);

        gdprBox = (CheckBox) findViewById(R.id.gdprCheckBox);
        ageBox = (CheckBox) findViewById(R.id.ageCheckBox);

        registerButton = (FloatingActionButton) findViewById(R.id.registerSignUpFab);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ageBox.isChecked()) {
                    Toast.makeText(ctx, "Pro registraci musíte být starší 15ti let.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!gdprBox.isChecked()) {
                    Toast.makeText(ctx, "Pro registraci musíte souhlasit se zpracováním os. údajů.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (passwordField.getText().toString().length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Heslo musí být alespoň 6 znaků dlouhé.", Toast.LENGTH_LONG).show();
                    return;
                }

                UserTableGateway gw = new UserTableGateway();
                gw.putUser(listener, new User(nameField.getText().toString(), emailField.getText().toString()), REQUEST_REGISTER_USER);

                progressDialog = ProgressDialog.show(ctx, "Registrace", "Může to trvat několik vteřin...");

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onFirebaseQueryResult(int resultCode, int requestCode, QuerySnapshot result) {
        if (requestCode == REQUEST_REGISTER_USER) {
            if (resultCode == TableGateway.RESULT_OK) {
                auth.createUserWithEmailAndPassword(emailField.getText().toString(), passwordField.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)   {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Registrace byla úspěšná.", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        }

                        else {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Registrace se nezdařila.", Toast.LENGTH_LONG).show();
                            Log.w("EXC", task.getException().getMessage());
                        }
                    }
                });
            }
            else {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Registrace se nezdařila.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
