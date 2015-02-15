package com.movie.watch;

import java.util.ArrayList;

public class Theatre {
	
	private String name;
	private String address;
	private String phone;
	private ArrayList<Movie> movies;
	
	//name
	public void setName(String s){
		name = s;
	}
	public String getName(){
		return name;
	}
	
	//address
	public void setAddress(String a){
		address = a;
	}
	public String getAddress(){
		return address;
	}
	
	//phone number
	public void setPhone(String p){
		phone = p;
	}
	public String getPhone(){
		return phone;
	}
	
	//moives
	public void setMovies(ArrayList<Movie> movies){
		this.movies = movies;
	}
	public ArrayList<Movie> getMovies(){
		return movies;
	}
	public void addMovie(Movie m){
		movies.add(m);
	}
}
