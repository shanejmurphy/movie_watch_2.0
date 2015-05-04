package com.movie.watch.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.movie.watch.R;
import com.movie.watch.analytics.GoogleAnalyticsTrackerUtil;
import com.movie.watch.busevents.TmdbFindMovieEvent;
import com.movie.watch.busevents.TmdbGetMovieEvent;
import com.movie.watch.model.Movie;
import com.movie.watch.model.Ratings;
import com.movie.watch.model.ReleaseDates;
import com.movie.watch.model.TmdbMovie;
import com.movie.watch.model.TmdbMovieResult;
import com.movie.watch.model.tmdb.Trailers;
import com.movie.watch.model.tmdb.Youtube;
import com.movie.watch.utils.LocationHelper;
import com.movie.watch.utils.MovieInfoParser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.InstanceState;
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
  @ViewById
  protected ImageView movieInfoMiniPlayButton;

  @Bean
  protected MovieInfoParser movieInfoParser;
  @Bean
  protected LocationHelper locationHelper;
  @Bean
  protected GoogleAnalyticsTrackerUtil gaTrackerUtil;

  @FragmentArg
  @InstanceState
  protected Movie rottenTomatoesMovie;

  @InstanceState
  protected boolean isBackDropImageAvailable;
  @InstanceState
  protected Youtube youtubeTrailer;
  @InstanceState
  protected boolean isAlreadyLoaded = false;

  public static Fragment create(Movie rottenTomatoesMovie) {
    return MovieInfoFragment_.builder().rottenTomatoesMovie(rottenTomatoesMovie).build();
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return null;
  }

  @AfterViews
  public void afterViews() {
    if (isAlreadyLoaded && isBackDropImageAvailable) {
      setBackDrop();
    }
    setPoster();
    setCriticsScore();
    setAudienceScore();
    setMetaScore();
    setGenre();
    setRating();
    setReleaseDate();
    setRuntime();
    setCast();
    setSynopsis();
    setPlayButton();

    isAlreadyLoaded = true;
  }

  private void setBackDrop() {
    movieInfoParser.displayBackDrop(rottenTomatoesMovie.getPosters().getDetailed(), movieInfoBackDrop);
  }

  private void setPoster() {
    movieInfoParser.displayPoster(rottenTomatoesMovie.getPosters().getProfile(), movieInfoPoster);
  }

  private void setCriticsScore() {
    Ratings rating = rottenTomatoesMovie.getRatings();
    String score = movieInfoParser.getCriticsScore(rating);
    if (score.equals(getString(R.string.no_data))) {
      return;
    }
    movieInfoCriticScore.setCompoundDrawablesWithIntrinsicBounds(movieInfoParser.getCriticRatingImage(rating), 0, 0, 0);
    movieInfoCriticScore.setText(score);
  }

  private void setAudienceScore() {
    Ratings rating = rottenTomatoesMovie.getRatings();
    String score = movieInfoParser.getAudienceScore(rottenTomatoesMovie.getRatings());
    if (score.equals(getString(R.string.no_data))) {
      return;
    }
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
    movieInfoOverview.setText(movieInfoParser.getSynopsis(rottenTomatoesMovie.getSynopsis()));
  }

  @Click
  protected void movieInfoPlayButton() {
    playYoutubeTrailer();
    gaTrackerUtil.trackTrailerPlayEvent(R.string.ga_label_main_trailer_play_button);
  }

  @Click
  protected void movieInfoMiniPlayButton() {
    playYoutubeTrailer();
    gaTrackerUtil.trackTrailerPlayEvent(R.string.ga_label_mini_trailer_play_button);
  }

  private void playYoutubeTrailer() {
    String url = movieInfoParser.getYoutubeUrl(youtubeTrailer.getSource());
    Uri uri = Uri.parse(url);
    startActivity(new Intent(Intent.ACTION_VIEW, uri));
  }

  public void onEventMainThread(TmdbFindMovieEvent findMovieEvent) {
    EventBus.getDefault().removeStickyEvent(findMovieEvent);
    TmdbMovieResult movie = findMovieEvent.getMovie();

    String backDropPath = movie.getBackdropPath();
    setTmdbBackDropImage(backDropPath, movieInfoBackDrop);

    String posterPath = movie.getPosterPath();
    setTmdbPosterImage(posterPath, movieInfoPoster);

    getTmdbMovie("" + movie.getId());
    getCredits("" + movie.getId());

  }

  private void setTmdbBackDropImage(String path, ImageView view) {
    isBackDropImageAvailable = path != null;
    if (isBackDropImageAvailable) {
      String url = movieInfoParser.getBackDropUrl(path);
      rottenTomatoesMovie.getPosters().setDetailed(url);
      movieInfoParser.displayBackDrop(url, view);
    }
  }

  private void setTmdbPosterImage(String path, ImageView view) {
    if (path != null) {
      String url = movieInfoParser.getPosterUrl(path);
      rottenTomatoesMovie.getPosters().setProfile(url);
      movieInfoParser.displayPoster(url, view);
    }
  }

  public void onEventMainThread(TmdbGetMovieEvent getMovieEvent) {
    EventBus.getDefault().removeStickyEvent(getMovieEvent);

    TmdbMovie movie = getMovieEvent.getMovie();
    rottenTomatoesMovie.setGenres(movie.getGenres());
    setGenre();

    rottenTomatoesMovie.setTrailers(movie.getTrailers());
    setPlayButton();

    String updatedReleaseDate = movieInfoParser.getReleaseDateForCountry(movie.getReleases(), locationHelper.getCountryCode());
    if (TextUtils.isEmpty(updatedReleaseDate) || updatedReleaseDate.equals(rottenTomatoesMovie.getReleaseDates().getTheater())) {
      return;
    }
    rottenTomatoesMovie.setReleaseDates(new ReleaseDates(updatedReleaseDate));
    setReleaseDate();
  }

  private void setGenre() {
    String genres = movieInfoParser.getGenres(rottenTomatoesMovie.getGenres());
    movieInfoGenre.setText(genres);
  }

  private void setPlayButton() {
    Trailers trailers = rottenTomatoesMovie.getTrailers();
    List<Youtube> trailerList = trailers == null ? new ArrayList<Youtube>() : trailers.getYoutubeTrailers();
    if (trailerList.isEmpty()) {
      return;
    }

    if (!isBackDropImageAvailable) {
      movieInfoMiniPlayButton.setVisibility(View.VISIBLE);
    } else {
      movieInfoPlayButton.setVisibility(View.VISIBLE);
    }
    youtubeTrailer = trailerList.get(0);
  }
}
