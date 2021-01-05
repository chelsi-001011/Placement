package com.example.placement;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Job_adapter extends ArrayAdapter<Job> {
    private LayoutInflater mInflater;
    private int layoutResource;
    private Context mContext;

    public Job_adapter(@NonNull Context context,  @NonNull List<Job> objects) {
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
            convertView=((Activity)getContext()).getLayoutInflater().inflate(R.layout.layout_job,parent,false);
        }
        TextView name=(TextView) convertView.findViewById(R.id.name);
        TextView description=(TextView) convertView.findViewById(R.id.description);
        TextView lastDate=(TextView) convertView.findViewById(R.id.last_date);
        ImageView image=(ImageView) convertView.findViewById(R.id.image);
        ImageView accepted=(ImageView) convertView.findViewById(R.id.accept);
        ImageView not_accepted=(ImageView) convertView.findViewById(R.id.not_accept);
        ImageView rejected=(ImageView) convertView.findViewById(R.id.reject);
        Job job=getItem(position);
        name.setText(job.getName());
        if(job.getCompany_photo()!=null){
            Picasso.get().load(Uri.parse(job.getCompany_photo())).into(image);

        }
        description.setText(job.getDescription());
        lastDate.setText(job.getLastDate());
        if(job.hasImage()){
            image.setImageResource(job.getmImageResourceId());
        }
        if(job.isAccepted()){
            accepted.setVisibility(View.VISIBLE);
            not_accepted.setVisibility(View.GONE);
            rejected.setVisibility(View.GONE);
        }
        else if(job.isRejected()){
            rejected.setVisibility(View.VISIBLE);
            accepted.setVisibility(View.GONE);
            not_accepted.setVisibility(View.GONE);
        }
        else{
            not_accepted.setVisibility(View.VISIBLE);
            accepted.setVisibility(View.GONE);
            rejected.setVisibility(View.GONE);

        }
        return convertView;
    }
}
