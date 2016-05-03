package com.example.sj.mylittlepolaroid;

/**
 * Created by SJ on 2015-12-05.
 */
public class InfoClass {
    public int _id;
    public String image;
    public String date;
    public String address;
    public String explain;
    public double x;
    public double y;



    public InfoClass(){}

    public InfoClass(int _id, String image, String date, String address, String explain,double x , double y){
        this._id = _id;
        this.image = image;
        this.date = date;
        this.address = address;
        this.explain = explain;
        this.x=x;
        this.y=y;

    }
}
