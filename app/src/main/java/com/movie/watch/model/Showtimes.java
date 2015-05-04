package com.movie.watch.model;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;

@Parcel
public class Showtimes implements Serializable {
	private Theatre theatre;
	private ArrayList<String> times;
	
	public void setTheater(Theatre t){
		theatre = t;
	}
	public Theatre getTheatre(){
		return theatre;
	}
	
	public void setTimes(ArrayList<String> times){
		this.times = times;
	}
	public ArrayList<String> getTimes(){
		return times;
	}
	
	public String displayTimes(){
		String timesStr = "";
		for(String time : times) {
			timesStr += time + "   ";
		}
		return timesStr;
	}
}
