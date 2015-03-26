package com.movie.watch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class TmdbFindResults {

  @SerializedName("movie_results")
  @Expose
  private List<TmdbMovieResult> movieResults = new ArrayList<TmdbMovieResult>();
  @SerializedName("person_results")
  @Expose
  private List<Object> personResults = new ArrayList<Object>();
  @SerializedName("tv_results")
  @Expose
  private List<Object> tvResults = new ArrayList<Object>();
  @SerializedName("tv_episode_results")
  @Expose
  private List<Object> tvEpisodeResults = new ArrayList<Object>();
  @SerializedName("tv_season_results")
  @Expose
  private List<Object> tvSeasonResults = new ArrayList<Object>();

  public List<TmdbMovieResult> getMovieResults() {
    return movieResults;
  }

  public void setMovieResults(List<TmdbMovieResult> movieResults) {
    this.movieResults = movieResults;
  }

  public List<Object> getPersonResults() {
    return personResults;
  }

  public void setPersonResults(List<Object> personResults) {
    this.personResults = personResults;
  }

  public List<Object> getTvResults() {
    return tvResults;
  }

  public void setTvResults(List<Object> tvResults) {
    this.tvResults = tvResults;
  }

  public List<Object> getTvEpisodeResults() {
    return tvEpisodeResults;
  }

  public void setTvEpisodeResults(List<Object> tvEpisodeResults) {
    this.tvEpisodeResults = tvEpisodeResults;
  }

  public List<Object> getTvSeasonResults() {
    return tvSeasonResults;
  }

  public void setTvSeasonResults(List<Object> tvSeasonResults) {
    this.tvSeasonResults = tvSeasonResults;
  }
}