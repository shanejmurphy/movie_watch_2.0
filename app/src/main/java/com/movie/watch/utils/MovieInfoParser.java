package com.movie.watch.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.movie.watch.R;
import com.movie.watch.constants.Constants;
import com.movie.watch.model.Cast;
import com.movie.watch.model.Genre;
import com.movie.watch.model.Ratings;
import com.movie.watch.model.tmdb.Country;
import com.movie.watch.model.tmdb.Releases;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@EBean(scope = EBean.Scope.Singleton)
public class MovieInfoParser {

  public static final String FRESH = "fresh";
  public static final int MONTHS_IN_THEATER = 3;
  public static final int MONTHS_BEFORE_IN_THEATER = 1;
  private DisplayImageOptions posterOptions;
  private DisplayImageOptions backDropOptions;
  private ImageLoader imageLoader;

  @RootContext
  protected Context context;

  @AfterInject
  protected void afterInject() {
    posterOptions = buildPosterImageOptions();
    backDropOptions = buildBackDropImageOptions();
    imageLoader = ImageLoader.getInstance();
    imageLoader.init(ImageLoaderConfiguration.createDefault(context));
  }

  public String getCriticsScore(Ratings rating) {
    String score = context.getString(R.string.no_data);
    if (rating == null) {
      return score;
    }
    if (rating.getCriticsScore() >= 0) {
      score = context.getString(R.string.critics_percentage, rating.getCriticsScore() + "%");
    }
    return score;
  }

  public int getCriticRatingImage(Ratings rating) {
    int criticRatingImage = R.drawable.desaturated_fresh;
    if (rating == null) {
      return criticRatingImage;
    }
    if (rating.isCertifiedFresh()) {
      criticRatingImage = R.drawable.certified_fresh;
    } else if (rating.isFresh()) {
      criticRatingImage = R.drawable.fresh;
    } else if (rating.isRotten()) {
      criticRatingImage = R.drawable.rotten;
    }
    return criticRatingImage;
  }

  public String getAudienceScore(Ratings rating) {
    String score = context.getString(R.string.no_data);
    if (rating == null) {
      return score;
    }
    if (rating.getAudienceScore() >= 0) {
      score = context.getString(R.string.audience_percentage, rating.getAudienceScore() + "%");
    }
    return score;
  }

  public int getAudienceRatingImage(Ratings rating) {
    int audienceRatingImage = R.drawable.desaturated_popcorn;
    if (rating == null) {
      return audienceRatingImage;
    }
    if (rating.getAudienceScore() < 60) {
      audienceRatingImage = R.drawable.spilt;
    } else {
      audienceRatingImage = R.drawable.popcorn;
    }
    return audienceRatingImage;
  }

  public String getCast(List<Cast> cast) {
    String castStr = context.getString(R.string.no_data);
    if (cast == null) {
      return castStr;
    }
    if (cast.size() <= 0) {
      return castStr;
    }

    castStr = "";
    int loopIndex = 1;
    for (Cast actor : cast) {
      String appendStr = loopIndex++ == cast.size() ? "" : ", ";
      castStr += actor.getName() + appendStr;
    }
    return castStr;
  }

  public String getSynopsis(String synopsis) {
    if (TextUtils.isEmpty(synopsis)) {
      return context.getString(R.string.no_data);
    }
    return synopsis;
  }

  public String getRuntime(int runtime, boolean isLongRuntime){
    int hours = 0;
    int minutes = runtime%60;
    if(runtime > 59){
      hours = runtime/60;
      String hoursStr = context.getString(R.string.hour_shortened);
      if (isLongRuntime) {
        hoursStr = hours > 1 ? context.getString(R.string.hour_plural) : context.getString(R.string.hour_singular);
      }
      return hours + hoursStr + " " + getMinutes(minutes, isLongRuntime);
    }
    if (minutes < 1) {
      return context.getString(R.string.no_data);
    }
    return getMinutes(minutes, isLongRuntime);
  }

  private String getMinutes(int minutes, boolean isLongRuntime) {
    String minuteStr = context.getString(R.string.minutes_shortened);
    if (isLongRuntime) {
      minuteStr = context.getString(R.string.minutes_plural);
      if (minutes == 1) {
        minuteStr = context.getString(R.string.minute_singular);
      }
    }
    String minuteNum = minutes < 10 ? "0" + minutes : "" + minutes;
    return minuteNum + minuteStr;
  }

