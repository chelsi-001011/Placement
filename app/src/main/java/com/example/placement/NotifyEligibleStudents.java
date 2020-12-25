package com.example.placement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotifyEligibleStudents extends Fragment {
    private ListView mJobView;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    String cpi,branch;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_job_post);
//        mJobView=(ListView) findViewById(R.id.list);
//        db=FirebaseFirestore.getInstance();
//        mAuth=FirebaseAuth.getInstance();
//        final String userid=mAuth.getCurrentUser().getUid();
//        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
//                        if(documentSnapshot.getString("user_id").equals(userid)){
//                            branch=documentSnapshot.getString("branch");
//                            db.collection("applications").get()
//                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                            if(task.isSuccessful()){
//                                                for (final QueryDocumentSnapshot document : task.getResult()) {
//
//                                                    final String sname=document.getString("name");
//                                                    if((boolean)document.get("accepted")){
//                                                        DocumentReference note=db.collection("students_applications").document(userid+" "+document.getString("document_id"));
//                                                        note.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                                            @Override
//                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                                if(task.isSuccessful()){
//                                                                    DocumentSnapshot documentSnapshot1=task.getResult();
//                                                                    if(documentSnapshot1.exists()){
//
//                                                                    }
//                                                                    else{
//                                                                        ArrayList<String> br=(ArrayList<String>)document.get("branches");
//                                                                        String document_id=document.getString("document_id");
//                                                                        for(int i=0;i<br.size();i++){
//                                                                            if(branch.equals(br.get(i))){
//                                                                                Map<String,Object> note=new HashMap<>();
//                                                                                note.put("name",sname);
//                                                                                note.put("user_id_student",userid);
//                                                                                note.put("document_id_company",document_id);
//                                                                                note.put("selected",false);
//                                                                                note.put("applied",false);
//                                                                                note.put("user_id_company",document.getString("user_id"));
//                                                                                note.put("lastDate",document.getString("lastDate"));
//                                                                                note.put("Description",document.getString("Description"));
//                                                                                note.put("rejected",false);
//                                                                                note.put("resume","");
//                                                                                note.put("accepted",false);
//                                                                                db.collection("students_applications")
//                                                                                        .document(userid+" "+document_id).set(note)
//                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                                                            @Override
//                                                                                            public void onSuccess(Void aVoid) {
//
//                                                                                            }
//                                                                                        })
//                                                                                        .addOnFailureListener(new OnFailureListener() {
//                                                                                            @Override
//                                                                                            public void onFailure(@NonNull Exception e) {
//
//                                                                                            }
//                                                                                        });
//
//
//                                                                                break;
//                                                                            }
//                                                                        }
//                                                                    }
//                                                                }
//                                                            }
//                                                        });
//
//                                                    }
//
//                                                }
//                                            }
//                                        }
//                                    });
//                            db.collection("students_applications").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                    final List<Job_Student> mJobList = new ArrayList<Job_Student>();
//
//                                    //  mJobList=new ArrayList<Job>();
//                                    if(task.isSuccessful()){
//                                        for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
//                                            Job_Student job_student=documentSnapshot.toObject(Job_Student.class);
//                                            if(job_student.getUser_id_student().equals(userid)){
//                                                mJobList.add(job_student);
//                                            }
//                                        }
//                                        ListView mJobView=(ListView) findViewById(R.id.list);
//                                        Job_student_adapter mJobAdapter=new Job_student_adapter(NotifyEligibleStudents.this,mJobList);
//                                        mJobView.setAdapter(mJobAdapter);
//                                        mJobView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                            @Override
//                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                                Job_Student jobi= mJobList.get(i);
//                                                Intent intent=new Intent(NotifyEligibleStudents.this,Job_Particular_Student.class);
//                                                intent.putExtra("Document id",jobi.getDocument_id_company());
//                                                startActivity(intent);
//
//                                            }
//                                        });
//                                    }
//                                    else{
//                                        Toast.makeText(NotifyEligibleStudents.this, "error", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                            break;
//                        }
//                    }
//
//                }
//            }
//        });
//
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view=inflater.inflate(R.layout.activity_job_post,container,false);
        mJobView=(ListView) view.findViewById(R.id.list);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        final String userid=mAuth.getCurrentUser().getUid();
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        if(documentSnapshot.getString("user_id").equals(userid)){
                            branch=documentSnapshot.getString("branch");
                            db.collection("applications").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){
                                                for (final QueryDocumentSnapshot document : task.getResult()) {

                                                    final String sname=document.getString("name");
                                                    if((boolean)document.get("accepted")){
                                                        DocumentReference note=db.collection("students_applications").document(userid+" "+document.getString("document_id"));
                                                        note.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if(task.isSuccessful()){
                                                                    DocumentSnapshot documentSnapshot1=task.getResult();
                                                                    if(documentSnapshot1.exists()){

                                                                    }
                                                                    else{
                                                                        ArrayList<String> br=(ArrayList<String>)document.get("branches");
                                                                        String document_id=document.getString("document_id");
                                                                        for(int i=0;i<br.size();i++){
                                                                            if(branch.equals(br.get(i))){
                                                                                Map<String,Object> note=new HashMap<>();
                                                                                note.put("name",sname);
                                                                                note.put("user_id_student",userid);
                                                                                note.put("document_id_company",document_id);
                                                                                note.put("selected",false);
                                                                                note.put("applied",false);
                                                                                note.put("user_id_company",document.getString("user_id"));
                                                                                note.put("lastDate",document.getString("lastDate"));
                                                                                note.put("Description",document.getString("Description"));
                                                                                note.put("rejected",false);
                                                                                note.put("resume","");
                                                                                note.put("accepted",false);
                                                                                db.collection("students_applications")
                                                                                        .document(userid+" "+document_id).set(note)
                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {

                                                                                            }
                                                                                        })
                                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                                            @Override
                                                                                            public void onFailure(@NonNull Exception e) {

                                                                                            }
                                                                                        });
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        });

                                                    }

                                                }
                                            }
                                        }
                                    });
                            db.collection("students_applications").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    final List<Job_Student> mJobList = new ArrayList<Job_Student>();

                                    //  mJobList=new ArrayList<Job>();
                                    if(task.isSuccessful()){
                                        for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                            Job_Student job_student=documentSnapshot.toObject(Job_Student.class);
                                            if(job_student.getUser_id_student().equals(userid)){
                                                mJobList.add(job_student);
                                            }
                                        }
                                        ListView mJobView=(ListView) view.findViewById(R.id.list);
                                        Job_student_adapter mJobAdapter=new Job_student_adapter(getActivity(),mJobList);
                                        mJobView.setAdapter(mJobAdapter);
                                        mJobView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Job_Student jobi= mJobList.get(i);
                                                Intent intent=new Intent(getActivity(),Job_Particular_Student.class);
                                                intent.putExtra("Document id",jobi.getDocument_id_company());
                                                startActivity(intent);

                                            }
                                        });
                                    }
                                    else{
                                        Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            break;
                        }
                    }

                }
            }
        });
        return view;
    }
}
