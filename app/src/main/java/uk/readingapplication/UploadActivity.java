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

    EditText uploadTitle, uploadDesc, uploadLang, uploadStory;

    //placeholder picture
    ImageView uploadImage;

    //button save all
    Button saveButton;

    //button save video
    Button saveMediaButton;

    //button save audio
    Button saveAudioButton;

    //upload buttons
    Button uploadMedia;
    Button uploadAudio;

    String imageURL;
    Uri uri;

    String videoURL;
    String audioURL;
    Uri uri1;
    Uri uri2;
    VideoView uploadVideo;
    VideoView uploadAudioView;

    MediaController mediaController;
    MediaController mediaController1;


    //new view for when selecting upload media (phone view)

    private void MyCustomView (Context context) {

        TypedArray arr = context.obtainStyledAttributes(R.styleable.MyCustomView);
        readAttributes(arr, context);
        arr.recycle();
    }

    //read attributes of the view
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


        //media controllers may malfunction based on order of code:

        mediaController = new MediaController(this);
        uploadVideo.setMediaController(mediaController);
        uploadVideo.start();



        mediaController1 = new MediaController(this);
        uploadAudioView.setMediaController(mediaController1);
        uploadAudioView.start();
        uploadAudioView = findViewById(R.id.uploadAudioView);



        //uploads uri of an image with Toast error on defect
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

        //uploads uri of a video with Toast error on defect

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

        //uploads uri of an audio with Toast error on defect

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

        //code to allow photoPicker based on image
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        //code to allow photoPicker based on audio but places in a VideoView
        uploadAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent audioPicker = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                audioPicker.addCategory(Intent.CATEGORY_OPENABLE);
                audioPicker.setType("audio/*");
                activityResultLauncher2.launch(audioPicker);
            }
        });

        //code to allow photoPicker based on video

        uploadMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoPicker = new Intent(Intent.ACTION_PICK);
                videoPicker.setType("video/*");
                activityResultLauncher1.launch(videoPicker);
                MyCustomView(UploadActivity.this);
            }
        });

        //save text and proceed to MainActivity
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        //save video
        saveMediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMedia();
            }
        });

        //save audio
        saveAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAudio();
            }
        });

    }


    //save image data
    public void saveData() {

        if (uri == null) {
            Toast.makeText(UploadActivity.this, "Error, no image selected", Toast.LENGTH_SHORT).show();
        } else {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android Images")
                    .child(uri.getLastPathSegment());

            //initiate loading screen
            AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();

            //save image data inside the storage
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

    //save video data
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

            //save video data inside the storage
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

    //save audio data
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

            //save audio data inside the storage
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


    //upload everything in the realtime-database
    public void uploadData(){

        String title = uploadTitle.getText().toString();
        String desc = uploadDesc.getText().toString();
        String lang = uploadLang.getText().toString();
        String story = uploadStory.getText().toString();

        //creates a DataClass that retrieves the data and .setValue to the database

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