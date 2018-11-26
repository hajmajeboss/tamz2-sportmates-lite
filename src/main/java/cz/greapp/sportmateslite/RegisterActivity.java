package cz.greapp.sportmateslite;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class RegisterActivity extends AppCompatActivity {

    Button registerButton;
    TextInputEditText nameField;
    TextInputEditText emailField;
    TextInputEditText passwordField;
    CheckBox gdprBox;
    CheckBox ageBox;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ctx = this;

        nameField = (TextInputEditText) findViewById(R.id.nameField);
        emailField = (TextInputEditText) findViewById(R.id.emailField);
        passwordField = (TextInputEditText) findViewById(R.id.passwordField);

        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ret = new Intent();
                Bundle bundle = new Bundle();
                User newUser = new User(nameField.getText().toString(), emailField.getText().toString(), passwordField.getText().toString());
                bundle.putSerializable("user", newUser );
                ret.putExtras(bundle);
                setResult(RESULT_OK, ret);
                finish();
            }
        });
    }
}
