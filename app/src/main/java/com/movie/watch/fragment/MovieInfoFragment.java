package com.movie.watch.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.movie.watch.R;
import com.movie.watch.busevents.TmdbFindMovieEvent;
import com.movie.watch.busevents.TmdbGetMovieEvent;
import com.movie.watch.model.Genre;
import com.movie.watch.model.Movie;
import com.movie.watch.model.Ratings;
import com.movie.watch.model.TmdbMovie;
import com.movie.watch.model.TmdbMovieResult;
import com.movie.watch.model.tmdb.Trailers;
import com.movie.watch.model.tmdb.Youtube;
import com.movie.watch.utils.MovieInfoParser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.fragment_movie_info)
public class MovieInfoFragment extends BaseFragment {
  private static final String TAG = MovieInfoFragment.class.getSimpleName();

  @ViewById
  protected ImageView movieInfoBackDrop;
  @ViewById
  protected ImageView movieInfoPoster;
  @ViewById
  protected TextView movieInfoCriticScore;
  @ViewById
  protected TextView movieInfoAudienceScore;
  @ViewById
  protected TextView movieInfoMetaScore;
  @ViewById
  protected TextView movieInfoGenre;
  @ViewById
  protected TextView movieInfoRating;
  @ViewById
  protected TextView movieInfoReleaseDate;
  @ViewById
  protected TextView movieInfoRuntime;
  @ViewById
  protected TextView movieInfoCast;
  @ViewById
  protected TextView movieInfoOverview;
  @ViewById
  protected ImageView movieInfoPlayButton;

  @Bean
  protected MovieInfoParser movieInfoParser;

  @FragmentArg
  protected Movie rottenTomatoesMovie;

  private Youtube youtubeTrailer;

  public static Fragment create(Movie rottenTomatoesMovie) {
    return MovieInfoFragment_.builder().rottenTomatoesMovie(rottenTomatoesMovie).build();
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return null;
  }

  @AfterViews
  public void afterViews() {
    setPoster();
    setCriticsScore();
    setAudienceScore();
    setMetaScore();
    setRating();
    setReleaseDate();
    setRuntime();
    setCast();
    setSynopsis();
  }

  private void setPoster() {
    movieInfoParser.displayPoster(rottenTomatoesMovie.getPosters().getProfile(), movieInfoPoster);
  }

  private void setCriticsScore() {
    Ratings rating = rottenTomatoesMovie.getRatings();
    String score = movieInfoParser.getCriticsScore(rating);
    movieInfoCriticScore.setCompoundDrawablesWithIntrinsicBounds(movieInfoParser.getCriticRatingImage(rating), 0, 0, 0);
    movieInfoCriticScore.setText(score);
  }

  private void setAudienceScore() {
    Ratings rating = rottenTomatoesMovie.getRatings();
    String score = movieInfoParser.getAudienceScore(rottenTomatoesMovie.getRatings());
    movieInfoAudienceScore.setCompoundDrawablesWithIntrinsicBounds(movieInfoParser.getAudienceRatingImage(rating), 0, 0, 0);
    movieInfoAudienceScore.setText(score);
  }

  private void setMetaScore() {
    movieInfoMetaScore.setVisibility(View.INVISIBLE);
  }

  private void setRating() {
    movieInfoRating.setText(rottenTomatoesMovie.getMpaaRating());
  }

  private void setReleaseDate() {
    String releaseDate = movieInfoParser.getMediumDate(rottenTomatoesMovie.getReleaseDates().getTheater());
    movieInfoReleaseDate.setText(releaseDate);
  }

  private void setRuntime() {
    movieInfoRuntime.setText(movieInfoParser.getRuntime(rottenTomatoesMovie.getRuntime(), true));
  }

  private void setCast() {
    movieInfoCast.setText(movieInfoParser.getCast(rottenTomatoesMovie.getCast()));
  }

  private void setSynopsis() {
    movieInfoOverview.setText(rottenTomatoesMovie.getSynopsis());
  }

  @Click
  protected void movieInfoPlayButton() {
    String url = movieInfoParser.getYoutubeUrl(youtubeTrailer.getSource());
    Uri uri = Uri.parse(url);
    startActivity(new Intent(Intent.ACTION_VIEW, uri));
  }

  public void onEventMainThread(TmdbFindMovieEvent findMovieEvent) {
    EventBus.getDefault().removeStickyEvent(findMovieEvent);

    TmdbMovieResult movie = findMovieEvent.getMovie();
    getTmdbMovie("" + movie.getId());
    getCredits("" + movie.getId());

    String backDropPath = movie.getBackdropPath();
    setTmdbImage(backDropPath, movieInfoBackDrop);

    String posterPath = movie.getPosterPath();
    setTmdbImage(posterPath, movieInfoPoster);
  }

  private void setTmdbImage(String path, ImageView view) {
    if (path != null) {
      String url = movieInfoParser.getBackDropUrl(path);
      movieInfoParser.displayPoster(url, view);
    }
  }

  public void onEventMainThread(TmdbGetMovieEvent getMovieEvent) {
    EventBus.getDefault().removeStickyEvent(getMovieEvent);

    TmdbMovie movie = getMovieEvent.getMovie();
    setGenre(movie.getGenres());

    setPlayButton(movie.getTrailers());
  }

  private void setGenre(List<Genre> genreList) {
    String genres = movieInfoParser.getGenres(genreList);
    movieInfoGenre.setText(genres);
  }

  private void setPlayButton(Trailers trailers) {
    List<Youtube> trailerList = trailers == null ? new ArrayList<Youtube>() : trailers.getYoutubeTrailers();
    if (trailerList.isEmpty()) {
      movieInfoPlayButton.setVisibility(View.GONE);
      return;
    }

    youtubeTrailer = trailerList.get(0);
    movieInfoPlayButton.setVisibility(View.VISIBLE);
  }
}