  public String getGenres(List<Genre> genreList) {
    String genreStr = context.getString(R.string.no_data);
    if (genreList == null) {
      return genreStr;
    }
    if (genreList.size() <= 0) {
      return genreStr;
    }

    genreStr = "";
    int loopIndex = 1;
    for (Genre genre : genreList) {
      String appendStr = loopIndex++ == genreList.size() ? "" : ", ";
      genreStr += genre.getName() + appendStr;
    }
    return genreStr;
  }

  public String getMediumDate(String releaseDate) {
    if (TextUtils.isEmpty(releaseDate)) {
      return context.getString(R.string.no_data);
    }
    DateFormat df = DateFormat.getDateInstance();
    Date date = DateTime.parse(releaseDate).toDate();
    return df.format(date);
  }

  public boolean isShowing(String theaterRelease) {
    if (TextUtils.isEmpty(theaterRelease)) {
      return false;
    }
    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
    DateTime dt = formatter.parseDateTime(theaterRelease);
    return dt.isAfter(DateTime.now().minusMonths(MONTHS_IN_THEATER)) && dt.isBefore(DateTime.now().plusMonths(MONTHS_BEFORE_IN_THEATER));
  }

  public String getImdbId(String id) {
    if (id != null && id.contains("tt")) {
      return id;
    } else {
      return "tt" + id;
    }
  }

  public String getBackDropUrl(String backDropPath) {
    backDropPath = trimPathIfNeeded(backDropPath);
    return Constants.TMDB_BACK_DROP_PATH + backDropPath;
  }

  public String getPosterUrl(String posterPath) {
    posterPath = trimPathIfNeeded(posterPath);
    return Constants.TMDB_POSTER_PATH + posterPath;
  }

  public String getProfileUrl(String profilePath) {
    profilePath = trimPathIfNeeded(profilePath);
    return Constants.TMDB_PROFILE_PATH + profilePath;
  }

  private String trimPathIfNeeded(String profilePath) {
    if (TextUtils.isEmpty(profilePath)) {
      return "";
    }
    if (profilePath.substring(0,1).contains("/")) {
      profilePath = profilePath.substring(1);
    }
    return profilePath;
  }

  public String getReviewText(String quote) {
    if (TextUtils.isEmpty(quote)) {
      return context.getString(R.string.no_data);
    }
    return quote;
  }

  public int getReviewIcon(String reviewRating) {
    switch (reviewRating) {
      case FRESH:
        return R.drawable.fresh;
      default:
        return R.drawable.rotten;
    }
  }

  public String getYoutubeUrl(String source) {
    return Constants.YOUTUBE_TRAILERS_BASE_URL + source;
  }

  public String getReleaseDateForCountry(Releases releases, String countryCode) {
    for(Country country : releases.getCountries()) {
      if (country.getIso31661().equalsIgnoreCase(countryCode)) {
        return country.getReleaseDate();
      }
    }
    return null;
  }

  public void displayBackDrop(final String uri, final ImageView imageView) {
    imageLoader.displayImage(uri, imageView, backDropOptions, new AnimateFirstDisplayListener());
  }

  public void displayPoster(final String uri, final ImageView imageView) {
    imageLoader.displayImage(uri, imageView, posterOptions, new AnimateFirstDisplayListener());
  }

  private DisplayImageOptions buildPosterImageOptions() {
    return new DisplayImageOptions.Builder()
    .showImageForEmptyUri(R.drawable.poster_default)
    .showImageOnFail(R.drawable.poster_default)
    .showImageOnLoading(R.drawable.poster_default)
    .cacheInMemory(true)
    .cacheOnDisk(true)
    .build();
  }

  private DisplayImageOptions buildBackDropImageOptions() {
    return new DisplayImageOptions.Builder()
    .imageScaleType(ImageScaleType.EXACTLY)
    .showImageOnLoading(R.drawable.transparent_backdrop)
    .cacheInMemory(true)
    .cacheOnDisk(true)
    .build();
  }

  private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

    static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
      if (loadedImage != null) {
        ImageView imageView = (ImageView) view;
        boolean firstDisplay = !displayedImages.contains(imageUri);
        if (firstDisplay) {
          FadeInBitmapDisplayer.animate(imageView, 500);
          displayedImages.add(imageUri);
        }
      }
    }
  }
}
