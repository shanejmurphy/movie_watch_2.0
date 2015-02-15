package com.movie.watch;

import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


public class Movie implements Parcelable
{
	private String status;
	private String id;
	private String title;
	private int year;
	private String rating;
	private int runtime;
	private String releaseDate;
	private String synopsis;
	
	private String thumbnailImg;
	private Bitmap thumbnailPoster;
	private String profileImg;
	private Bitmap profilePoster;

	private String consensus;
	private String criticRating;
	private int criticScore;	
	private String audienceRating;
	private int audienceScore;
	
	private List<String> cast;
	
	private ArrayList<Review> reviews;
	
	private ArrayList<Movie> similarMovies;
	
	private ArrayList<String> alternativeTitles;
	
	private String videoLink;
	private String link;
	
	private String imdbId;
	
	private ArrayList<Showtimes> showtimes;
	
	//constructor
	public Movie(){
		reviews = new ArrayList<Review>();
	}
	
	/*
	 * Accessor Methods
	 */
	
	//getters and setters for Id
	public void setId(String idNum)
	{
		id = idNum;
	}
	public String getId()
	{
		return id;
	}
	
	public void setStatus(String status)
	{
		this.status = status;
	}
	public String getStatus()
	{
		return status;
	}
	
	//getters and setters for title
	public void setTitle(String filmTitle)
	{
		title = filmTitle;
	}
	public String getTitle()
	{
		return title;
	}
	
	//getters and setters for year
	public void setYear(int year)
	{
		this.year = year;
	}
	public int getYear()
	{
		return year;
	}
	
	//getters and setters for rating
	public void setRating(String rating)
	{
		this.rating = rating;
	}
	public String getRating()
	{
		return rating;
	}
	
	//getters and setters for runtime
	public void setRuntime(int runtime){
		this.runtime = runtime;
	}
	public int getRuntime(){
		return runtime;
	}
	
	public String getLongRuntime(){
		int hours = 0;
		int mins = runtime%60;
		if(runtime > 59){
			hours = runtime/60;
			return hours + " hours " + mins + " minutes";
		}
		return mins + " minutes";
	}
	
	//getters and setters for releaesedat
	public void setReleaseDate(String date){
		releaseDate = date;
	}
	public String getReleaseDate(){
		return releaseDate;
	}
	
	public String getLongReleaseDate(){
		Date d = Date.valueOf(releaseDate);
		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
		return df.format(d);
	}
	
	//getters and setters for synopsis
	public void setSynopsis(String synopsis){
		this.synopsis = synopsis;
	}
	public String getSynopsis(){
		return synopsis;
	}
	
	//getters and setters for thumbnail image string
	public void setThumbnailImg(String imgLoc){
		thumbnailImg = imgLoc;
	}
	public String getThumbnailImg(){
		return thumbnailImg;
	}
	
	//getters and setters for profile image
	public void setProfileImg(String imgLoc){
		profileImg = imgLoc;
	}
	public String getProfileImg(){
		return profileImg;
	}
	
	//getters and setters for consensus
	public void setConsensus(String consensus){
		this.consensus = consensus;
	}
	public String getConsensus(){
		return consensus;
	}
	
	//getters and setters for critic rating
	public void setCriticRating(String rating){
		criticRating = rating;
	}
	public String getCriticRating(){
		return criticRating;
	}
	
	//getters and setters for critic score
	public void setCriticScore(int score){
		criticScore = score;
	}
	public int getCriticScore(){
		return criticScore;
	}
	
	//getters and setters for audience rating
	public void setAudienceRating(String rating){
		audienceRating = rating;
	}
	public String getAudienceRating(){
		return audienceRating;
	}
	
	//getters and setters for score
	public void setAudienceScore(int score){
		audienceScore = score;
	}
	public int getAudienceScore(){
		return audienceScore;
	}
	
	public void setReviews(ArrayList<Review> r){
		reviews = r;
	}
	
	public ArrayList<Review> getReviews(){
		return reviews;
	}
	
	public void setSimilarMovies(ArrayList<Movie> m){
		similarMovies = m;
	}
	
