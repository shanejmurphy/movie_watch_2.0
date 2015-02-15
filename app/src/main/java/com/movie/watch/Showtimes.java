package com.movie.watch;

import java.util.ArrayList;

public class Showtimes {
	private String movieTitle;
	private Theatre theatre;
	private ArrayList<String> times;
	
	public void setMovieTitle(String t){
		movieTitle = t;
	}
	public String getMovieTitle(){
		if(movieTitle != null){
			return movieTitle;
		}
		else{
			return "";
		}
	}
	
	public void setTheater(Theatre t){
		theatre = t;
	}
	public Theatre getTheatre(){
		return theatre;
	}
	
	//time
	public void setTimes(ArrayList<String> t){
		times = t;
	}
	public ArrayList<String> getTimes(){
		return times;
	}
	
	public String displayTimes(){
		String s = "";
		for(int i=0; i<times.size(); i++){
			s += times.get(i) + "   ";
		}
		return s;
	}
}
