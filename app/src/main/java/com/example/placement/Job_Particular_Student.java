package com.example.placement;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Job_Particular_Student extends AppCompatActivity {
    TextView mname,mcpi,mdescription,mlastDate,mposition,mlocation,mBranches,applied,rejected,selected;
    Button maccept;
    String mDocumentid;
    FirebaseFirestore db;
    DocumentReference note;
    boolean appliedb,rejectedb;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_particular_student);
        mAuth=FirebaseAuth.getInstance();
        mname=(TextView) findViewById(R.id.name);
        mcpi=(TextView) findViewById(R.id.cpi_cut);
        mdescription=(TextView) findViewById(R.id.description_job);
        mlastDate=(TextView) findViewById(R.id.lastDate_job);
        mposition=(TextView) findViewById(R.id.position_job);
        mlocation=(TextView) findViewById(R.id.location_job);
        maccept=(Button) findViewById(R.id.apply);
        db=FirebaseFirestore.getInstance();
        mBranches=(TextView) findViewById(R.id.branches);
        applied=(TextView) findViewById(R.id.applied);
        rejected=(TextView) findViewById(R.id.rejected);
        selected=(TextView) findViewById(R.id.selected);
        applied.setVisibility(View.GONE);
        rejected.setVisibility(View.GONE);
        maccept.setVisibility(View.GONE);
        selected.setVisibility(View.GONE);
        mDocumentid=getIntent().getStringExtra("Document id");
        setUpWidgets(mDocumentid);
        maccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applied.setVisibility(View.VISIBLE);
                rejected.setVisibility(View.GONE);
                maccept.setVisibility(View.GONE);
                selected.setVisibility(View.GONE);

                db.collection("students_applications").document(mAuth.getCurrentUser().getUid()+" "+mDocumentid).update("applied",true);
//                db.collection("students_applications").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
//
//                                //  Toast.makeText(ProfileActivity.this, user_id+" "+documentSnapshot.getString("user_id"), Toast.LENGTH_SHORT).show();
//                                //String given_uid=documentSnapshot.getString("use")
//                                if(documentSnapshot.getString("document_id_company").equals(mDocumentid)&&documentSnapshot.getString("user_id_student").equals(mAuth.getCurrentUser().getUid())){
//
//                                    db.collection("students_applications").document(documentSnapshot.getId()).update("applied",true);
//
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                });
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
        db.collection("students_applications").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){

                        //  Toast.makeText(ProfileActivity.this, user_id+" "+documentSnapshot.getString("user_id"), Toast.LENGTH_SHORT).show();
                        //String given_uid=documentSnapshot.getString("use")
                        if(documentSnapshot.getString("document_id_company").equals(mDocumentid)&&documentSnapshot.getString("user_id_student").equals(mAuth.getCurrentUser().getUid())){
                            System.out.println(documentSnapshot.get("applied"));
                            if((boolean)documentSnapshot.get("selected")){
                                applied.setVisibility(View.GONE);
                                rejected.setVisibility(View.GONE);
                                maccept.setVisibility(View.GONE);
                                selected.setVisibility(View.VISIBLE);
                            }
                            else if((boolean)documentSnapshot.get("rejected")){
                                applied.setVisibility(View.GONE);
                                rejected.setVisibility(View.VISIBLE);
                                maccept.setVisibility(View.GONE);
                                selected.setVisibility(View.GONE);
                            }
                            else if((boolean)documentSnapshot.get("applied")){
                                applied.setVisibility(View.VISIBLE);
                                rejected.setVisibility(View.GONE);
                                maccept.setVisibility(View.GONE);
                                selected.setVisibility(View.GONE);
                            }
                            else{
                                applied.setVisibility(View.GONE);
                                rejected.setVisibility(View.GONE);
                                maccept.setVisibility(View.VISIBLE);
                                selected.setVisibility(View.GONE);
                            }
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
