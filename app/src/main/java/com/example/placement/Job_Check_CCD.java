package com.example.placement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
                final List<Job> mJobList = new ArrayList<Job>();

              //  mJobList=new ArrayList<Job>();
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        Job job=documentSnapshot.toObject(Job.class);
                        mJobList.add(job);
                    }
                    ListView mJobView=(ListView) findViewById(R.id.list);
                    Job_adapter mJobAdapter=new Job_adapter(Job_Check_CCD.this,mJobList);
                    mJobView.setAdapter(mJobAdapter);
                    mJobView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Job jobi= mJobList.get(i);
                            Intent intent=new Intent(Job_Check_CCD.this,Job_Particular_CCD.class);
                            intent.putExtra("Document id",jobi.getDocument_id());
                            startActivity(intent);

                        }
                    });
                }
                else{
                    Toast.makeText(Job_Check_CCD.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
