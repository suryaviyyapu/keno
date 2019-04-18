package com.bl4k3.keno;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;




public class FileUpload extends AppCompatActivity implements View.OnClickListener{
    final static int PICK_PDF_CODE = 2342;
    Button choose;
    TextView textView;
    EditText editText;
    ProgressBar progressBar;
    StorageReference mstorageReference;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fileupload);
        editText = findViewById(R.id.file_name);
        choose = findViewById(R.id.choose_file);
        textView = findViewById(R.id.textViewStatus);
       // upload = (Button) findViewById(R.id.button_upload);
        progressBar = findViewById(R.id.progress_bar);
        mstorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        findViewById(R.id.choose_file).setOnClickListener(this);
        //findViewById(R.id.button_upload).setOnClickListener(this);


    }


    private void getPDF() {
        //for greater than lollipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
        ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }
        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_CODE);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user chooses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                //progressBar.setVisibility(View.VISIBLE);
                uploadFile(data.getData());
            }else{
                //progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void uploadFile(Uri data) {
        String filename = editText.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        final StorageReference sRef = mstorageReference.child(Constants.STORAGE_PATH_UPLOADS + filename/*System.currentTimeMillis()*/ + ".pdf");
       // UploadTask uploadTask = sRef.putFile(data);


/*

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return sRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    textView.setText("File Uploaded Successfully");
                    String url = sRef.getDownloadUrl().toString();
                    mDatabaseReference.child(mDatabaseReference.push().getKey().setValue(url));
                    progressBar.setVisibility(View.GONE);
                    Uri downloadUri = task.getResult();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });*/
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.GONE);
                textView.setText("File Uploaded Successfully");
                Upload upload = new Upload(editText.getText().toString());//, sRef.getDownloadUrl().toString());
                mDatabaseReference.child(mDatabaseReference.push().getKey()).setValue(upload);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                textView.setText((int) progress + "% Uploading...");
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_file:
                String file_name = editText.getText().toString().trim();
                if(file_name.isEmpty()){
                    editText.setError("Enter name");
                    editText.requestFocus();
                }
                else {
                    getPDF();
                }
                break;
        }
    }
}