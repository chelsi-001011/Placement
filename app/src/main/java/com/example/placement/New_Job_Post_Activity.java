package com.example.placement;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class New_Job_Post_Activity extends AppCompatActivity {
    private CheckBox cse,mnc,eee,ece,me,civil,cl,cst,bt;
    Vector<String> branchesOffered=new Vector<String>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView post;
    private EditText cpi,position,location,lastDate,description;
   // String mCpi,mPosition,mLocation,mLastDate,mDescription;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job_post);
        cse=(CheckBox) findViewById(R.id.cse);
        mnc=(CheckBox) findViewById(R.id.mnc);
        eee=(CheckBox) findViewById(R.id.eee);
        ece=(CheckBox) findViewById(R.id.ece);
        me=(CheckBox) findViewById(R.id.me);
        civil=(CheckBox) findViewById(R.id.civil);
        cl=(CheckBox ) findViewById(R.id.cl);
        cst=(CheckBox) findViewById(R.id.cst);
        bt=(CheckBox ) findViewById(R.id.bt);
        post=(TextView) findViewById(R.id.post_job);
        cpi=(EditText) findViewById(R.id.cpi);
        position=(EditText) findViewById(R.id.position);
        location=(EditText) findViewById(R.id.location);
        lastDate=(EditText) findViewById(R.id.lastDate);
        description=(EditText) findViewById(R.id.description);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mCpi=cpi.getText().toString();
//                mPosition=position.getText().toString();
//                mLocation=location.getText().toString();
//                mLastDate=lastDate.getText().toString();
//                mDescription=description.getText().toString();
                prepareBranches();
            //    setUpWidgets(mCpi,mPosition,mLocation,mLastDate,mDescription);
             //   final String cpi, String position, String location, String LastDate, String Description
                setUpWidgets();
                Toast.makeText(New_Job_Post_Activity.this, "Posted", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void setUpWidgets(){
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
                                        String mcpi=cpi.getText().toString();
                                        String mposition=position.getText().toString();
                                        String mLocation=location.getText().toString();
                                        String mLastDate=lastDate.getText().toString();
                                        String mDescription=description.getText().toString();
                                        db.collection("applications").document(documentSnapshot.getId()).update("CPI Cutoff",mcpi);
                                        db.collection("applications").document(documentSnapshot.getId()).update("Location",mposition);
                                        db.collection("applications").document(documentSnapshot.getId()).update("Position",mLocation);
                                        db.collection("applications").document(documentSnapshot.getId()).update("lastDate",mLastDate);
                                        db.collection("applications").document(documentSnapshot.getId()).update("Description",mDescription);

                                        break;
                                    }
                                }
                            }catch (NullPointerException e){

                            }

                        }
                        else{
                            Toast.makeText(New_Job_Post_Activity.this, "Error in fetching details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void prepareBranches(){
        try {
            if(cse.isChecked()){
                branchesOffered.add(cse.getText().toString());

            }
            if(mnc.isChecked()){
                branchesOffered.add(mnc.getText().toString());

            }
            if(eee.isChecked()){
                branchesOffered.add(eee.getText().toString());

            }
            if(ece.isChecked()){
                branchesOffered.add(ece.getText().toString());

            }
            if(me.isChecked()){
                branchesOffered.add(me.getText().toString());

            }
            if(civil.isChecked()){
                branchesOffered.add(civil.getText().toString());

            }
            if(cl.isChecked()){
                branchesOffered.add(cl.getText().toString());

            }
            if(cst.isChecked()){
                branchesOffered.add(cst.getText().toString());

            }
            if(bt.isChecked()){
                branchesOffered.add(bt.getText().toString());

            }

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

                                    Map<String,Object> job=new HashMap<>();
                                    String name;
                                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){

                                        //  Toast.makeText(ProfileActivity.this, user_id+" "+documentSnapshot.getString("user_id"), Toast.LENGTH_SHORT).show();
                                        //String given_uid=documentSnapshot.getString("use")
                                        if(documentSnapshot.getString("user_id").equals(user_id)){
                                            name=documentSnapshot.getString("name");
                                            job.put("name",name);
                                            break;
                                        }
                                    }
                                    job.put("branches",branchesOffered);
                                    job.put("user_id",mAuth.getCurrentUser().getUid());
                                    job.put("document_id","");
                                    job.put("CPI Cutoff","");
                                    job.put("Location","");
                                    job.put("Position","");
                                    job.put("lastDate","");
                                    job.put("Description","");
                                    job.put("accepted",false);
                                    db.collection("applications")
                                            .add(job)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

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
                                                                    String id=documentSnapshot.getId();
                                                                    db.collection("applications").document(documentSnapshot.getId()).update("document_id",id);

                                                                    break;
                                                                }
                                                            }
                                                        }catch (NullPointerException e){

                                                        }

                                                    }
                                                    else{
                                                        Toast.makeText(New_Job_Post_Activity.this, "Error in fetching details", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }catch (NullPointerException e){

                                }

                            }
                            else{
                                Toast.makeText(New_Job_Post_Activity.this, "Error in fetching details", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }catch (NullPointerException e){

        }

    }
}
