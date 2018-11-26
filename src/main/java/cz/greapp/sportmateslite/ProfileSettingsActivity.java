package cz.greapp.sportmateslite;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class ProfileSettingsActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextInputEditText nameField;
    Button takePhotoButton;
    Button selectPhotoButton;
    Button changePasswordButton;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        toolbar = (Toolbar) findViewById(R.id.profileSettingsToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        Bundle extras = getIntent().getExtras();
        user = (User) extras.getSerializable("user");

        nameField = (TextInputEditText) findViewById(R.id.editNameField);
        nameField.setText(user.getName());


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_settings_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.profile_settings_navigation_save) {
            user.setName(nameField.getText().toString());
            Intent ret = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", user);
            ret.putExtras(bundle);
            setResult(RESULT_OK, ret);
            Toast.makeText(this, "Uloženo!", Toast.LENGTH_SHORT).show();
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

