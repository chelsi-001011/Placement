package com.example.placement;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Job_Particular_CCD extends AppCompatActivity {
    TextView mname,mcpi,mdescription,mlastDate,mposition,mlocation,mBranches,accepted,rejected;
    Button maccept,mreject,isAccepted,isRejected;
    String mDocumentid;
    FirebaseFirestore db;
    DocumentReference note;
    boolean acceptedb,nothingb,rejectedb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_particular_ccd);
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
        accepted.setVisibility(View.GONE);
        rejected.setVisibility(View.GONE);
        maccept.setVisibility(View.GONE);
        mreject.setVisibility(View.GONE);

        mDocumentid=getIntent().getStringExtra("Document id");
        note=db.document("applications/mDocumentid");
        setUpWidgets(mDocumentid);
        maccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accepted.setVisibility(View.VISIBLE);
                rejected.setVisibility(View.GONE);
                maccept.setVisibility(View.GONE);
                mreject.setVisibility(View.GONE);
                acceptedb=true;
                rejectedb=false;
                db.collection("applications").document(mDocumentid).update("accepted",true);
                db.collection("applications").document(mDocumentid).update("rejected",false);
//                db.collection("applications").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @RequiresApi(api = Build.VERSION_CODES.N)
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
//
//                                //  Toast.makeText(ProfileActivity.this, user_id+" "+documentSnapshot.getString("user_id"), Toast.LENGTH_SHORT).show();
//                                //String given_uid=documentSnapshot.getString("use")
//                                if(documentSnapshot.getString("document_id").equals(mDocumentid)){
//                                    db.collection("applications").document(documentSnapshot.getId()).update("accepted",true);
//                                    db.collection("applications").document(documentSnapshot.getId()).update("rejected",false);
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

                db.collection("applications").document(mDocumentid).update("accepted",false);
                db.collection("applications").document(mDocumentid).update("rejected",true);

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
                        if((boolean)documentSnapshot.get("accepted")){
                            accepted.setVisibility(View.VISIBLE);
                            rejected.setVisibility(View.GONE);
                            maccept.setVisibility(View.GONE);
                            mreject.setVisibility(View.GONE);
                        }
                        else if((boolean)documentSnapshot.get("rejected")){
                            accepted.setVisibility(View.GONE);
                            rejected.setVisibility(View.VISIBLE);
                            maccept.setVisibility(View.GONE);
                            mreject.setVisibility(View.GONE);
                        }
                        else{
                            accepted.setVisibility(View.GONE);
                            rejected.setVisibility(View.GONE);
                            maccept.setVisibility(View.VISIBLE);
                            mreject.setVisibility(View.VISIBLE);
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
