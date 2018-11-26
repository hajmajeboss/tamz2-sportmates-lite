package cz.greapp.sportmateslite;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    Button signUpButton;
    TextInputEditText emailField;
    TextInputEditText passwordField;
    CheckBox rememberCheckBox;
    Context ctx;

    public static final int REGISTER_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ctx = this;

        emailField = (TextInputEditText) findViewById(R.id.emailField);
        passwordField = (TextInputEditText) findViewById(R.id.passwordField);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                String name = "Petr Heinz";

                if (!email.equals("") && !password.equals("")) {
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LoginActivity.REGISTER_CODE) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(ctx, MainActivity.class);
                finish();
                startActivity(intent);
            }
        }
    }
}
