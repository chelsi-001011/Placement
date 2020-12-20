package com.example.placement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.placement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    private Button login;
    private ImageView profile,create,job,apply;
    private FirebaseAuth mAuth;
    String type ="";
    // int x;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login=(Button) findViewById(R.id.loginbtn);
        mAuth = FirebaseAuth.getInstance();
        profile=(ImageView) findViewById(R.id.myProfile);
        create=(ImageView) findViewById(R.id.create);
        create.setVisibility(View.GONE);
        job=(ImageView) findViewById(R.id.job);
        job.setVisibility(View.GONE);

        apply=(ImageView) findViewById(R.id.apply);
        apply.setVisibility(View.GONE);
        isCompany();
        isCCD();
        isStudent();
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,New_Job_Post_Activity.class);
                startActivity(intent);
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,NotifyEligibleStudents.class);
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SignOutActivity.class);
                startActivity(intent);
            }
        });
        job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Job_Check_CCD.class);
                startActivity(intent);
            }
        });
    }

    private void isCompany(){
        Task<QuerySnapshot> querySnapshotTask = db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    int x = 0;
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            try {
                                mAuth = FirebaseAuth.getInstance();
                                FirebaseUser user = mAuth.getCurrentUser();
                                String user_id = user.getUid();
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                    //  Toast.makeText(ProfileActivity.this, user_id+" "+documentSnapshot.getString("user_id"), Toast.LENGTH_SHORT).show();
                                    //String given_uid=documentSnapshot.getString("use")
                                    if (documentSnapshot.getString("user_id").equals(user_id)) {
                                        type = documentSnapshot.getString("type");
                                        if (type.equals("Company")) {
                                            create.setVisibility(View.VISIBLE);
                                            break;
                                        }
                                    }
                                }
                            } catch (NullPointerException e) {
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Error in fetching details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void isCCD(){
        Task<QuerySnapshot> querySnapshotTask = db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    int x = 0;
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            try {
                                mAuth = FirebaseAuth.getInstance();
                                FirebaseUser user = mAuth.getCurrentUser();
                                String user_id = user.getUid();
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                    //  Toast.makeText(ProfileActivity.this, user_id+" "+documentSnapshot.getString("user_id"), Toast.LENGTH_SHORT).show();
                                    //String given_uid=documentSnapshot.getString("use")
                                    if (documentSnapshot.getString("user_id").equals(user_id)) {
                                        type = documentSnapshot.getString("type");
                                        if (type.equals("CCD")) {
                                            job.setVisibility(View.VISIBLE);
                                            break;
                                        }
                                    }
                                }
                            } catch (NullPointerException e) {
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Error in fetching details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void isStudent(){
        Task<QuerySnapshot> querySnapshotTask = db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    int x = 0;
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            try {
                                mAuth = FirebaseAuth.getInstance();
                                FirebaseUser user = mAuth.getCurrentUser();
                                String user_id = user.getUid();
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                    //  Toast.makeText(ProfileActivity.this, user_id+" "+documentSnapshot.getString("user_id"), Toast.LENGTH_SHORT).show();
                                    //String given_uid=documentSnapshot.getString("use")
                                    if (documentSnapshot.getString("user_id").equals(user_id)) {
                                        type = documentSnapshot.getString("type");
                                        if (type.equals("Student")) {
                                            apply.setVisibility(View.VISIBLE);
                                            break;
                                        }
                                    }
                                }
                            } catch (NullPointerException e) {
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Error in fetching details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser!=null){

        }
        else{
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}