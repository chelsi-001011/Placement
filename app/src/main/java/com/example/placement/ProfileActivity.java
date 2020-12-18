package com.example.placement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private EditText name,email,phone;
    private FirebaseAuth mAuth;
    private CircleImageView profilePhoto;
    private ImageView save,back;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String new_name;
    private String new_email;
    private String new_phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_profile);
        name=(EditText)findViewById(R.id.name);
        email=(EditText) findViewById(R.id.email);
        phone=(EditText) findViewById(R.id.phone);
        profilePhoto=(CircleImageView) findViewById(R.id.profilephoto);
        save=(ImageView) findViewById(R.id.profile_save);
        back=(ImageView) findViewById(R.id.backprofile);
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
                                        phone.setText(documentSnapshot.getString("phone"));
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