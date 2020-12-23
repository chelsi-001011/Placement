package com.example.placement;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class Student_Company_adapter extends ArrayAdapter<Job_Student> {
    private LayoutInflater mInflater;
    private int layoutResource;
    private Context mContext;

    public Student_Company_adapter(@NonNull Context context, @NonNull List<Job_Student> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView=((Activity)getContext()).getLayoutInflater().inflate(R.layout.layout_student,parent,false);
        }
        final TextView name=(TextView) convertView.findViewById(R.id.name);
        final ImageView image=(ImageView) convertView.findViewById(R.id.image);
        ImageView accepted=(ImageView) convertView.findViewById(R.id.accept);
        ImageView rejected=(ImageView) convertView.findViewById(R.id.reject);
        ImageView not_accept=(ImageView) convertView.findViewById(R.id.not_accept);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        not_accept.setVisibility(View.GONE);
        accepted.setVisibility(View.GONE);
        rejected.setVisibility(View.GONE);
        //Job job=getItem(position);
        Job_Student job_student=getItem(position);
        final String user_id=job_student.getUser_id_student();
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()) {
                        if(documentSnapshot.getString("user_id").equals(user_id)){
                            name.setText(documentSnapshot.getString("name"));
                        }
                    }
                }
            }
        });
        if(job_student.hasImage()){
            image.setImageResource(job_student.getmImageResourceId());
        }
        if(job_student.isRejected()){
            rejected.setVisibility(View.VISIBLE);
        }
        else if(job_student.isSelected()){
            accepted.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
}
