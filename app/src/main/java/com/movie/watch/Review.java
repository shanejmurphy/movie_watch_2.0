package com.movie.watch;

import java.sql.Date;
import java.text.DateFormat;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable{
	private String criticName;
	private String publication;
	private String date;
	private String rating;
	private String quote;
	private String link;
	
	public Review(){
		//default constructor required as it is parcelable
	}
	
	public void setCriticName(String name){
		criticName = name;
	}
	
	public String getCriticName(){
		return criticName;
	}
	
	public void setPublication(String publication){
		this.publication = publication;
	}
	
	public String getPublication(){
		return publication;
	}
	
	public void setDate(String date){
		this.date = date;
	}
	
	public String getDate(){
		return date;
	}
	
	public String getLongDate(){
		Date d = Date.valueOf(date);
		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
		return df.format(d);
	}
	
	public void setRating(String rating){
		this.rating = rating;
	}
	
	public String getRating(){
		return rating;
	}
	
	public void setQuote(String quote){
		this.quote = quote;
	}
	
	public String getQuote(){
		return quote;
	}
	
	public void setLink(String link){
		this.link = link;
	}
	
	public String getLink(){
		return link;
	}
	
	//parcelable stuff
	public int describeContents() 
	{
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) 
    {
    	out.writeString(criticName);
        out.writeString(publication);
        out.writeString(date);
        out.writeString(rating);
        out.writeString(quote);
        out.writeString(link);
    }

    public static final Parcelable.Creator<Review> CREATOR
            = new Parcelable.Creator<Review>() {
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public Review(Parcel in) {
       
        criticName = in.readString();
        publication = in.readString();
        date = in.readString();
        rating = in.readString();
        quote = in.readString();
        link = in.readString();
    }
}
