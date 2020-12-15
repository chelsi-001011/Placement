package com.example.placement;

public class Job {
    private String name;
    private int mImageResourceId=NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED=-1;
    private String lastDate;
    private boolean accepted;
    private String Description;

    public Job(String name, int mImageResourceId, String lastDate, boolean accepted, String description) {
        this.name = name;
        this.mImageResourceId = mImageResourceId;
        this.lastDate = lastDate;
        this.accepted = accepted;
        Description = description;
    }

    public Job() {

    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getmImageResourceId() {
        return mImageResourceId;
    }

    public void setmImageResourceId(int mImageResourceId) {
        this.mImageResourceId = mImageResourceId;
    }

    public static int getNoImageProvided() {
        return NO_IMAGE_PROVIDED;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean hasImage(){
        return mImageResourceId!=NO_IMAGE_PROVIDED;
    }
}
