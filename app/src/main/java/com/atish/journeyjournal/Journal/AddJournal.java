package com.atish.journeyjournal.Journal;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.atish.journeyjournal.MapActivity;
import com.atish.journeyjournal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AddJournal extends AppCompatActivity {

    FirebaseFirestore fStore;
    Uri filePath;
    Button addjournalbtn;
    private FirebaseStorage firebaseStorage;
    private Bitmap bitmap;
    private TextInputEditText idEdtTitle, idEdtlocation, idEdtAboutJournal;
    private ImageView journalImage;
    private ImageButton maplocation;
    private ProgressBar progessBar;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fStore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        firebaseStorage = FirebaseStorage.getInstance();

        addjournalbtn = findViewById(R.id.addjournalbtn);
        idEdtTitle = findViewById(R.id.idEdttitle);
        idEdtlocation = findViewById(R.id.idEdtlocation);
        idEdtAboutJournal = findViewById(R.id.idEdtaboutjournal);
        journalImage = findViewById(R.id.journalimage);
        maplocation = findViewById(R.id.maplocation);
        progessBar = findViewById(R.id.progessbar);


//        Date and time
        Date currentTime = Calendar.getInstance().getTime();
        String formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);

        maplocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddJournal.this, MapActivity.class);
                startActivity(intent);
            }
        });

        journalImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                select image from gallery

                Dexter.withContext(AddJournal.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Select Image File"), 1);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();

            }
        });

        addjournalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                for select image from gallery

                try {
                    addjournalbtn.setEnabled(false);
                    addjournalbtn.setTextColor(getResources().getColor(R.color.btntxtdisable));
                    progessBar.setVisibility(View.VISIBLE);

                    StorageReference storageReference = firebaseStorage.getReference("journeyjournal" + new Random().nextInt(100000000));
                    storageReference.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String title = idEdtTitle.getText().toString();
                                                    String image = uri.toString();
                                                    String location = idEdtlocation.getText().toString();
                                                    String aboutj = idEdtAboutJournal.getText().toString();
                                                    String datetime = formattedDate.toString();

                                                    DocumentReference docref = fStore.collection("Journals").document(user.getUid()).collection("myJournals").document();

                                                    Map<String, Object> journal = new HashMap<>();
                                                    journal.put("title", title);
                                                    journal.put("image", image);
                                                    journal.put("location", location);
                                                    journal.put("aboutj", aboutj);
                                                    journal.put("datetime", datetime);

                                                    docref.set(journal).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            progessBar.setVisibility(View.VISIBLE);
                                                            addjournalbtn.setEnabled(false);
                                                            addjournalbtn.setTextColor(getResources().getColor(R.color.btntxtdisable));
                                                            Toast.makeText(AddJournal.this, "Journal Added.", Toast.LENGTH_SHORT).show();
                                                            onBackPressed();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progessBar.setVisibility(View.GONE);
                                                            addjournalbtn.setEnabled(true);
                                                            addjournalbtn.setTextColor(getResources().getColor(R.color.btntxt));
                                                            Toast.makeText(AddJournal.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                }
                                            });
                                }
                            });

                } catch (Exception e) {
                    addjournalbtn.setEnabled(true);
                    addjournalbtn.setTextColor(getResources().getColor(R.color.btntxt));
                    progessBar.setVisibility(View.GONE);
                    Toast.makeText(AddJournal.this, "Image not found.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        idEdtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                no use for now
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                no use for now
            }
        });

    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(idEdtTitle.getText())) {
            addjournalbtn.setEnabled(true);
            addjournalbtn.setTextColor(getResources().getColor(R.color.btntxt));
        } else {
            addjournalbtn.setEnabled(false);
            addjournalbtn.setTextColor(getResources().getColor(R.color.btntxtdisable));
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        select image from gallery
        if (requestCode == 1 && resultCode == RESULT_OK) {
            filePath = data.getData();
            try {
                InputStream inputStream = this.getApplicationContext().getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                journalImage.setImageBitmap(bitmap);
                journalImage.setRotation(90);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                Toast.makeText(AddJournal.this, "Image was not found", Toast.LENGTH_SHORT).show();
            }
        }

    }

}