
package com.movie.watch.model;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

@Parcel
public class AlternateIds {

    @Expose
    private String imdb;

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }
}
