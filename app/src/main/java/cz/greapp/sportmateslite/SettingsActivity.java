package cz.greapp.sportmateslite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    Toolbar toolbar;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;

    CheckBox autoLoginCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        prefEditor = preferences.edit();

        toolbar = (Toolbar) findViewById(R.id.settingsToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        autoLoginCheckBox = (CheckBox) findViewById(R.id.autoLoginCheckBox);
        autoLoginCheckBox.setChecked(preferences.getBoolean("autoLogin", false));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.settings_navigation_save) {
            prefEditor.putBoolean("autoLogin", autoLoginCheckBox.isChecked());
            prefEditor.apply();
            prefEditor.commit();
            Toast.makeText(this, "Uloženo!", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
