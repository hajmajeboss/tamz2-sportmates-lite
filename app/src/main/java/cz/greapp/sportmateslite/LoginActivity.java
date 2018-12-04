package cz.greapp.sportmateslite;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cz.greapp.sportmateslite.Data.Models.User;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    FloatingActionButton loginButton;
    Button signUpButton;
    TextInputEditText emailField;
    TextInputEditText passwordField;
    CheckBox rememberCheckBox;
    Context ctx;

    SharedPreferences preferences;
    SharedPreferences.Editor prefEdit;

    ProgressDialog progressDialog;

    public static final int REGISTER_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth =  FirebaseAuth.getInstance();
        ctx = this;

        emailField = (TextInputEditText) findViewById(R.id.emailField);
        passwordField = (TextInputEditText) findViewById(R.id.passwordField);

        // Login
        loginButton = (FloatingActionButton) findViewById(R.id.signInFab);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                if (!email.equals("") && !password.equals("")) {
                    progressDialog = ProgressDialog.show(ctx, "Přihlásit se", "Může to trvat několik vteřin...");
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                FirebaseUser user = auth.getCurrentUser();
                                Intent intent = new Intent(ctx, MainActivity.class);
                                Bundle extras = new Bundle();
                                extras.putSerializable("user", new User("", user.getEmail()));
                                intent.putExtras(extras);
                                finish();
                                startActivity(intent);
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Přihlášení selhalo. " +
                                        "Zkontrolujte, zda máte vyplněno správné uživatelské jméno a heslo.",
                                        Toast.LENGTH_SHORT).show();
                            }
                       }
                   });

                }
                else {
                    Toast.makeText(ctx, "Vyplňte prosím přihlašovací údaje.", Toast.LENGTH_LONG).show();
                }

            }
        });

        // Sign-up
        signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, RegisterActivity.class);
                startActivityForResult(intent, LoginActivity.REGISTER_CODE);
            }
        });


        // Auto sign-in
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(ctx, MainActivity.class);
            Bundle extras = new Bundle();
            extras.putSerializable("user", new User("", currentUser.getEmail()));
            intent.putExtras(extras);
            finish();
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LoginActivity.REGISTER_CODE) {
            if (resultCode == RESULT_OK) {
                FirebaseUser user = auth.getCurrentUser();
                Intent intent = new Intent(ctx, MainActivity.class);
                Bundle extras = new Bundle();
                extras.putSerializable("user", new User("", user.getEmail()));
                intent.putExtras(extras);
                finish();
                startActivity(intent);
            }
        }
    }
}
