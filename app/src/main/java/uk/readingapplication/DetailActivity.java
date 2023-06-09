package uk.readingapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.nfc.cardemulation.CardEmulation;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Collections;

public class DetailActivity extends AppCompatActivity {

    TextView detailDesc, detailTitle, detailLang, detailStory;
    ImageView detailImage;
    FloatingActionButton deleteButton, editButton;

    //placeholders for the DataClass
    String key = "";
    String imageUrl = "";
    String videoUrl = "";
    String audioUrl = "";

    PlayerView detailAudio;
    PlayerView detailVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailDesc = findViewById(R.id.detailDesc);
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailLang = findViewById(R.id.detailLang);
        detailStory = findViewById(R.id.detailStory);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        detailVideo = findViewById(R.id.detailVideo);
        detailAudio = findViewById(R.id.detailAudio);


        //retrieves text data and image with string placeholders
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            detailDesc.setText(bundle.getString("Description"));
            detailTitle.setText(bundle.getString("Title"));
            detailLang.setText(bundle.getString("Language"));
            detailStory.setText(bundle.getString("Story"));
            key = bundle.getString("Key");

            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);

            videoUrl = bundle.getString("Video");
            initializeExoplayerView(videoUrl);

            audioUrl = bundle.getString("Audio");
            initializeExoplayerAudio(audioUrl);
        }



        //change view to Video Library when video clicked
        detailVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoUrl = bundle.getString("Video");
                Intent intent = new Intent(DetailActivity.this, FullscreenActivity.class)
                        .putExtra("Video1", videoUrl);
                startActivity(intent);
            }
        });

        //delete image, if only image available. delete image and video if only image and video available. delete image, video and audio if all available.
        try {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DatabaseReference reference = FirebaseDatabase.getInstance("https://readingapplication-c4df8-default-rtdb.europe-west1.firebasedatabase.app").getReference("Android Content");
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                    storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            reference.child(key).removeValue();
                            Toast.makeText(DetailActivity.this, "Story deleted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    });
                }
            });
        } catch (Exception e){

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final DatabaseReference reference = FirebaseDatabase.getInstance("https://readingapplication-c4df8-default-rtdb.europe-west1.firebasedatabase.app").getReference("Android Content");
                    FirebaseStorage storage = FirebaseStorage.getInstance();

                    StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                    StorageReference storageReference1 = storage.getReferenceFromUrl(videoUrl);


                    storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            reference.child(key).removeValue();
                            Toast.makeText(DetailActivity.this, "Story deleted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }


                    });

                    storageReference1.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            reference.child(key).removeValue();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();

                        }
                    });
                }
            });

            try  {

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final DatabaseReference reference = FirebaseDatabase.getInstance("https://readingapplication-c4df8-default-rtdb.europe-west1.firebasedatabase.app").getReference("Android Content");
                        FirebaseStorage storage = FirebaseStorage.getInstance();

                        StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                        StorageReference storageReference1 = storage.getReferenceFromUrl(videoUrl);
                        StorageReference storageReference2 = storage.getReferenceFromUrl(audioUrl);


                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                reference.child(key).removeValue();
                                Toast.makeText(DetailActivity.this, "Story deleted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();

                            }
                        });

                        storageReference1.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                reference.child(key).removeValue();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                        });

                        storageReference2.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                reference.child(key).removeValue();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();

                            }
                        });
                    }

                });

            } catch (Exception a){

            }

        }

        //sends users to UpdateClass, edits all Strings
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class)
                        .putExtra("Title", detailTitle.getText().toString())
                        .putExtra("Description", detailDesc.getText().toString())
                        .putExtra("Language", detailLang.getText().toString())
                        .putExtra("Story", detailStory.getText().toString())
                        .putExtra("Video", videoUrl)
                        .putExtra("Image", imageUrl)
                        .putExtra("Audio", audioUrl)
                        .putExtra("Key", key);
                startActivity(intent);
            }
        });

    }

    //ExoPlayer for videos
    private void initializeExoplayerView(String videoURL){
        try {
            ExoPlayer exoPlayer = new ExoPlayer.Builder(this).build();
            detailVideo.setPlayer(exoPlayer);
            Uri videouri = Uri.parse(videoURL);
            MediaItem mediaItem = MediaItem.fromUri(videouri);
            exoPlayer.addMediaItems(Collections.singletonList(mediaItem));
            exoPlayer.prepare();
            exoPlayer.setPlayWhenReady(false);

        } catch (Exception e) {
            Toast.makeText(this, "No video added yet", Toast.LENGTH_SHORT).show();
        }
    }

    //ExoPlayer for Audios
    private void initializeExoplayerAudio(String audioURL){
        try {
            ExoPlayer exoPlayer1 = new ExoPlayer.Builder(this).build();
            detailAudio.setPlayer(exoPlayer1);
            Uri videouri1 = Uri.parse(audioURL);
            MediaItem mediaItem1 = MediaItem.fromUri(videouri1);
            exoPlayer1.addMediaItems(Collections.singletonList(mediaItem1));
            exoPlayer1.prepare();
            exoPlayer1.setPlayWhenReady(false);

        } catch (Exception e) {
            Toast.makeText(this, "No audio added yet", Toast.LENGTH_SHORT).show();
        }
    }
}