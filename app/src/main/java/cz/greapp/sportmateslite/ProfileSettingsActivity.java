package cz.greapp.sportmateslite;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;

import cz.greapp.sportmateslite.Data.Models.User;

public class ProfileSettingsActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button takePhotoButton;
    Button selectPhotoButton;
    Button changePasswordButton;

    String passwordText;

    User user;

    Context ctx;
    Activity activity;

    Uri photoOutputUri;

    public static final int RESULT_LOAD_IMAGE = 100;
    public static final int REQUEST_CAMERA_PERMISSION = 0;
    public static final int REQUEST_WRITE_PERMISSION = 8096;
    public static final int RESULT_TAKE_PHOTO = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        ctx = this;
        activity = this;
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
                takePhoto();
            }
        });

        changePasswordButton = (Button) findViewById(R.id.changePasswordButton);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Změnit heslo");

                final EditText input = new EditText(ctx);
                LayoutInflater inflater = activity.getLayoutInflater();



                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                final View dlgView = inflater.inflate(R.layout.dialog_change_password, null);
                builder.setView(dlgView);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    TextInputEditText oldPass;
                    TextInputEditText newPass;
                    ProgressBar progressBar;
                    FirebaseUser firebaseUser;
                    DialogInterface dlg;
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         oldPass = dlgView.findViewById(R.id.oldPass);
                         newPass = dlgView.findViewById(R.id.newPass);

                         progressBar = dlgView.findViewById(R.id.passwordChangeProgressBar);
                         dlg = dialog;

                         oldPass.setVisibility(View.INVISIBLE);
                         newPass.setVisibility(View.INVISIBLE);
                         progressBar.setVisibility(View.VISIBLE);

                         if (oldPass.getText().toString().equals("") || newPass.getText().toString().equals("")) {
                             Toast.makeText(ctx, "Vyplňte prosím všechna pole.", Toast.LENGTH_SHORT).show();
                         }

                         else if (newPass.getText().toString().length() < 6) {
                             Toast.makeText(ctx, "Heslo musí být alespoň 6 znaků dlouhé.", Toast.LENGTH_SHORT).show();
                         }

                         else {
                             firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                             AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPass.getText().toString());
                             firebaseUser.reauthenticate(credential)
                                     .addOnCompleteListener(new OnCompleteListener<Void>() {
                                         @Override
                                         public void onComplete(@NonNull Task<Void> task) {
                                             if (task.isSuccessful()) {
                                                 firebaseUser.updatePassword(newPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task) {
                                                         if (task.isSuccessful()) {
                                                             Toast.makeText(ctx, "Heslo bylo změněno.", Toast.LENGTH_SHORT).show();
                                                             dlg.dismiss();
                                                         } else {
                                                             Toast.makeText(ctx, "Chyba při změně hesla. Zkontrolujte prosím připojení k internetu.", Toast.LENGTH_SHORT).show();
                                                             dlg.dismiss();
                                                         }
                                                     }
                                                 });
                                             } else {
                                                 Toast.makeText(ctx, "Zadané heslo je špatné.", Toast.LENGTH_SHORT).show();
                                                 dlg.dismiss();
                                             }
                                         }
                                     });
                         }
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
                takePhoto();
            } else {
                Toast.makeText(this, "Fotoaparát není povolen!", Toast.LENGTH_LONG).show();
            }

        }

        if (requestCode == ProfileSettingsActivity.REQUEST_WRITE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                Toast.makeText(this, "Zápis do externí paměti není povolen!", Toast.LENGTH_LONG).show();
            }

        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void takePhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ProfileSettingsActivity.REQUEST_WRITE_PERMISSION);
            } else {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                }
                else{
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory(),
                            "sportmates_" + user.getId() + "_" + System.currentTimeMillis() + ".jpg");
                    photoOutputUri = FileProvider.getUriForFile(ctx, BuildConfig.APPLICATION_ID + ".provider", file);

                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoOutputUri);
                    startActivityForResult(cameraIntent, ProfileSettingsActivity.RESULT_TAKE_PHOTO);
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {

                if (photoOutputUri == null) {
                    return;
                }

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference().child(user.getId() + ".jpg");

                final ProgressDialog dlg = ProgressDialog.show(ctx, "Nahrát fotografii", "Může to trvat několik vteřin...");
                storageRef.putFile(photoOutputUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
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

