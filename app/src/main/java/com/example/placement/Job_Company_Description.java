package com.example.placement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Job_Company_Description extends AppCompatActivity {
    TextView mname,mcpi,mdescription,mlastDate,mposition,mlocation,mBranches,accepted,rejected,students;
    Button maccept,mreject,isAccepted,isRejected;
    String mDocumentid;
    FirebaseFirestore db;
    DocumentReference note;
    boolean acceptedb,nothingb,rejectedb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_company_descripiton);
        mname=(TextView) findViewById(R.id.name);
        mcpi=(TextView) findViewById(R.id.cpi_cut);
        mdescription=(TextView) findViewById(R.id.description_job);
        mlastDate=(TextView) findViewById(R.id.lastDate_job);
        mposition=(TextView) findViewById(R.id.position_job);
        mlocation=(TextView) findViewById(R.id.location_job);
        db=FirebaseFirestore.getInstance();
        mBranches=(TextView) findViewById(R.id.branches);
        students=(TextView) findViewById(R.id.applied_students);


        mDocumentid=getIntent().getStringExtra("Document id");
        setUpWidgets(mDocumentid);
        students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Job_Company_Description.this,Student_Application_check_Company.class);
                intent.putExtra("document id",mDocumentid);
                startActivity(intent);

            }
        });
    }
    private void setUpWidgets(final String mDocumentid){
    db.collection("applications").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot documentSnapshot:task.getResult()){

                    //  Toast.makeText(ProfileActivity.this, user_id+" "+documentSnapshot.getString("user_id"), Toast.LENGTH_SHORT).show();
                    //String given_uid=documentSnapshot.getString("use")
                    if(documentSnapshot.getString("document_id").equals(mDocumentid)){
                        mname.setText(documentSnapshot.getString("name"));
                        mdescription.setText(documentSnapshot.getString("Description"));
                        mlocation.setText(documentSnapshot.getString("Location"));
                        mposition.setText(documentSnapshot.getString("Position"));
                        mcpi.setText(documentSnapshot.getString("CPI Cutoff"));
                        mlastDate.setText(documentSnapshot.getString("lastDate"));
                        ArrayList<String > b= (ArrayList<String>) documentSnapshot.get("branches");
                        String uu="";
                        for(int i=0;i<b.size();i++){
                            uu=uu+"\n"+ b.get(i);
                        }
                        mBranches.setText(uu);
                        break;
                    }
                }
            }
        }
    });
    }
//    public void setUpWidgets(String name,String cpi,String description,String lastDate,String position,String location,boolean accept,boolean reject){
//
//    }

    @Override
    protected void onStart() {
        super.onStart();

    }

}
