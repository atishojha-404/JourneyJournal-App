package com.atish.journeyjournal.Journal;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.atish.journeyjournal.MainActivity;
import com.atish.journeyjournal.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class JournalDetails extends AppCompatActivity {

    Intent data;
    FirebaseFirestore fStore;
    FirebaseUser user;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fStore = FirebaseFirestore.getInstance();
        data = getIntent();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();


        TextView title = findViewById(R.id.journaltitle);
        TextView content = findViewById(R.id.journalDetailsContent);
        TextView location = findViewById(R.id.locationtext);
        TextView datetime = findViewById(R.id.dateandtime);
        ImageView image = findViewById(R.id.journalimage);
        content.setMovementMethod(new ScrollingMovementMethod());

        content.setText(data.getStringExtra("content"));
        title.setText(data.getStringExtra("title"));
        location.setText(data.getStringExtra("location"));
        datetime.setText(data.getStringExtra("datetime"));
        Glide.with(image.getContext()).load(data.getStringExtra("image")).into(image);
        String docId = data.getStringExtra("journalID");

        String titletoshare = data.getStringExtra("title");
        String contenttoshare = data.getStringExtra("content");
        String dateandtimetoshare = data.getStringExtra("datetime");
        String locationtoshare = data.getStringExtra("location");



        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), FullScreenImage.class);
                i.putExtra("image", data.getStringExtra("image"));
                startActivity(i);
            }
        });

        FloatingActionButton fabEdit = findViewById(R.id.editbtn);
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), EditJournal.class);
                i.putExtra("title", data.getStringExtra("title"));
                i.putExtra("content", data.getStringExtra("content"));
                i.putExtra("location", data.getStringExtra("location"));
                i.putExtra("datetime", data.getStringExtra("datetime"));
                i.putExtra("image", data.getStringExtra("image"));
                i.putExtra("journalID", data.getStringExtra("journalID"));
                startActivity(i);
            }
        });

        FloatingActionButton fabDelete = findViewById(R.id.deletebtn);
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(JournalDetails.this);
                dialog.setTitle("Delete Journal");
                dialog.setMessage("Are you sure?");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DocumentReference docRef = fStore.collection("Journals").document(user.getUid()).collection("myJournals").document(docId);
                        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(JournalDetails.this, "Journal Deleted.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(JournalDetails.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dialog.setNegativeButton("Cancel", null);
                dialog.setCancelable(false);
                dialog.show();

            }
        });

        FloatingActionButton fabShare = findViewById(R.id.sharebtn);
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable drawable = (BitmapDrawable)image.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", "content");

                Uri uri = Uri.parse(bitmapPath);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("imge/png");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Title: " + titletoshare+"\n" + "Date: " + dateandtimetoshare+"\n" + "Location: " + locationtoshare+"\n" + "My Thoughts: " + contenttoshare);
                startActivity(Intent.createChooser(intent, "Share Via"));
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

}