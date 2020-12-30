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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Student_particular_Company extends AppCompatActivity {
    TextView mname,mcpi,mdescription,mlastDate,mposition,mlocation
            ,mBranches,accepted,rejected,student_name,student_cpi,student_branch;
    Button maccept,mreject,isAccepted,isRejected;
    String mDocumentid ,mUserIdStudent;
    FirebaseFirestore db;
    DocumentReference note;
    boolean acceptedb,nothingb,rejectedb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_application_company);
        maccept=(Button) findViewById(R.id.accept);
        mreject=(Button) findViewById(R.id.reject);
        db=FirebaseFirestore.getInstance();
        accepted=(TextView) findViewById(R.id.accepted);
        rejected=(TextView) findViewById(R.id.rejected);
        student_name=(TextView) findViewById(R.id.student_name_detail);
        student_cpi=(TextView) findViewById(R.id.student_cpi_details);
        student_branch=(TextView) findViewById(R.id.student_branch_details);
        accepted.setVisibility(View.GONE);
        rejected.setVisibility(View.GONE);
        maccept.setVisibility(View.GONE);
        mreject.setVisibility(View.GONE);
        mDocumentid=getIntent().getStringExtra("document_id");
        mUserIdStudent=getIntent().getStringExtra("User id student");
        setStudentWidgets(mUserIdStudent,mDocumentid);
        maccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accepted.setVisibility(View.VISIBLE);
                rejected.setVisibility(View.GONE);
                maccept.setVisibility(View.GONE);
                mreject.setVisibility(View.GONE);
                acceptedb=true;
                rejectedb=false;
                db.collection("students_applications").document(mUserIdStudent+" "+mDocumentid).update("selected",true);
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
//                                    db.collection("students_applications").document(documentSnapshot.getId()).update("selected",true);
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
    private void setStudentWidgets(final String mUserIdStudent, final String mDocumentid){
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
        db.collection("students_applications").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        if(documentSnapshot.getString("document_id_company").equals(mDocumentid)
                                &&documentSnapshot.getString("user_id_student").equals(mUserIdStudent)){
                            if((boolean)documentSnapshot.get("selected")){
                                accepted.setVisibility(View.VISIBLE);
                                rejected.setVisibility(View.GONE);
                                maccept.setVisibility(View.GONE);
                                mreject.setVisibility(View.GONE);
                            }
                            else if((boolean)documentSnapshot.get("rejected")){
                                rejected.setVisibility(View.VISIBLE);
                                accepted.setVisibility(View.GONE);
                                maccept.setVisibility(View.GONE);
                                mreject.setVisibility(View.GONE);
                            }
                            else{
                                rejected.setVisibility(View.GONE);
                                accepted.setVisibility(View.GONE);
                                maccept.setVisibility(View.VISIBLE);
                                mreject.setVisibility(View.VISIBLE);
                            }

                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

}
