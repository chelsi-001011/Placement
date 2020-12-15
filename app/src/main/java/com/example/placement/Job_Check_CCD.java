package com.example.placement;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Job_Check_CCD extends AppCompatActivity {
    private ListView mJobView;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_post);
        mJobView=(ListView) findViewById(R.id.list);
        db=FirebaseFirestore.getInstance();
        db.collection("applications").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Job> mJobList = new ArrayList<>();

                mJobList=new ArrayList<Job>();
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        Job job=documentSnapshot.toObject(Job.class);
                        mJobList.add(job);
                    }
                    ListView mJobView=(ListView) findViewById(R.id.list);
                    Job_adapter mJobAdapter=new Job_adapter(Job_Check_CCD.this,mJobList);
                    mJobView.setAdapter(mJobAdapter);
                }
                else{
                    Toast.makeText(Job_Check_CCD.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
