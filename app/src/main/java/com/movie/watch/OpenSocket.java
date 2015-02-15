	package com.movie.watch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class OpenSocket {

	private String url;
	
	public OpenSocket(String url){
		this.url = url;
	}
	
	public ArrayList<Movie> setMovies(){
		String json = readMovieFeed();
		ArrayList<Movie> movies = new ArrayList<Movie>();
		try 
		{
	   		JSONObject jsonObject = new JSONObject(json); //a big string of values
	   		JSONArray movieArray = jsonObject.getJSONArray("movies");
	   		Log.d("log_tag", "Movie Array Size is: " + movieArray.length());
	
	   		for(int i = 0; i < movieArray.length(); i++) 
			{
	   			//create Movie Object
	   			Movie movie = new Movie();
	   			
	   			//get and set the movie ID
	   			movie.setId(movieArray.optJSONObject(i).optString("id"));
	   			
	   			//get and set the movie title
				movie.setTitle(movieArray.optJSONObject(i).optString("title"));
				
				//get and set the movie year
				movie.setYear(movieArray.optJSONObject(i).optInt("year"));
				
				//get and set the movie rating
				movie.setRating(movieArray.optJSONObject(i).optString("mpaa_rating"));
				
				//get and set the movie runtime
				movie.setRuntime(movieArray.optJSONObject(i).optInt("runtime"));
				
				//get and set the consensus
				String consensus = movieArray.optJSONObject(i).optString("critics_consensus");
				movie.setConsensus(consensus);
				
				//get and set the movie release date
				String date = movieArray.optJSONObject(i).optString("release_dates"); //get the image url as a string
				JSONObject dateObj = new JSONObject(date); //create a JSONObject from ratings Object
				//movie or dvd
				//String movieReleaseDate = dateObj.optString("theater");
				movie.setReleaseDate(dateObj.optString("theater"));
				//String dvdReleaseDate = dateObj.optString("dvd");
				//movie.setReleaseDate(dateObj.optString("dvd"));
				
				//get the score
				String scores = movieArray.optJSONObject(i).optString("ratings"); //get the image url as a string
				JSONObject ratingsObj = new JSONObject(scores); //create a JSONObject from ratings Object
				
				//get the crtics rating for fresh or rotten field
				movie.setCriticRating(ratingsObj.optString("critics_rating"));
				//get the actual critic score
				movie.setCriticScore(ratingsObj.optInt("critics_score"));
				//Log.i("log_tag", "Critics Score =: " + movie.score); //the actual score
				
				movie.setAudienceRating(ratingsObj.optString("audience_rating"));
				//get the actual audience score
				movie.setAudienceScore(ratingsObj.optInt("audience_score"));
				
				//get the description
				movie.setSynopsis(movieArray.optJSONObject(i).optString("synopsis"));
				
				//get the image url as a string
				String imgString = movieArray.optJSONObject(i).optString("posters");
				JSONObject postersObj = new JSONObject(imgString); //create a JSONObject from posters Object
				//grab the thumbnail image url as a string
				//movie.setThumbnailImg(postersObj.optString("thumbnail"));
				//movie.setThumbnailBitmap(movie.getThumbnailImg());
				movie.setProfileImg(postersObj.optString("profile"));
				//movie.setProfileBitmap(movie.getProfileImg());
				
				//get the cast list
				List<String> castList = new ArrayList<String>();
				JSONArray castArray = movieArray.optJSONObject(i).optJSONArray("abridged_cast");
				for(int j=0; j<castArray.length(); j++){
					String name = castArray.optJSONObject(j).optString("name");
					castList.add(name);
					//Log.i("Cast:", name);
				}
				//set the cast
				movie.setCast(castList);
				
				 
				//get the aternate id as a string
				String alternate = movieArray.optJSONObject(i).optString("alternate_ids");
				if(alternate != ""){
					JSONObject alternateObj = new JSONObject(alternate); //create a JSONObject from posters Object
					movie.setImdbId(alternateObj.optString("imdb"));
				}
				
				//get the movie links list
				String links = movieArray.optJSONObject(i).optString("links");
				JSONObject linksObj = new JSONObject(links); //create a JSONObject from posters Object
				String webLink = linksObj.optString("alternate");
				movie.setLink(webLink);
	
				//create a movie item for display
				movies.add(movie);
				//Log.i("Movie: ", movie.getTitle());
			}
		} 
		catch (Exception e) 
		{
			Log.e("log_tag", "Exception creating JSONArray for Movies " + e.toString());
			e.printStackTrace();
		}
	   	return movies;
	}
	
	public String getTrailerLink(){
		String json = readMovieFeed();
		String mainTrailerLink = "";
		try 
		{
	   		JSONObject jsonObject = new JSONObject(json); //a big string of values
	   		JSONArray clipsArray = jsonObject.getJSONArray("clips");
	   		//Log.d("log_tag", "Clips Array Size is: " + clipsArray.length());
	   			
	   		//get and set the movie ID
	   		String links = clipsArray.optJSONObject(0).optString("links");
			JSONObject trailerObj = new JSONObject(links); //create a JSONObject from posters Object
			mainTrailerLink = trailerObj.optString("alternate");
		}catch(Exception e){
			Log.e("log_tag", "Exception creating JSONArray for Trailers " + e.toString());
			e.printStackTrace();
		}
		return mainTrailerLink;
	}
	
	/*
	public String getTMDBAuthentication(){
		String json = readMovieFeed();
		String auth = "";
		try 
		{
	   		JSONObject authJsonObject = new JSONObject(json); //a big string of values 
	   		//JSONArray clipsArray = jsonObject.getJSONArray("request_token");
	   		//Log.d("log_tag", "Clips Array Size is: " + clipsArray.length());
	   			
	   		//get authentication
	   		auth = authJsonObject.optString("request_token");

		}catch(Exception e){
			Log.e("Trailer", "Exception getting Authentication from TMDB API " + json);
			e.printStackTrace();
		}
		return auth;
	}
	
	public String getTMDBSessionId(String link){
		String json = readMovieFeed(link);
		String id = "";
		try 
		{
	   		JSONObject sessionJsonObject = new JSONObject(json); //a big string of values
	   		//Log.d("log_tag", "Clips Array Size is: " + clipsArray.length());
	   			
	   		//get session_id
	   		id = sessionJsonObject.optString("guest_session_id");
	   		DisplayMovie.guestSessionId = id;

		}catch(Exception e){
			Log.e("Trailer", "Exception getting Session ID from TMDB API " + json);
			e.printStackTrace();
		}
		return id;
	}
	*/
	
	public String[] getDateAndTrailer(String id, String country){
		String [] s = new String[2]; //fixed size
		String json = readMovieFeed();
		String date = "";
		String trailer = "";
		try 
		{
	   		JSONObject jsonObject = new JSONObject(json); //a big string of values
	   		
	   		//release dates
	   		if(!country.equalsIgnoreCase("us")){
		   		JSONObject releasesObject = jsonObject.optJSONObject("releases");
		   		JSONArray countriesArray = releasesObject.optJSONArray("countries");
		   		for(int i=0; i< countriesArray.length(); i++){
		   			String countryCode = countriesArray.optJSONObject(i).optString("iso_3166_1");
		   			if(countryCode.equalsIgnoreCase(country)){
		   				date = countriesArray.optJSONObject(i).optString("release_date").toLowerCase();
		   				break;
		   			}
		   		}
	   		}
	   		
	   		//Log.i("Date", "Release Date for " + country + " is" + date);
	   		s[0] = date;
	   		
	   		//trailers
	   		JSONObject trailersObject = jsonObject.optJSONObject("trailers");
	   		JSONArray clipsArray = trailersObject.optJSONArray("youtube");
	   			
	   		//get and set the movie ID
	   		trailer = clipsArray.optJSONObject(0).optString("source");
	   		s[1] = trailer;

		}catch(Exception e){
			Log.e("Date", "Exception creating JSONArray for Date & Trailers " + id + "    " + e.toString());
			Log.e("Date", "JSON String = " + json);
			e.printStackTrace();
		}
		return s;
	}
	
	public String getTMDBTrailerLink(String id){
		/*String sessionId = "";
		if(DisplayMovie.guestSessionId == null){
			sessionId = getTMDBSessionId(TMDB_GUEST_URL);
		}
		else{
			sessionId = DisplayMovie.guestSessionId;
		}
		Log.d("Trailer", "Session ID is: " + sessionId);*/
		//String trailerURL = TMDB_BASE_URL + "3/movie/" + id + "/trailers" + TMDB_API_KEY;
		//Log.d("Trailer", "Trailer URL is: " + id);
		String json = readMovieFeed();
		String link = "";
		try 
		{
	   		JSONObject jsonObject = new JSONObject(json); //a big string of values
	   		JSONArray clipsArray = jsonObject.optJSONArray("youtube");
	   			
	   		//get and set the movie ID
	   		link = clipsArray.optJSONObject(0).optString("source");

		}catch(Exception e){
			Log.e("Trailer", "Exception creating JSONArray for Trailers " + id + "    " + e.toString());
			Log.e("Trailer", "JSON String = " + json);
			e.printStackTrace();
		}
		//Log.d("Trailer", "Youtube link is: " + link);
		return link;
	}
	
	public ArrayList<String> getTMDBTitles(String id){
		/*String sessionId = "";
		if(DisplayMovie.guestSessionId == null){
			sessionId = getTMDBSessionId(TMDB_GUEST_URL);
		}
		else{
			sessionId = DisplayMovie.guestSessionId;
		}
		Log.d("Trailer", "Session ID is: " + sessionId);*/
		//String trailerURL = TMDB_BASE_URL + "3/movie/" + id + "/trailers" + TMDB_API_KEY;
		//Log.d("Trailer", "Trailer URL is: " + id);
		String json = readMovieFeed();
		ArrayList<String> titles = new ArrayList<String>();
		try 
		{
	   		JSONObject jsonObject = new JSONObject(json); //a big string of values
	   		JSONArray titlesArray = jsonObject.optJSONArray("titles");
	   			
	   		//get the titles
	   		for(int i=0; i<titlesArray.length(); i++){
	   			String title = titlesArray.optJSONObject(i).optString("title");
	   			Log.i("Showtime", "Showtime title is: " + title);
	   			titles.add(title);
	   		}

		}catch(Exception e){
			Log.e("Showtime", "Exception creating JSONArray for Alternative Showtimes " + id + "    " + e.toString());
			Log.e("Showtime", "Alternative JSON String = " + json);
			e.printStackTrace();
		}
		//Log.d("Trailer", "Youtube link is: " + link);
		return titles;
	}
	
	public ArrayList<Review> getReviews(){
		//provide string from the url
		String reviewFeed = readMovieFeed();
		
		ArrayList<Review> reviews = new ArrayList<Review>();
    	try 
		{
    		JSONObject jsonObject = new JSONObject(reviewFeed); //a big string of values
    		JSONArray reviewArray = jsonObject.optJSONArray("reviews");
    		//Log.d("log_tag", "Review Array Size is: " + reviewArray.length());

    		for(int i = 0; i < reviewArray.length(); i++) 
			{
    			//create Review Object
    			Review review = new Review();
    			
    			//name
    			String critic = reviewArray.optJSONObject(i).optString("critic");
    			review.setCriticName(critic);
    			
    			//date
    			String date = reviewArray.optJSONObject(i).optString("date");
    			review.setDate(date);
    			
    			//rating
    			String rating = reviewArray.optJSONObject(i).optString("freshness");
    			review.setRating(rating);
    			
    			//publication
    			String publication = reviewArray.optJSONObject(i).optString("publication");
    			review.setPublication("- " + publication);
    			
    			//critique
    			String quote = reviewArray.optJSONObject(i).optString("quote");
    			review.setQuote(quote);
    			
    			//link to article
    			String links = reviewArray.optJSONObject(i).optString("links");
    			JSONObject linkObj = new JSONObject(links);
    			review.setLink(linkObj.optString("review"));
    			
    			//Log.i("Review", "Review for " + publication + ": " + quote);
    			reviews.add(review);
    			
			}
		}catch(Exception e){
			Log.e("log_tag", "Error getting Review information for " + url + e.toString());
		}
    	return reviews;
    }
	
	public ArrayList<Showtimes> getShowtimes(){
		String htmlContent = readMovieFeed();
		ArrayList<Showtimes> showtimes = new ArrayList<Showtimes>();
		try 
		{
	   		showtimes = new ShowtimeParser().parse(htmlContent);
	   		Log.i("Showtime", "OS Showtimes Size = " + showtimes.size());
		}catch(Exception e){
			Log.e("Showtime", "Exception getting info for Showtimes " + e.toString());
			e.printStackTrace();
		}
		return showtimes;
	}
	
	//images
	public ArrayList<String> getImages(){
		String json = readMovieFeed();
		ArrayList<String> imageLinks = new ArrayList<String>();
		try 
		{
			Log.i("Images", "In getImages()");
	   		JSONObject jsonObject = new JSONObject(json); //a big string of values
	   		JSONArray imagesArray = jsonObject.getJSONArray("backdrops");
	   			
	   		for(int i=0; i<imagesArray.length(); i++){
	   			//get and set the movie ID
	   			String link = imagesArray.optJSONObject(i).getString("file_path");
	   			Log.i("Images", "Image path = " + link);
	   			imageLinks.add(link);
	   		}

		}catch(Exception e){
			Log.e("Images", "Exception creating JSONArray for Images " +  e.toString());
			Log.e("Images", "JSON String = " + json);
			e.printStackTrace();
		}
		//Log.d("Trailer", "Youtube link is: " + link);
		return imageLinks;
	}
	
	//return string containg information from url
	public String readMovieFeed() 
    {
		InputStream content = null;
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		if(url.contains("themoviedb"))
		{
			//set a header
			httpGet.setHeader("Accept", "application/json");
		}
		
		try 
		{
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode != HttpStatus.SC_OK) 
			{
				Log.e(MovieMeter.class.toString(), "Failed to download file " + statusCode + " for URL " + url);	
			} 
			else 
			{
				HttpEntity entity = response.getEntity();
				content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while((line = reader.readLine()) != null) 
				{
					builder.append(line);
				}
				content.close();
				//Log.d(MovieMeter.class.toString(), "File Downloaded Successfully " + statusCode + " for URL " + url);	
			}
		} 
		catch (MalformedURLException e)
		{
			Log.e("log_tag", "Malformed URL "+e.toString());
		}
		catch (ClientProtocolException e) 
		{
			Log.e("log_tag", "Client Protocol Exception "+e.toString());
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			Log.e("log_tag", "IO Exception "+e.toString());
			e.printStackTrace();
		}
		catch(Exception e)
		{
            Log.e("log_tag", "Error in http connection "+e.toString());
		}
		
		//Log.d("JSON", builder.toString());
		
		return builder.toString();
	}
}
