package uk.readingapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.TrackSelectionView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class UploadActivity extends AppCompatActivity {

    ImageView uploadImage;
    Button saveButton;
    Button saveMediaButton;
    Button saveAudioButton;
    EditText uploadTitle, uploadDesc, uploadLang, uploadStory;
    String imageURL;
    Uri uri;
    VideoView uploadVideo;
    VideoView uploadAudioView;



    Button uploadMedia;
    Button uploadAudio;

    String videoURL;
    String audioURL;
    Uri uri1;
    Uri uri2;
    MediaController mediaController;
    MediaController mediaController1;


    private void MyCustomView (Context context) {

        TypedArray arr = context.obtainStyledAttributes(R.styleable.MyCustomView);
        readAttributes(arr, context);
        arr.recycle();
    }

    private void readAttributes(TypedArray arr, Context context){
        //uploadVideo.setBackground(ContextCompat.getDrawable(context, R.color.black));
        uploadVideo.setBackground(null);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        uploadImage = findViewById(R.id.uploadImage);
        uploadDesc = findViewById(R.id.uploadDesc);
        uploadTitle = findViewById(R.id.uploadTitle);
        uploadLang = findViewById(R.id.uploadLang);
        uploadStory = findViewById(R.id.uploadStory);

        uploadVideo = findViewById(R.id.videoView);
        uploadMedia = findViewById(R.id.uploadMedia);




        saveButton = findViewById(R.id.saveButton);
        saveMediaButton = findViewById(R.id.saveMediaButton);
        saveAudioButton = findViewById(R.id.saveAudioButton);


        uploadAudioView = findViewById(R.id.uploadAudioView);
        uploadAudio = findViewById(R.id.uploadAudio);

        mediaController = new MediaController(this);
        uploadVideo.setMediaController(mediaController);
        uploadVideo.start();



        mediaController1 = new MediaController(this);
        uploadAudioView.setMediaController(mediaController1);
        uploadAudioView.start();
        uploadAudioView = findViewById(R.id.uploadAudioView);




        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            uploadImage.setImageURI(uri);
                        } else {
                            Toast.makeText(UploadActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        ActivityResultLauncher<Intent> activityResultLauncher1 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri1 = data.getData();
                            uploadVideo.setVideoURI(uri1);
                        } else {
                            Toast.makeText(UploadActivity.this, "No Video Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );


        ActivityResultLauncher<Intent> activityResultLauncher2 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri2 = data.getData();
                            // Uri uri= Uri.parse();

                            uploadAudioView.setVideoURI(uri2);
                        } else {
                            Toast.makeText(UploadActivity.this, "No Audio Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        uploadAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent audioPicker = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                audioPicker.addCategory(Intent.CATEGORY_OPENABLE);
                audioPicker.setType("audio/*");
                activityResultLauncher2.launch(audioPicker);
            }
        });

        uploadMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoPicker = new Intent(Intent.ACTION_PICK);
                videoPicker.setType("video/*");
                activityResultLauncher1.launch(videoPicker);
                MyCustomView(UploadActivity.this);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        saveMediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMedia();
            }
        });

        saveAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAudio();
            }
        });

    }


    public void saveData() {

        if (uri == null) {
            Toast.makeText(UploadActivity.this, "Error, no image selected", Toast.LENGTH_SHORT).show();
        } else {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android Images")
                    .child(uri.getLastPathSegment());

            AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
                    Uri urlImage = uriTask.getResult();
                    imageURL = urlImage.toString();

                    //Uri urlVideo = uriTask.getResult();
                    //videoURL = urlVideo.toString();

                    uploadData();
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });

        }
    }

    public void saveMedia() {
        if (uri1 == null) {
            Toast.makeText(UploadActivity.this, "Error, no video selected", Toast.LENGTH_SHORT).show();
        } else {
            StorageReference storageReference1 = FirebaseStorage.getInstance().getReference().child("Android Videos")
                    .child(uri1.getLastPathSegment());

            AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();

            storageReference1.putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
                    Uri urlVideo = uriTask.getResult();
                    videoURL = urlVideo.toString();
                    //uploadData();
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });

        }
    }

    public void saveAudio() {
        if (uri2 == null) {
            Toast.makeText(UploadActivity.this, "Error, no audio selected", Toast.LENGTH_SHORT).show();
        } else {
            StorageReference storageReference2 = FirebaseStorage.getInstance().getReference().child("Android Audios")
                    .child(uri2.getLastPathSegment());

            AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();

            storageReference2.putFile(uri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
                    Uri urlAudio = uriTask.getResult();
                    audioURL = urlAudio.toString();
                    //uploadData();
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });

        }
    }


    public void uploadData(){

        String title = uploadTitle.getText().toString();
        String desc = uploadDesc.getText().toString();
        String lang = uploadLang.getText().toString();
        String story = uploadStory.getText().toString();

        DataClass dataClass = new DataClass(title, desc, lang, story, imageURL, videoURL, audioURL);

        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        FirebaseDatabase.getInstance("https://readingapplication-c4df8-default-rtdb.europe-west1.firebasedatabase.app").getReference("Android Content").child(currentDate)
                .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(UploadActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}