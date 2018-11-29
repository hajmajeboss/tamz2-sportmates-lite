package cz.greapp.sportmateslite;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import cz.greapp.sportmateslite.Data.Models.User;

public class ProfileSettingsActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button takePhotoButton;
    Button selectPhotoButton;
    Button changePasswordButton;

    String passwordText;

    User user;

    Context ctx;

    public static final int RESULT_LOAD_IMAGE = 100;
    public static final int REQUEST_CAMERA_PERMISSION = 0;
    public static final int RESULT_TAKE_PHOTO = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        ctx = this;
        toolbar = (Toolbar) findViewById(R.id.profileSettingsToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        Bundle extras = getIntent().getExtras();
        user = (User) extras.getSerializable("user");

        selectPhotoButton = (Button) findViewById(R.id.selectProfilePictureButton);
        selectPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, ProfileSettingsActivity.RESULT_LOAD_IMAGE );
            }
        });

        takePhotoButton = (Button) findViewById(R.id.takeProfilePictureButton);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, ProfileSettingsActivity.REQUEST_CAMERA_PERMISSION);
                    } else {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, ProfileSettingsActivity.RESULT_TAKE_PHOTO);
                    }
                }
            }
        });

        changePasswordButton = (Button) findViewById(R.id.changePasswordButton);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Nové heslo");

                final EditText input = new EditText(ctx);

                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        passwordText = input.getText().toString();
                    }
                });
                builder.setNegativeButton("Zrušit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ProfileSettingsActivity.REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, ProfileSettingsActivity.RESULT_TAKE_PHOTO);
            } else {
                Toast.makeText(this, "Fotoaparát není povolen!", Toast.LENGTH_LONG).show();
            }

        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference().child(user.getId() + ".jpg");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] photoBytes = stream.toByteArray();

                final ProgressDialog dlg = ProgressDialog.show(ctx, "Nahrát fotografii", "Může to trvat několik vteřin...");
                storageRef.putBytes(photoBytes).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            dlg.dismiss();
                        }
                        else {
                            Toast.makeText(ctx, "Nahrávání obrázku selhalo. Zkontrolujte připojení k internetu.", Toast.LENGTH_SHORT).show();
                            dlg.dismiss();
                        }
                    }
                });
            }
        }

        if (requestCode == RESULT_LOAD_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri url = data.getData();

                if (url == null) {
                    Toast.makeText(ctx, "Obrázek nelze nahrát.", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference().child(user.getId() + ".jpg");

                final ProgressDialog dlg = ProgressDialog.show(ctx, "Nahrát fotografii", "Může to trvat několik vteřin...");
                storageRef.putFile(url).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            dlg.dismiss();
                        }
                        else {
                            Toast.makeText(ctx, "Nahrávání obrázku selhalo. Zkontrolujte připojení k internetu.", Toast.LENGTH_SHORT).show();
                            dlg.dismiss();
                        }
                    }
                });
            }
        }

    }
}

