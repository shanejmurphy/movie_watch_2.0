package com.movie.watch.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.movie.watch.Movie;
import com.movie.watch.utils.MovieItemView;
import com.movie.watch.utils.MovieItemView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

@EBean
public class MovieListAdapter extends BaseAdapter {

  @RootContext
  protected Context context;

  ArrayList<Movie> movies;

  @AfterInject
  public void afterInject() {
    movies = new ArrayList<>();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    MovieItemView movieItemView;
    if (convertView == null) {
      movieItemView = MovieItemView_.build(context);
    } else {
      movieItemView = (MovieItemView) convertView;
    }
    movieItemView.bind(getItem(position), this);

    return movieItemView;
  }

  @Override
  public int getCount() {
    return movies.size();
  }

  @Override
  public Movie getItem(int position) {
    return movies.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  public void remove(Movie movie) {
    movies.remove(movie);
    notifyDataSetChanged();
  }

  public ArrayList<Movie> getMovies() {
    return movies;
  }

  public void setMovies(ArrayList<Movie> movies) {
    this.movies = movies;
  }

  public interface OnMustWatch {
    public void OnMustWatch(Movie movie, int position);
  }
}