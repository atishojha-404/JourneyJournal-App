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

import com.atish.journeyjournal.MainActivity;
import com.atish.journeyjournal.MapActivity;
import com.atish.journeyjournal.R;
import com.bumptech.glide.Glide;
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

import java.io.InputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EditJournal extends AppCompatActivity {

    Intent data;
    TextInputEditText idEdttitleedit, idEdtlocationedit, idEdtaboutjournaledit;
    ImageView journalimageedit;
    Button editjournalbtn;
    ImageButton maplocation;
    private ProgressBar progessBar;

    FirebaseFirestore fStore;
    Uri filePath;
    private FirebaseStorage firebaseStorage;
    private Bitmap bitmap;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_journal);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fStore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        data = getIntent();

        idEdttitleedit = findViewById(R.id.idEdttitleedit);
        idEdtlocationedit = findViewById(R.id.idEdtlocationedit);
        idEdtaboutjournaledit = findViewById(R.id.idEdtaboutjournaledit);
        journalimageedit = findViewById(R.id.journalimageedit);
        editjournalbtn = findViewById(R.id.editjournalbtn);
        progessBar = findViewById(R.id.progessbare);
        maplocation = findViewById(R.id.maplocation);

        //        Date and time
        Date currentTime = Calendar.getInstance().getTime();
        String formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);

        String journalTitle = data.getStringExtra("title");
        String journalLocation = data.getStringExtra("location");
        String journalDetail = data.getStringExtra("content");
        String image = data.getStringExtra("image");

        idEdttitleedit.setText(journalTitle);
        idEdtlocationedit.setText(journalLocation);
        idEdtaboutjournaledit.setText(journalDetail);
        Glide.with(journalimageedit.getContext()).load(data.getStringExtra("image")).into(journalimageedit);

        maplocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditJournal.this, MapActivity.class);
                startActivity(intent);
            }
        });

        journalimageedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(EditJournal.this)
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

        editjournalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    editjournalbtn.setEnabled(false);
                    editjournalbtn.setTextColor(getResources().getColor(R.color.btntxtdisable));
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
                                                    String title = idEdttitleedit.getText().toString();
                                                    String image = uri.toString();
                                                    String location = idEdtlocationedit.getText().toString();
                                                    String datetime = formattedDate.toString();
                                                    String aboutj = idEdtaboutjournaledit.getText().toString();

                                                    DocumentReference docref = fStore.collection("Journals").document(user.getUid()).collection("myJournals").document(data.getStringExtra("journalID"));

                                                    Map<String, Object> journal = new HashMap<>();
                                                    journal.put("title", title);
                                                    journal.put("image", image);
                                                    journal.put("location", location);
                                                    journal.put("datetime", datetime);
                                                    journal.put("aboutj", aboutj);

                                                    docref.update(journal).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            progessBar.setVisibility(View.VISIBLE);
                                                            editjournalbtn.setEnabled(false);
                                                            editjournalbtn.setTextColor(getResources().getColor(R.color.btntxtdisable));
                                                            Toast.makeText(EditJournal.this, "Journal Updated.", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progessBar.setVisibility(View.GONE);
                                                            editjournalbtn.setEnabled(true);
                                                            editjournalbtn.setTextColor(getResources().getColor(R.color.btntxt));
                                                            Toast.makeText(EditJournal.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                }
                                            });
                                }
                            });

                } catch (Exception e) {

                    editjournalbtn.setEnabled(true);
                    editjournalbtn.setTextColor(getResources().getColor(R.color.btntxt));
                    progessBar.setVisibility(View.GONE);
                    Toast.makeText(EditJournal.this, "Please Select Image again", Toast.LENGTH_SHORT).show();
                }


            }
        });

        idEdttitleedit.addTextChangedListener(new TextWatcher() {
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
        if (!TextUtils.isEmpty(idEdttitleedit.getText())) {
            editjournalbtn.setEnabled(true);
            editjournalbtn.setTextColor(getResources().getColor(R.color.btntxt));
        } else {
            editjournalbtn.setEnabled(false);
            editjournalbtn.setTextColor(getResources().getColor(R.color.btntxtdisable));
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

        if (requestCode == 1 && resultCode == RESULT_OK) {
            filePath = data.getData();
            try {
                InputStream inputStream = this.getApplicationContext().getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                journalimageedit.setImageBitmap(bitmap);
                journalimageedit.setRotation(90);
            } catch (Exception ex) {
                Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}