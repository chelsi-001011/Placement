package com.example.placement;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class Student_particular_CCD extends AppCompatActivity {
    TextView mname,mcpi,mdescription,mlastDate,mposition,mlocation
            ,mBranches,accepted,rejected,student_name,student_cpi,student_branch,selected;
    Button maccept,mreject,isAccepted,isRejected;
    String mDocumentid ,mUserIdStudent;
    FirebaseFirestore db;
    DocumentReference note;
    boolean acceptedb,nothingb,rejectedb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_application_ccd);
        mname=(TextView) findViewById(R.id.name);
        mcpi=(TextView) findViewById(R.id.cpi_cut);
        mdescription=(TextView) findViewById(R.id.description_job);
        mlastDate=(TextView) findViewById(R.id.lastDate_job);
        mposition=(TextView) findViewById(R.id.position_job);
        mlocation=(TextView) findViewById(R.id.location_job);
        maccept=(Button) findViewById(R.id.accept);
        mreject=(Button) findViewById(R.id.reject);
        db=FirebaseFirestore.getInstance();
        mBranches=(TextView) findViewById(R.id.branches);
        accepted=(TextView) findViewById(R.id.accepted);
        rejected=(TextView) findViewById(R.id.rejected);
        selected=(TextView) findViewById(R.id.selected);
        student_name=(TextView) findViewById(R.id.student_name_detail);
        student_cpi=(TextView) findViewById(R.id.student_cpi_details);
        student_branch=(TextView) findViewById(R.id.student_branch_details);
        accepted.setVisibility(View.GONE);
        rejected.setVisibility(View.GONE);
        maccept.setVisibility(View.GONE);
        mreject.setVisibility(View.GONE);
        selected.setVisibility(View.GONE);

        mDocumentid=getIntent().getStringExtra("Document id company");
        mUserIdStudent=getIntent().getStringExtra("User id student");
        note=db.document("applications/mDocumentid");
        setUpCompanyWidgets(mDocumentid);
        setStudentWidgets(mUserIdStudent);
        setApplicationWidget(mDocumentid,mUserIdStudent);
        maccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accepted.setVisibility(View.VISIBLE);
                rejected.setVisibility(View.GONE);
                maccept.setVisibility(View.GONE);
                mreject.setVisibility(View.GONE);
                acceptedb=true;
                rejectedb=false;

                db.collection("students_applications").document(mUserIdStudent+" "+mDocumentid).update("accepted",true);
//                db.collection("students_applications").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
//
//                                //  Toast.makeText(ProfileActivity.this, user_id+" "+documentSnapshot.getString("user_id"), Toast.LENGTH_SHORT).show();
//                                //String given_uid=documentSnapshot.getString("use")
//                                if(documentSnapshot.getString("document_id_company").equals(mDocumentid)&&
//                                documentSnapshot.getString("user_id_student").equals(mUserIdStudent)){
//
//                                    db.collection("students_applications").document(documentSnapshot.getId()).update("accepted",true);
//
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                });
            }
        });
        mreject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accepted.setVisibility(View.GONE);
                rejected.setVisibility(View.VISIBLE);
                maccept.setVisibility(View.GONE);
                mreject.setVisibility(View.GONE);
                acceptedb=false;
                rejectedb=true;
                db.collection("students_applications").document(mUserIdStudent+" "+mDocumentid).update("rejected",true);