	public ArrayList<Movie> getSimilarMovies(){
		return similarMovies;
	}
	
	//getters and setters for cast
	public void setCast(List<String> castMembers){
		cast = castMembers;
	}
	public List<String> getCast(){
		return cast;
	}
	
	//getters and setters for alternativetitle
	public void setAlternativeTitles(ArrayList<String> titles)
	{
		alternativeTitles = titles;
	}
	public ArrayList<String> getAlternativeTitles()
	{
		return alternativeTitles;
	}
	
	//getters and setters for links
	public void setLink(String filmLink){
		link = filmLink;
	}
	public String getLink(){
		return link;
	}
	
	//getters and setters for links
	public void setClipLink(String videoLink){
		this.videoLink = videoLink;
	}
	public String getClipLink(){
		return videoLink;
	}
	
	//getters and setters for imdb ID
	public void setImdbId(String id){
		imdbId = id;
	}
	public String getImdbId(){
		return imdbId;
	}
	public String getFullImdbId(){
		return "tt" + imdbId;
	}
	
	public void setShowtimes(ArrayList<Showtimes> s){
		showtimes = s;
	}
	
	public ArrayList<Showtimes> getShowtimes(){
		return showtimes;
	}
	
	public String displaySynopsis(String syn)
	{
		final int maxLength = 200;
		String shortVersion;
		if(syn.length() > maxLength)
		{
			shortVersion = syn.substring(0, maxLength) + "...";
		}
		else
		{
			shortVersion = syn;
		}
		return shortVersion;
	}
	
	public String castToString(List<String> names){
		String s = "";
		for(int i=0; i<names.size(); i++){
			if(i == names.size()-1){
				s += names.get(i);
			}
			else
				s += names.get(i) + ", ";
		}
		return s;
	}
	
	public void setThumbnailBitmap(String imageURL){
		URL imgURL = null;
		try{
			//change to a URL
			imgURL = new URL(imageURL);
			//now decode into an image
			thumbnailPoster = BitmapFactory.decodeStream(imgURL.openConnection().getInputStream());
		}
		catch(Exception e){
			e.printStackTrace();
			Log.e("log_tag", "Error processing the Image at " + imgURL);
		}
	}
	
	public Bitmap getThumbnailBitmap(){
		return thumbnailPoster;
	}
	
	public void setProfileBitmap(String imageURL){
		URL imgURL = null;
		try{
			//change to a URL
			imgURL = new URL(imageURL);
			//now decode into an image
			profilePoster = BitmapFactory.decodeStream(imgURL.openConnection().getInputStream());
		}
		catch(Exception e){
			e.printStackTrace();
			Log.e("log_tag", "Error processing the Image at " + imgURL);
		}
	}
	
	public Bitmap getProfileBitmap(){
		return profilePoster;
	}
	
	public void setBitmap(Bitmap b){
		profilePoster = b;
	}
	
	public Bitmap getPoster(){
		return profilePoster;
	}
	
	public void setPoster(Bitmap b){
		profilePoster = b;
	}
	
	//parcelable stuff
	public int describeContents() 
	{
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) 
    {
    	out.writeString(id);
        out.writeString(title);
        out.writeInt(year);
        out.writeString(rating);
        out.writeInt(runtime);
        out.writeString(releaseDate);
        out.writeString(consensus);
        out.writeString(synopsis);
        out.writeStringList(cast);
        out.writeInt(criticScore);
        out.writeInt(audienceScore);
        out.writeString(imdbId);
        out.writeString(link);
        out.writeString(videoLink);
        out.writeTypedList(reviews);
        //need Genre
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie(Parcel in) {
       
        this();
        cast = new ArrayList<String>();
        id = in.readString();
        title = in.readString();
        year = in.readInt();
        rating = in.readString();
        runtime = in.readInt();
        releaseDate = in.readString();
        consensus = in.readString();
        synopsis = in.readString();
        in.readStringList(cast);
        criticScore = in.readInt();
        audienceScore = in.readInt();
        imdbId = in.readString();
        link = in.readString();
        videoLink = in.readString();
        in.readTypedList(reviews, Review.CREATOR);
    }
}
