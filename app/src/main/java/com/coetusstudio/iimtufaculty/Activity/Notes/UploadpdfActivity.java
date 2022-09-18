package com.coetusstudio.iimtufaculty.Activity.Notes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.coetusstudio.iimtufaculty.Model.PdfData;
import com.coetusstudio.iimtufaculty.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class UploadpdfActivity extends AppCompatActivity {


    ImageView imagebrowse,filelogo,cancelfile;
    Uri filepath;
    AppCompatButton imageupload;
    TextInputEditText filetitle;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    String facultySection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadpdf);

        Intent intent = getIntent();
        facultySection = intent.getStringExtra("section");

        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Notes");

        filetitle=findViewById(R.id.noticeTitle);

        imagebrowse=findViewById(R.id.imagebrowse);
        imageupload=findViewById(R.id.imageupload);

        filelogo=findViewById(R.id.filelogo);
        cancelfile=findViewById(R.id.cancelfile);

        filelogo.setVisibility(View.INVISIBLE);
        cancelfile.setVisibility(View.INVISIBLE);

        cancelfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filelogo.setVisibility(View.INVISIBLE);
                cancelfile.setVisibility(View.INVISIBLE);
                imagebrowse.setVisibility(View.VISIBLE);
            }
        });


        imagebrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent=new Intent();
                                intent.setType("application/pdf");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent,"Select Pdf Files"),101);
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

        imageupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processupload(filepath);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101 && resultCode==RESULT_OK)
        {
            filepath=data.getData();
            filelogo.setVisibility(View.VISIBLE);
            cancelfile.setVisibility(View.VISIBLE);
            imagebrowse.setVisibility(View.INVISIBLE);
        }
    }


    public void processupload(Uri filepath)
    {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("File Uploading....!!!");
        pd.show();

        final StorageReference reference=storageReference.child("Notes/"+System.currentTimeMillis()+".pdf");
        reference.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                PdfData pdfData=new PdfData(filetitle.getEditableText().toString(),uri.toString(), facultySection);
                                databaseReference.child(facultySection).push().setValue(pdfData);

                                pd.dismiss();
                                Toast.makeText(getApplicationContext(),"File Uploaded",Toast.LENGTH_LONG).show();

                                filelogo.setVisibility(View.INVISIBLE);
                                cancelfile.setVisibility(View.INVISIBLE);
                                imagebrowse.setVisibility(View.VISIBLE);
                                filetitle.setText("");
                            }
                        });

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        float percent=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        pd.setMessage("Uploading :"+(int)percent+"%");
                    }
                });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UploadpdfActivity.this, NotesActivity.class);
        startActivity(intent);
        finish();
    }
}