//                db.collection("students_applications").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
//
//                                //  Toast.makeText(ProfileActivity.this, user_id+" "+documentSnapshot.getString("user_id"), Toast.LENGTH_SHORT).show();
//                                //String given_uid=documentSnapshot.getString("use")
//                                if(documentSnapshot.getString("document_id_company").equals(mDocumentid)&&
//                                        documentSnapshot.getString("user_id_student").equals(mUserIdStudent)){
//
//                                    db.collection("students_applications").document(documentSnapshot.getId()).update("rejected",true);
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
    private void setUpCompanyWidgets(final String mDocumentid){
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
//                        if((boolean)documentSnapshot.get("accepted")){
//                            accepted.setVisibility(View.VISIBLE);
//                            rejected.setVisibility(View.GONE);
//                            maccept.setVisibility(View.GONE);
//                            mreject.setVisibility(View.GONE);
//                        }
//                        else if((boolean)documentSnapshot.get("rejected")){
//                            accepted.setVisibility(View.GONE);
//                            rejected.setVisibility(View.VISIBLE);
//                            maccept.setVisibility(View.GONE);
//                            mreject.setVisibility(View.GONE);
//                        }
//                        else{
//                            accepted.setVisibility(View.GONE);
//                            rejected.setVisibility(View.GONE);
//                            maccept.setVisibility(View.VISIBLE);
//                            mreject.setVisibility(View.VISIBLE);
//                        }


                        break;
                    }
                }
            }
        }
    });
    }

    private void setStudentWidgets(final String mUserIdStudent){
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){

                        //  Toast.makeText(ProfileActivity.this, user_id+" "+documentSnapshot.getString("user_id"), Toast.LENGTH_SHORT).show();
                        //String given_uid=documentSnapshot.getString("use")
                        if(documentSnapshot.getString("user_id").equals(mUserIdStudent)){
                            student_name.setText(documentSnapshot.getString("name"));
                            student_cpi.setText(documentSnapshot.getString("cpi"));
                            student_branch.setText(documentSnapshot.getString("branch"));
//                        if((boolean)documentSnapshot.get("accepted")){
//                            accepted.setVisibility(View.VISIBLE);
//                            rejected.setVisibility(View.GONE);
//                            maccept.setVisibility(View.GONE);
//                            mreject.setVisibility(View.GONE);
//                        }
//                        else if((boolean)documentSnapshot.get("rejected")){
//                            accepted.setVisibility(View.GONE);
//                            rejected.setVisibility(View.VISIBLE);
//                            maccept.setVisibility(View.GONE);
//                            mreject.setVisibility(View.GONE);
//                        }
//                        else{
//                            accepted.setVisibility(View.GONE);
//                            rejected.setVisibility(View.GONE);
//                            maccept.setVisibility(View.VISIBLE);
//                            mreject.setVisibility(View.VISIBLE);
//                        }


                            break;
                        }
                    }
                }
            }
        });
    }

    private void setApplicationWidget(final String mDocumentid,final String mUserIdStudent){

        db.collection("students_applications").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){

                        //  Toast.makeText(ProfileActivity.this, user_id+" "+documentSnapshot.getString("user_id"), Toast.LENGTH_SHORT).show();
                        //String given_uid=documentSnapshot.getString("use")
                        if(documentSnapshot.getString("document_id_company").equals(mDocumentid)&&
                                documentSnapshot.getString("user_id_student").equals(mUserIdStudent)){
                             if((boolean)documentSnapshot.get("selected")){
                                accepted.setVisibility(View.GONE);
                                rejected.setVisibility(View.GONE);
                                maccept.setVisibility(View.GONE);
                                mreject.setVisibility(View.GONE);
                                selected.setVisibility(View.VISIBLE);
                            }
                            else if((boolean)documentSnapshot.get("accepted")){
                                accepted.setVisibility(View.VISIBLE);
                                rejected.setVisibility(View.GONE);
                                maccept.setVisibility(View.GONE);
                                mreject.setVisibility(View.GONE);
                                selected.setVisibility(View.GONE);
                            }
                            else if((boolean)documentSnapshot.get("rejected")){
                                accepted.setVisibility(View.GONE);
                                rejected.setVisibility(View.VISIBLE);
                                maccept.setVisibility(View.GONE);
                                mreject.setVisibility(View.GONE);
                                selected.setVisibility(View.GONE);
                            }

                            else{
                                accepted.setVisibility(View.GONE);
                                rejected.setVisibility(View.GONE);
                                maccept.setVisibility(View.VISIBLE);
                                mreject.setVisibility(View.VISIBLE);
                                selected.setVisibility(View.GONE);
                            }
                            break;
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        note.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    return;
                }

                note.update("accepted",acceptedb);
                note.update("rejected",rejectedb);

            }
        });
    }

}
