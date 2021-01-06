package com.example.placement;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.firebase.storage.FileDownloadTask;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private EditText name,email,phone;
    private FirebaseAuth mAuth;
    private StorageReference storage; // used for uploading files

    private ImageView profilePhoto;
    private ImageView save,back;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // used to store URLs of uploaded files
    private String new_name;
    private String new_email;
    private String new_phone;
    private String new_resume;

    private Button changephoto;
    private final int IMG_REQUEST_ID=10;
    private Uri imgUri;
    //FirebaseStorage fstorage;


    Button selectFile, upload;
    TextView notification;
    Uri pdfUri; // Uri is local storage for URL path
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_profile);
        name = (EditText) findViewById(R.id.name);

        changephoto=(Button) findViewById(R.id.changephoto);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        profilePhoto = (ImageView) findViewById(R.id.profilephoto);

        save = (ImageView) findViewById(R.id.profile_save);
        back = (ImageView) findViewById(R.id.backprofile);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        selectFile = findViewById(R.id.selectFile);
        upload = findViewById(R.id.upload);
        notification = findViewById(R.id.notification);

        db.collection("users");

        changephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_change();
            }
        });


        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectPdf();
                } else {
                    ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pdfUri!=null)
                    uploadFile(pdfUri);
                else
                    Toast.makeText(ProfileActivity.this, "Select a File",Toast.LENGTH_SHORT).show();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveWidgets();
            }
        });
        setUpProfileWidgets();

    }

    private void image_change(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),IMG_REQUEST_ID);
    }



    private void uploadFile(final Uri pdfUri) {

        progressDialog=new ProgressDialog(this);
        progressDialog.setProgress(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgress(0);
        progressDialog.show();

        final String fileName=""+name.getText().toString(); // this is the filename which is the name of the student
        StorageReference storageReference=storage; //returns root paths

        storageReference.child("Uploads").child(fileName).putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String url=taskSnapshot.getStorage().getDownloadUrl().toString();
                db.collection("users") // adding pdfUri's path in the collection
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ProfileActivity.this,"File successfully uploaded",Toast.LENGTH_SHORT).show();
                                    try {
                                        mAuth=FirebaseAuth.getInstance();
                                        FirebaseUser user=mAuth.getCurrentUser();
                                        String user_id=user.getUid();
                                        for(QueryDocumentSnapshot documentSnapshot:task.getResult()){

                                            if(documentSnapshot.getString("user_id").equals(user_id)){
                                                new_resume=pdfUri.getEncodedPath();
                                                db.collection("users").document(documentSnapshot.getId()).update("resume",new_resume);
                                                setUpProfileWidgets();
                                                break;
                                            }
                                        }
                                    }catch (NullPointerException e){

                                    }

                                }
                                else{
                                    Toast.makeText(ProfileActivity.this,"File not uploaded successfully",Toast.LENGTH_SHORT).show();
//                            Toast.makeText(ProfileActivity.this, "Error in fetching details", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ProfileActivity.this,"File not uploaded successfully",Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

//                track the progress of our upload
                int currentProgress=(int)(100* snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            selectPdf();
        }else{
            Toast.makeText(ProfileActivity.this,"please provide permission..",Toast.LENGTH_SHORT).show();
        }

    }
    @SuppressLint("LogConditional")
    private void selectPdf(){
        // to offer user to select a file using file manager

        //we will be using an Intent
        try{
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT); // to fetch files
            startActivityForResult(intent, 86);
        }
        catch (ActivityNotFoundException e)
        {
            Log.d("OpenPDFError", e.getMessage());
        }
    }

    @SuppressLint({"MissingSuperCall", "SetTextI18n"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //check whether user has selected a file or not
        if(requestCode==86 && resultCode==RESULT_OK && data!=null){
            pdfUri=data.getData();
            notification.setText("A file is selected : "+data.getData().getLastPathSegment());
        }

        else if(requestCode==IMG_REQUEST_ID&&resultCode==RESULT_OK&&data!=null
                &&data.getData()!=null ){
            imgUri=data.getData();
            try {
                Bitmap bitmapImg= MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
                profilePhoto.setImageBitmap(bitmapImg);
                if(imgUri!=null){
                    final ProgressDialog progressDialog=new ProgressDialog(this);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.show();
                    final StorageReference reference=storage.child("pictures").child(mAuth.getCurrentUser().getUid().toString());
                    try{
                        reference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {progressDialog.dismiss();
                                Toast.makeText(ProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                changephoto.setVisibility(View.VISIBLE);
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final Uri firebaseUri=uri;
                                        db.collection("users")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if(task.isSuccessful()){
                                                            try {
                                                                mAuth=FirebaseAuth.getInstance();
                                                                FirebaseUser user=mAuth.getCurrentUser();
                                                                String user_id=user.getUid();
                                                                for(QueryDocumentSnapshot documentSnapshot:task.getResult()){

                                                                    //  Toast.makeText(ProfileActivity.this, user_id+" "+documentSnapshot.getString("user_id"), Toast.LENGTH_SHORT).show();
                                                                    //String given_uid=documentSnapshot.getString("use")
                                                                    if(documentSnapshot.getString("user_id").equals(user_id)){

                                                                        db.collection("users").document(documentSnapshot.getId()).update("photo",firebaseUri.toString());
                                                                        setUpProfileWidgets();
                                                                        break;
                                                                    }
                                                                }
                                                            }catch (NullPointerException e){

                                                            }

                                                        }
                                                        else{
                                                            Toast.makeText(ProfileActivity.this, "Error in fetching details", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        db.collection("applications")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if(task.isSuccessful()){
                                                            try {
                                                                mAuth=FirebaseAuth.getInstance();
                                                                FirebaseUser user=mAuth.getCurrentUser();
                                                                String user_id=user.getUid();
                                                                for(QueryDocumentSnapshot documentSnapshot:task.getResult()){

                                                                    //  Toast.makeText(ProfileActivity.this, user_id+" "+documentSnapshot.getString("user_id"), Toast.LENGTH_SHORT).show();
                                                                    //String given_uid=documentSnapshot.getString("use")
                                                                    if(documentSnapshot.getString("user_id").equals(user_id)){

                                                                        db.collection("applications").document(documentSnapshot.getId()).update("company_photo",firebaseUri.toString());


                                                                    }
                                                                }
                                                            }catch (NullPointerException e){

                                                            }

                                                        }
                                                        else{
                                                            Toast.makeText(ProfileActivity.this, "Error in fetching details", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        db.collection("students_applications")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if(task.isSuccessful()){
                                                            try {
                                                                mAuth=FirebaseAuth.getInstance();
                                                                FirebaseUser user=mAuth.getCurrentUser();
                                                                String user_id=user.getUid();
                                                                for(QueryDocumentSnapshot documentSnapshot:task.getResult()){

                                                                    //  Toast.makeText(ProfileActivity.this, user_id+" "+documentSnapshot.getString("user_id"), Toast.LENGTH_SHORT).show();
                                                                    //String given_uid=documentSnapshot.getString("use")
                                                                    if(documentSnapshot.getString("user_id_company").equals(user_id)){

                                                                        db.collection("students_applications").document(documentSnapshot.getId()).update("company_photo",firebaseUri.toString());


                                                                    }
                                                                }
                                                            }catch (NullPointerException e){

                                                            }

                                                        }
                                                        else{
                                                            Toast.makeText(ProfileActivity.this, "Error in fetching details", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                });

                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                        double progress=(100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                                        //  Toast.makeText(ProfileActivity.this, , Toast.LENGTH_SHORT).show();
                                        progressDialog.setMessage("Saved "+(int)progress+" %");
                                        changephoto.setVisibility(View.GONE);
                                    }
                                });
                    }catch (Exception e){
                    }
                }

            }
            catch (IOException e){

            }
        }
        else {
            Toast.makeText(ProfileActivity.this,"Please select A file",Toast.LENGTH_SHORT).show();
        }


    }

    private void saveWidgets(){
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            try {
                                mAuth=FirebaseAuth.getInstance();
                                FirebaseUser user=mAuth.getCurrentUser();
                                String user_id=user.getUid();
                                for(QueryDocumentSnapshot documentSnapshot:task.getResult()){

                                    //  Toast.makeText(ProfileActivity.this, user_id+" "+documentSnapshot.getString("user_id"), Toast.LENGTH_SHORT).show();
                                    //String given_uid=documentSnapshot.getString("use")
                                    if(documentSnapshot.getString("user_id").equals(user_id)){

                                        new_name=name.getText().toString();
                                        db.collection("users").document(documentSnapshot.getId()).update("name",new_name);
                                        new_email=email.getText().toString();
                                        db.collection("users").document(documentSnapshot.getId()).update("email",new_email);

                                        new_phone=phone.getText().toString();
                                        db.collection("users").document(documentSnapshot.getId()).update("phone",new_phone);


                                        setUpProfileWidgets();
                                        Toast.makeText(ProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                }
                            }catch (NullPointerException e){

                            }

                        }
                        else{
                            Toast.makeText(ProfileActivity.this, "Error in fetching details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        db.collection("applications")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            try {
                                mAuth=FirebaseAuth.getInstance();
                                FirebaseUser user=mAuth.getCurrentUser();
                                String user_id=user.getUid();
                                for(QueryDocumentSnapshot documentSnapshot:task.getResult()){

                                    //  Toast.makeText(ProfileActivity.this, user_id+" "+documentSnapshot.getString("user_id"), Toast.LENGTH_SHORT).show();
                                    //String given_uid=documentSnapshot.getString("use")
                                    if(documentSnapshot.getString("user_id").equals(user_id)){

                                        new_name=name.getText().toString();
                                        db.collection("applications").document(documentSnapshot.getId()).update("name",new_name);
                                        new_email=email.getText().toString();
                                        db.collection("applications").document(documentSnapshot.getId()).update("email",new_email);

                                    }
                                }
                            }catch (NullPointerException e){

                            }

                        }
                        else{
                            Toast.makeText(ProfileActivity.this, "Error in fetching details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });




    }
    private void setUpProfileWidgets(){
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            try {
                                mAuth=FirebaseAuth.getInstance();
                                FirebaseUser user=mAuth.getCurrentUser();
                                String user_id=user.getUid();
                                for(QueryDocumentSnapshot documentSnapshot:task.getResult()){

                                    //  Toast.makeText(ProfileActivity.this, user_id+" "+documentSnapshot.getString("user_id"), Toast.LENGTH_SHORT).show();
                                    //String given_uid=documentSnapshot.getString("use")
                                    if(documentSnapshot.getString("user_id").equals(user_id)){
                                        //    Toast.makeText(ProfileActivity.this, documentSnapshot.toString(), Toast.LENGTH_SHORT).show();
                                        //   Toast.makeText(ProfileActivity.this, "yeah", Toast.LENGTH_SHORT).show();
                                        name.setText(documentSnapshot.getString("name"));
                                        // Toast.makeText(ProfileActivity.this, documentSnapshot.getId(), Toast.LENGTH_SHORT).show();
                                        email.setText(documentSnapshot.getString("email"));
                                        Picasso.get().load(Uri.parse(documentSnapshot.getString("photo"))).into(profilePhoto);
                                        phone.setText(documentSnapshot.getString("phone"));
                                        notification.setText(documentSnapshot.getString("resume"));



                                        break;
                                    }
                                }
                            }catch (NullPointerException e){

                            }

                        }
                        else{
                            Toast.makeText(ProfileActivity.this, "Error in fetching details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


}