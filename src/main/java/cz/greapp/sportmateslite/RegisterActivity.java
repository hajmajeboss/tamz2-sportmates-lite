package cz.greapp.sportmateslite;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import cz.greapp.sportmateslite.Data.Models.User;

public class RegisterActivity extends AppCompatActivity {

    FloatingActionButton registerButton;
    TextInputEditText nameField;
    TextInputEditText emailField;
    TextInputEditText passwordField;
    CheckBox gdprBox;
    CheckBox ageBox;
    Context ctx;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ctx = this;

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
                    Toast.makeText(ctx, "Pro registraci musíte být starší 15ti let.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!gdprBox.isChecked()) {
                    Toast.makeText(ctx, "Pro registraci musíte souhlasit se zpracováním os. údajů.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent ret = new Intent();
                Bundle bundle = new Bundle();

                User newUser = new User(nameField.getText().toString(), emailField.getText().toString(), passwordField.getText().toString());
                if (newUser == null) {
                    Toast.makeText(ctx, "namefield null", Toast.LENGTH_SHORT).show();
                    return;
                }
                bundle.putSerializable("user", newUser );
                ret.putExtras(bundle);
                setResult(RESULT_OK, ret);
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
