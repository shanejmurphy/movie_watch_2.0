package com.movie.watch.model;

import org.parceler.Parcel;

import java.io.Serializable;

@Parcel
public class Theatre implements Serializable {
	
	private String name;
	private String address;
	private String phone;

	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	
	public void setAddress(String address){
		this.address = address;
	}
	public String getAddress(){
		return address;
	}
	
	public void setPhone(String phone){
		this.phone = phone;
	}
	public String getPhone(){
		return phone;
	}
}
