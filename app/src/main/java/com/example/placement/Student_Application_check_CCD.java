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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Student_Application_check_CCD extends Fragment {
    private ListView mJobView;
    private FirebaseFirestore db;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_job_post);
//        mJobView=(ListView) findViewById(R.id.list);
//        db=FirebaseFirestore.getInstance();
//        db.collection("students_applications").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                final List<Job_Student> mJobList = new ArrayList<Job_Student>();
//
//                //  mJobList=new ArrayList<Job>();
//                if(task.isSuccessful()){
//                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
//                        Job_Student job_student=documentSnapshot.toObject(Job_Student.class);
//                        if(job_student.isApplied()&&!job_student.isRejected()){
//                            mJobList.add(job_student);
//
//                        }
//                    }
//                    ListView mJobView=(ListView) findViewById(R.id.list);
//                    Student_Check_CCD_adapter mJobAdapter=new Student_Check_CCD_adapter(Student_Application_check_CCD.this,mJobList);
//                    mJobView.setAdapter(mJobAdapter);
//                    mJobView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                            Job_Student jobi= mJobList.get(i);
//                         //   Toast.makeText(Student_Application_check_CCD.this, jobi.getDocument_id_company(), Toast.LENGTH_SHORT).show();
//                            Intent intent=new Intent(Student_Application_check_CCD.this, Student_particular_CCD.class);
//                            intent.putExtra("Document id company",jobi.getDocument_id_company());
//                            intent.putExtra("User id student",jobi.getUser_id_student());
//                            startActivity(intent);
//
//                        }
//                    });
//                }
//                else{
//                    Toast.makeText(Student_Application_check_CCD.this, "error", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.activity_job_post,container,false);
        mJobView=(ListView) view.findViewById(R.id.list);
        db=FirebaseFirestore.getInstance();
        db.collection("students_applications").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final List<Job_Student> mJobList = new ArrayList<Job_Student>();

                //  mJobList=new ArrayList<Job>();
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        Job_Student job_student=documentSnapshot.toObject(Job_Student.class);
                        if(job_student.isApplied()&&!job_student.isRejected()){
                            mJobList.add(job_student);

                        }
                    }
                    ListView mJobView=(ListView) view.findViewById(R.id.list);
                    Student_Check_CCD_adapter mJobAdapter=new Student_Check_CCD_adapter(getActivity(),mJobList);
                    mJobView.setAdapter(mJobAdapter);
                    mJobView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Job_Student jobi= mJobList.get(i);
                            //   Toast.makeText(Student_Application_check_CCD.this, jobi.getDocument_id_company(), Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getActivity(), Student_particular_CCD.class);
                            intent.putExtra("Document id company",jobi.getDocument_id_company());
                            intent.putExtra("User id student",jobi.getUser_id_student());
                            startActivity(intent);

                        }
                    });
                }
                else{
                    Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;

    }
}
