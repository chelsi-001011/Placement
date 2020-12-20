package com.example.placement;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class Job_student_adapter extends ArrayAdapter<Job_Student> {
    private LayoutInflater mInflater;
    private int layoutResource;
    private Context mContext;

//    public Job_student_adapter(@NonNull Context context,  @NonNull List<Job> objects) {
//        super(context, 0, objects);
//    }

    public Job_student_adapter(@NonNull Context context, @NonNull List<Job_Student> objects) {
        super(context, 0, objects);
    }


    //    public Job_adapter(@NonNull Context context, int resource, @NonNull List<Job> objects) {
//        super(context, resource, objects);
//        mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        mContext=context;
//        layoutResource=resource;
//    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView=((Activity)getContext()).getLayoutInflater().inflate(R.layout.layout_job_student,parent,false);
        }
        TextView name=(TextView) convertView.findViewById(R.id.name);
        TextView description=(TextView) convertView.findViewById(R.id.description);
        TextView lastDate=(TextView) convertView.findViewById(R.id.last_date);
        ImageView image=(ImageView) convertView.findViewById(R.id.image);
        ImageView accepted=(ImageView) convertView.findViewById(R.id.accepted);
        ImageView applied=(ImageView) convertView.findViewById(R.id.applied);
        ImageView rejected=(ImageView) convertView.findViewById(R.id.rejected);
        //Job job=getItem(position);
        Job_Student job_student=getItem(position);
        name.setText(job_student.getName());
        description.setText(job_student.getDescription());
        lastDate.setText(job_student.getLastDate());
        if(job_student.hasImage()){
            image.setImageResource(job_student.getmImageResourceId());
        }

         if(job_student.isRejected()){
            rejected.setVisibility(View.VISIBLE);
            accepted.setVisibility(View.GONE);
            applied.setVisibility(View.GONE);
        }
         else if(job_student.isSelected()){
            rejected.setVisibility(View.GONE);
            accepted.setVisibility(View.VISIBLE);
            applied.setVisibility(View.GONE);
        }
        else if(job_student.isApplied()){
            accepted.setVisibility(View.GONE);
            applied.setVisibility(View.VISIBLE);
            rejected.setVisibility(View.GONE);
        }
        else{
            applied.setVisibility(View.GONE);
            accepted.setVisibility(View.GONE);
            rejected.setVisibility(View.GONE);

        }
        return convertView;
    }
}
