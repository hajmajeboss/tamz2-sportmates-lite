package cz.greapp.sportmateslite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import cz.greapp.sportmateslite.Data.Models.User;

public class LoginActivity extends AppCompatActivity {

    FloatingActionButton loginButton;
    Button signUpButton;
    TextInputEditText emailField;
    TextInputEditText passwordField;
    CheckBox rememberCheckBox;
    Context ctx;

    SharedPreferences preferences;
    SharedPreferences.Editor prefEdit;

    public static final int REGISTER_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ctx = this;

        rememberCheckBox = (CheckBox) findViewById(R.id.rememberCheckBox);

        preferences = getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        prefEdit = preferences.edit();

        emailField = (TextInputEditText) findViewById(R.id.emailField);
        passwordField = (TextInputEditText) findViewById(R.id.passwordField);

        loginButton = (FloatingActionButton) findViewById(R.id.signInFab);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                String name = "Petr Heinz";

                if (!email.equals("") && !password.equals("")) {
                    if (rememberCheckBox.isChecked()) {
                        prefEdit.putBoolean("autoLogin", true);
                        prefEdit.putString("email", email);
                        prefEdit.putString("pass", password);
                        prefEdit.commit();
                    }
                    Intent intent = new Intent(ctx, MainActivity.class);
                    Bundle extras = new Bundle();
                    extras.putSerializable("user", new User(name, email,password));
                    intent.putExtras(extras);
                    finish();
                    startActivity(intent);
                }
                else {
                    Toast.makeText(ctx, "Vyplňte prosím přihlašovací údaje.", Toast.LENGTH_LONG).show();
                }

            }
        });

        signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, RegisterActivity.class);
                startActivityForResult(intent, LoginActivity.REGISTER_CODE);
            }
        });

        if (preferences.getBoolean("autoLogin", false) && !preferences.getString("email", "").equals("") && !preferences.getString("pass", "").equals("") ) {
            Intent intent = new Intent(ctx, MainActivity.class);
            Bundle extras = new Bundle();
            extras.putSerializable("user", new User("Petr Heinz", preferences.getString("email", ""), preferences.getString("pass", "")));
            intent.putExtras(extras);
            finish();
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LoginActivity.REGISTER_CODE) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(ctx, MainActivity.class);
                Bundle extras = getIntent().getExtras();
                User user = (User) extras.getSerializable("user");
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                finish();
                startActivity(intent);
            }
        }
    }
}
