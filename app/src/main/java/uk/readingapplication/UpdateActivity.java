package uk.readingapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UpdateActivity extends AppCompatActivity {

    ImageView updateImage;
    Button updateButton;
    Button updateAudioButton;
    Button saveMediaButton2;
    EditText updateDesc, updateTitle, updateLang, updateStory;
    String title, desc, lang, story;
    String imageUrl;
    String key, oldImageURL;
    Uri uri;



    VideoView videoViewUpdate;

    VideoView updateAudioView;
    Button updateMedia;

    Button updateAudio;
    String videoURL;
    Uri uri1;
    String key1, oldVideoURL;
    MediaController mediaController;


    String audioURL;

    Uri uri2;
    String key2, oldAudioURL;
    MediaController mediaController1;


    DatabaseReference databaseReference;
    StorageReference storageReference;

    private void MyCustomView (Context context) {

        TypedArray arr = context.obtainStyledAttributes(R.styleable.MyCustomView);
        readAttributes(arr, context);
        arr.recycle();
    }

    private void readAttributes(TypedArray arr, Context context){
        //uploadVideo.setBackground(ContextCompat.getDrawable(context, R.color.black));
        videoViewUpdate.setBackground(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        updateButton = findViewById(R.id.updateButton);
        updateDesc = findViewById(R.id.updateDesc);
        updateImage = findViewById(R.id.updateImage);
        updateLang = findViewById(R.id.updateLang);
        updateStory = findViewById(R.id.updateStory);
        updateTitle = findViewById(R.id.updateTitle);
        updateMedia = findViewById(R.id.updateMedia);
        updateAudio = findViewById(R.id.updateAudio);

        saveMediaButton2 = findViewById(R.id.saveMediaButton2);
        videoViewUpdate = findViewById(R.id.videoViewUpdate);


        updateAudioButton = findViewById(R.id.updateAudioButton);
        updateAudioView = findViewById(R.id.updateAudioView);




        mediaController = new MediaController(this);
        videoViewUpdate.setMediaController(mediaController);
        videoViewUpdate.start();



        mediaController1 = new MediaController(this);
        updateAudioView.setMediaController(mediaController1);
        updateAudioView.start();
        updateAudioView = findViewById(R.id.updateAudioView);


        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            updateImage.setImageURI(uri);
                        }else {
                            Toast.makeText(UpdateActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
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
                            videoViewUpdate.setVideoURI(uri1);
                        } else {
                            Toast.makeText(UpdateActivity.this, "No Video Selected", Toast.LENGTH_SHORT).show();
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
                            updateAudioView.setVideoURI(uri2);
                        } else {
                            Toast.makeText(UpdateActivity.this, "No Audio Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Glide.with (UpdateActivity.this).load(bundle.getString("Image")).into(updateImage);
            updateTitle.setText(bundle.getString("Title"));
            updateDesc.setText(bundle.getString("Description"));
            updateLang.setText(bundle.getString("Language"));
            updateStory.setText(bundle.getString("Story"));
            key = bundle.getString("Key");
            oldImageURL = bundle.getString("Image");
            oldVideoURL = bundle.getString("Video");
            oldAudioURL = bundle.getString("Audio");
        }
        databaseReference = FirebaseDatabase.getInstance("https://readingapplication-c4df8-default-rtdb.europe-west1.firebasedatabase.app").getReference("Android Content").child(key);

        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        updateMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoPicker = new Intent(Intent.ACTION_PICK);
                videoPicker.setType("video/*");
                activityResultLauncher1.launch(videoPicker);
                MyCustomView(UpdateActivity.this);
            }
        });

        updateAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent audioPicker = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                audioPicker.addCategory(Intent.CATEGORY_OPENABLE);
                audioPicker.setType("audio/*");
                activityResultLauncher2.launch(audioPicker);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });


        saveMediaButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveMedia();
            }
        });

        updateAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveAudio();
            }
        });

    }


    public void saveData(){

        if (uri == null) {
            Toast.makeText(UpdateActivity.this, "Error, no image replaced", Toast.LENGTH_SHORT).show();
        } else {

            storageReference = FirebaseStorage.getInstance().getReference().child("Android Images")
                    .child(uri.getLastPathSegment());

            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
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
                    imageUrl = urlImage.toString();
                    updateData();
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
            Toast.makeText(UpdateActivity.this, "Error, no video replaced", Toast.LENGTH_SHORT).show();
        } else {
            StorageReference storageReference1 = FirebaseStorage.getInstance().getReference().child("Android Videos")
                    .child(uri1.getLastPathSegment());

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UpdateActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            android.app.AlertDialog dialog = builder.create();
            dialog.show();

            storageReference1.putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
                    Uri urlVideo = uriTask.getResult();
                    videoURL = urlVideo.toString();
                    //updateData();
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
            Toast.makeText(UpdateActivity.this, "Error, no audio selected", Toast.LENGTH_SHORT).show();
        } else {
            StorageReference storageReference2 = FirebaseStorage.getInstance().getReference().child("Android Audios")
                    .child(uri2.getLastPathSegment());

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UpdateActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            android.app.AlertDialog dialog = builder.create();
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

    public void updateData(){

        title = updateTitle.getText().toString().trim();
        desc = updateDesc.getText().toString().trim();
        lang = updateLang.getText().toString();
        story = updateStory.getText().toString();

        DataClass dataClass = new DataClass(title, desc, lang, story, imageUrl, videoURL, audioURL);

        databaseReference.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    StorageReference reference;
                    StorageReference reference1;
                    StorageReference reference2;

                    try {
                        reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                        reference1 = FirebaseStorage.getInstance().getReferenceFromUrl(oldVideoURL);
                        reference2 = FirebaseStorage.getInstance().getReferenceFromUrl(oldAudioURL);
                        reference.delete();
                        reference1.delete();
                        reference2.delete();

                    } catch (Exception e) {

                    }

                    try {

                        reference1 = FirebaseStorage.getInstance().getReferenceFromUrl(oldVideoURL);
                        reference2 = FirebaseStorage.getInstance().getReferenceFromUrl(oldAudioURL);
                        reference1.delete();
                        reference2.delete();

                    } catch (Exception e)

                    {

                    }

                    try {

                        reference2 = FirebaseStorage.getInstance().getReferenceFromUrl(oldAudioURL);
                        reference2.delete();

                    }catch (Exception d){

                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}