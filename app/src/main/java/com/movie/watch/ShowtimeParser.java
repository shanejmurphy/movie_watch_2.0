package com.movie.watch;

import java.util.ArrayList;

import android.util.Log;

public class ShowtimeParser {

	/*
	 *  parsing the following content
	 *  
	 *  <div class=theater><div id=theater_14304478634468596905 >
	 *	<div class=name><a href="/movies?near=liffey+valley&amp;hl=en&amp;sort=1&amp;tid=c683b78798aa5ca9" id=link_1_theater_14304478634468596905>ODEON - Blanchardstown</a></div>
	 *	<div class=address>Blanchardstown Road South, Blanchardstown Shopping Center, Blanchardstown, Ireland<a href="" class=fl target=_top></a></div></div>
	 *	<div class=times><span style="color:#666"><span style="padding:0 ">‎</span><!--  -->14:30‎</span><span style="color:"><span style="padding:0 "> &nbsp‎</span><!--  -->18:05‎</span><span style="color:"><span style="padding:0 "> &nbsp‎</span><!--  -->18:40‎</span><span style="color:"><span style="padding:0 "> &nbsp‎</span><!--  -->19:30‎</span><span style="color:"><span style="padding:0 "> &nbsp‎</span><!--  -->21:30‎</span></div></div>
	 * 
	 */

	private static final String MOVIE_DIV = "<div class=movie ";
	private static final String THEATRE_DIV = "<div class=theater>";
	private static final String NAME_DIV = "<div class=name>";
	private static final String ADDRESS_DIV = "<div class=address>";
	private static final String TIMES_DIV = "<div class=times>";
	
	private static final String MOVIE_TITLE_HOOK = "itemprop=\"name\">";
	private static final String THEATRE_HOOK = "id=link_1_theater_";
	private static final String TIMES_HOOK = "-->";
	
	//private static final String TRAILER_HOOK = "Trailer</a>";
	
	//private String title;
	
	/*public ShowtimeParser(String movieTitle){
		title = movieTitle;
	}*/
	
	public ArrayList<Showtimes> parse(String html){
		String htmlToParse = "";
		String moreHtml = "";
		int start= 0, end = 0;
		ArrayList<Showtimes> showtimes = new ArrayList<Showtimes>();
		//if(html.contains(">" + title + "<")){
		try{
		/*if*/while(html.contains(MOVIE_DIV)){
				//start = html.indexOf(">" + title + "<");
				int i = 0;
				start = html.indexOf(MOVIE_DIV) + MOVIE_DIV.length();
				htmlToParse = html.substring(start);
				//end = htmlToParse.indexOf("<div class=movie");
				end = htmlToParse.indexOf(MOVIE_DIV);
				if(end > 0){
					html = htmlToParse.substring(0, end);
					moreHtml = htmlToParse;
				}
				else{
					html = htmlToParse;
				}
				
				//get the movie name
				String movieTitle = getMovieTitle(html);
				//getTrailerURL(html);
				//try{
				while(html.contains(THEATRE_DIV)){// && (i < 6)){
					//only return 3 cinemas at most
					start = html.indexOf(NAME_DIV) + NAME_DIV.length() + 2;
					htmlToParse = html.substring(start);
					Theatre t = new Theatre();
					ArrayList<String> times = new ArrayList<String>();
					Showtimes s = new Showtimes();
					if(i == 0){
						//movie title -- only for 1st theatre in the list
						s.setMovieTitle(movieTitle);
					}
					
					//thatre
					t.setName(getName(htmlToParse));
					
					start = html.indexOf(ADDRESS_DIV);
					htmlToParse = html.substring(start);
					t.setAddress(getAddress(htmlToParse));
					html = htmlToParse;
					
					start = html.indexOf(TIMES_DIV);
					htmlToParse = html.substring(start);
					times.addAll(getTimes(htmlToParse));
					html = htmlToParse;
					
					//set the showtimes for a theatre
					s.setTimes(times);
					s.setTheater(t);
					showtimes.add(s);
					
					//increment i
					i++;
				}
				if(end > 0){
					html = moreHtml.substring(end);
				}
			}
		}
		catch(Exception e){
			Log.e("Showtime", "Error returning showtimes" + e.toString());
		}
		return showtimes;
	}
	
	public String getMovieTitle(String subs){
		int beginning = 0;
		String titleString = "";
		int start = subs.indexOf(MOVIE_TITLE_HOOK) + MOVIE_TITLE_HOOK.length(); //200
		
		titleString = subs.substring(start);
		int end = titleString.indexOf("</h2>");
		
		subs = titleString.substring(0, end);
		//end = subs.indexOf("<");
		if(subs.contains("href=")){ //we have many titles
			beginning = subs.indexOf(">") + 1;
			end = subs.indexOf("</");
			return subs.substring(beginning, end);
		}
		else{
			return subs;
		}
	}
	
	public String getName(String subs){
		int start = subs.indexOf(THEATRE_HOOK); //200
		int end = subs.indexOf("<");			//300
		
		String nameString = subs.substring(start, end); //100
		int nameStart = nameString.indexOf(">") + 1; 	//80
		Log.i("Showtime", "Cinema Name: " + nameString.substring(nameStart)); 
		return nameString.substring(nameStart);
	}
	
	public String getAddress(String subs){
		int start = ADDRESS_DIV.length();
		int end = subs.indexOf("<", start);
		
		String addressString = subs.substring(start, end);
		Log.i("Showtime", "Cinema Address: " + addressString);
		return addressString;
	}
	
	public ArrayList<String> getTimes(String subs){
		ArrayList<String> times = new ArrayList<String>();
		//int start = TIMES_DIV.length();
		String tempString = "";//subs.substring(start);
		int end = subs.indexOf("</div></div>");
		String timeString = subs.substring(0, end);
		while(timeString.contains(TIMES_HOOK)){
			int timeStart = timeString.indexOf(TIMES_HOOK) + (TIMES_HOOK.length());
			int timeEnd = timeString.indexOf("&#8206;</", timeStart);//
			String time = timeString.substring(timeStart, timeEnd);
			if(time.contains("http")){
				int urlStart = time.indexOf("http");
				int urlEnd = time.indexOf("\" ");
				String timeURL = time.substring(urlStart, urlEnd);
				timeStart = time.indexOf(">") + 1;
				String urlTime = time.substring(timeStart);
				times.add(urlTime);
				tempString = timeString.substring(timeEnd + "</a></span>".length());
				Log.i("Showtime", "Time URL: " + timeURL);
				Log.i("Showtime", "Time: " + urlTime);
			}
			else{
				times.add(time);
				tempString = timeString.substring(timeEnd + "</span>".length());
				Log.i("Showtime", "Time: " + time);
			}
			timeString = tempString;
			//Log.i("Showtime", "Time: " + time);
		}
		return times;
	}
	
	/*
	public String getTrailer(String subs){
		String trailerString = "";
		if(subs.contains(">" + title + "<")){
			int start = subs.indexOf(">" + title + "<");
			String htmlToParse = subs.substring(start);
			subs = htmlToParse;
			int end = subs.indexOf(TRAILER_HOOK);
			if(end > 0){
				String tempString = subs.substring(0, end);
				start = tempString.lastIndexOf("v%3D") + 4;
				int endOfURL = tempString.lastIndexOf("\"");
				htmlToParse = tempString.substring(start, endOfURL);
				end = htmlToParse.indexOf("&amp");
				trailerString = htmlToParse.substring(0, end);
				//Log.i("Showtime", "Trailer URL = " + trailerString);
			}
		}
		return trailerString;
	}*/
}
