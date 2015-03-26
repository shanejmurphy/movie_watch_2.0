package com.movie.watch.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.movie.watch.R;
import com.movie.watch.constants.Constants;
import com.movie.watch.model.Cast;
import com.movie.watch.model.Genre;
import com.movie.watch.model.Ratings;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.joda.time.DateTime;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

@EBean
public class MovieInfoParser {

  public static final String FRESH = "fresh";
  @RootContext
  protected Context context;

  public String getCriticsScore(Ratings rating) {
    String score = "--";
    if (rating.getCriticsScore() >= 0) {
      score = "" + rating.getCriticsScore();
    }
    return context.getString(R.string.critics_percentage, score + "%");
  }

  public int getCriticRatingImage(Ratings rating) {
    int criticRatingImage = 0;
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
    String score = "--";
    if (rating.getAudienceScore() >= 0) {
      score = "" + rating.getAudienceScore();
    }
    return context.getString(R.string.audience_percentage, score + "%");
  }

  public int getAudienceRatingImage(Ratings rating) {
    int audienceRatingImage = 0;
    if (rating.getAudienceScore() < 60) {
      audienceRatingImage = R.drawable.spilt;
    } else {
      audienceRatingImage = R.drawable.popcorn;
    }
    return audienceRatingImage;
  }

  public String getCast(List<Cast> cast) {
    String castStr = "";
    int loopIndex = 1;
    for (Cast actor : cast) {
      String appendStr = loopIndex++ == cast.size() ? "" : ", ";
      castStr += actor.getName() + appendStr;
    }
    return castStr;
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
    String genreStr = "";
    int loopIndex = 1;
    for (Genre genre : genreList) {
      String appendStr = loopIndex++ == genreList.size() ? "" : ", ";
      genreStr += genre.getName() + appendStr;
    }
    return genreStr;
  }

  public String getMediumDate(String releaseDate) {
    DateFormat df = DateFormat.getDateInstance();
    Date date = DateTime.parse(releaseDate).toDate();
    return df.format(date);
  }

  public String getImdbId(String id) {
    if (id.contains("tt")) {
      return id;
    } else {
      return "tt" + id;
    }
  }

  public String getBackDropUrl(String backDropPath) {
    return Constants.TMDB_BACK_DROP_PATH + backDropPath;
  }

  public String getProfileUrl(String profilePath) {
    return Constants.TMDB_PROFILE_PATH + profilePath;
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
    String url = Constants.YOUTUBE_TRAILERS_BASE_URL + source;
    return url;
  }

  public void displayPoster(final String uri, final ImageView view) {
    DisplayImageOptions options = buildDisplayImageOptions();
    ImageLoader imageLoader = ImageLoader.getInstance();
    imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    imageLoader.displayImage(uri, view, options, new ImageLoadingListener() {
      @Override
      public void onLoadingStarted() {
      }

      @Override
      public void onLoadingFailed(FailReason failReason) {
      }

      @Override
      public void onLoadingComplete(Bitmap loadedImage) {
        view.setImageBitmap(loadedImage);
      }

      @Override
      public void onLoadingCancelled() {
      }
    });
  }

  private DisplayImageOptions buildDisplayImageOptions() {
    return new DisplayImageOptions.Builder()
    .imageScaleType(ImageScaleType.EXACTLY)
    .showStubImage(R.color.transparent)
    .resetViewBeforeLoading()
    .cacheInMemory()
    .cacheOnDisc()
    .build();
  }
}
