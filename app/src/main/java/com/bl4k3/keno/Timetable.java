package com.bl4k3.keno;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.sql.Time;

public class Timetable extends AppCompatActivity {

    public FirebaseAuth mAuth;
    FirebaseUser user;
    ImageView imageView;
    RelativeLayout rl;
    TextView textView;
    Bitmap bitmap;
    String TAG = "Timetable";
    String photoStringLink;
    private SharedPreferences pref;
    private static final int CHOOSE_IMAGE = 101;
   // public static final String Timetable = "Timetable";
   // public static final String TT_img = "Timetable_img;";
    Uri LocalFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        rl = findViewById(R.id.nott);
        mAuth = FirebaseAuth.getInstance();
        imageView = findViewById(R.id.timetableImage);
        textView = findViewById(R.id.tx1);
        user = mAuth.getCurrentUser();
        //Back button
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //pref = getSharedPreferences(Timetable, Context.MODE_PRIVATE);
        //pref.edit().putInt(TT_img,0).apply();
        //Log.d(TAG, "Timetable IMG to Prefs 0 :Success");


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData()!= null){
            LocalFileUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),LocalFileUri);
                imageView.setImageBitmap(bitmap);
                Log.d(TAG, "Image to FBS :Init");


                UploadImageToFirebase();

                Log.d(TAG, "Image to FBS :Done");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void UploadImageToFirebase() {
        if (user!=null) {
            final StorageReference profileImgRef = FirebaseStorage.getInstance().getReference(Constants.TIMETABLE_PATH_UPLOADS + user.getUid()+ "/" + System.currentTimeMillis() + ".jpg");
            if (LocalFileUri != null) {
                UploadTask uploadTask = profileImgRef.putFile(LocalFileUri);
                Task<Uri> urlTask =uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return  profileImgRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Uri downloadUri = task.getResult();
                            Toast.makeText(getApplicationContext(),"Successfully uploaded",Toast.LENGTH_SHORT).show();
                            if (downloadUri != null){
                                photoStringLink = downloadUri.toString();
                                //Toast.makeText(getApplicationContext(),photoStringLink,Toast.LENGTH_SHORT).show();

                            } else {
                                //Handle failures
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user != null){
            if(user.getPhotoUrl()!=null){
                rl.setVisibility(View.GONE);
                Glide.with(this)
                        .load(user.getPhotoUrl().toString()).into(imageView);
            }else {
                rl.setVisibility(View.VISIBLE);
            }
        }
    }

    public void ttAdd(View v){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select timetable image"), CHOOSE_IMAGE);
    }
    public void save(View v){
        if(user != null && photoStringLink != null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(photoStringLink)).build();

            user.updateProfile(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG,"Profile data updated");
                    //Toast.makeText(getApplicationContext(),"Successfully updated profile data",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"Profile data update Failed");
                   //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
