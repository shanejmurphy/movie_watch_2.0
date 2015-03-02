package com.movie.watch.utils.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movie.watch.R;
import com.movie.watch.adapter.MovieListAdapter;
import com.movie.watch.model.Cast;
import com.movie.watch.model.Movie;
import com.movie.watch.model.Ratings;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.list_item_card)
public class MovieItemView extends RelativeLayout {

  public static final String PERCENTAGE_VALUE = "%";

  private Context context;

  @ViewById
  protected TextView cardTitle;
  @ViewById
  protected ImageView cardImage;
  @ViewById
  protected TextView cardCast;
  @ViewById
  protected TextView cardMisc;
  @ViewById
  protected TextView cardCriticScore;
  @ViewById
  protected TextView cardAudienceScore;

  public MovieItemView(Context context) {
    super(context);
    this.context = context;
  }

  public void bind(final Movie movie, final MovieListAdapter movieListAdapter) {
    cardTitle.setText(movie.getTitle());
    setCastText(movie);
    setMiscText(movie);
    setCriticScoreText(movie);
    setAudienceScoreText(movie);
    displayPoster(movie);
  }

  private void setCastText(Movie movie) {
    String cast = "";
    for (Cast actor : movie.getCast()) {
      cast += actor.getName() + ", ";
    }
    cardCast.setText(cast);
  }

  private void setMiscText(Movie movie) {
    String misc = movie.getMpaaRating() + " - " + movie.getLongRuntime();
    cardMisc.setText(misc);
  }

  private void setCriticScoreText(Movie movie) {
    Integer score = movie.getRatings().getCriticsScore();
    cardCriticScore.setText(score + PERCENTAGE_VALUE);
    cardCriticScore.setCompoundDrawablesWithIntrinsicBounds(getCriticRatingImage(movie), 0, 0, 0);
  }

  private int getCriticRatingImage(Movie movie) {
    int criticRatingImage = 0;
    Ratings rating = movie.getRatings();
    if (rating.isCertifiedFresh()) {
      criticRatingImage = R.drawable.certified_fresh;
    } else if (rating.isFresh()) {
      criticRatingImage = R.drawable.fresh;
    } else if (rating.isRotten()) {
      criticRatingImage = R.drawable.rotten;
    }
    return criticRatingImage;
  }

  private void setAudienceScoreText(Movie movie) {
    Integer score = movie.getRatings().getAudienceScore();
    cardAudienceScore.setText(score + PERCENTAGE_VALUE);
    cardAudienceScore.setCompoundDrawablesWithIntrinsicBounds(getAudienceRatingImage(movie), 0, 0, 0);
  }

  private int getAudienceRatingImage(Movie movie) {
    int audienceRatingImage = 0;
    Ratings rating = movie.getRatings();
    if (rating.isRotten()) {
      audienceRatingImage = R.drawable.spilt;
    } else {
      audienceRatingImage = R.drawable.popcorn;
    }
    return audienceRatingImage;
  }

  private void displayPoster(final Movie movie) {
    DisplayImageOptions options = buildDisplayImageOptions();
    ImageLoader imageLoader = ImageLoader.getInstance();
    imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    imageLoader.displayImage(movie.getPosters().getProfile(), cardImage, options, new ImageLoadingListener() {
      @Override
      public void onLoadingStarted() {
      }

      @Override
      public void onLoadingFailed(FailReason failReason) {
      }

      @Override
      public void onLoadingComplete(Bitmap loadedImage) {
        cardImage.setImageBitmap(loadedImage);
      }

      @Override
      public void onLoadingCancelled() {
      }
    });
  }

  private DisplayImageOptions buildDisplayImageOptions() {
    return new DisplayImageOptions.Builder()
    .showStubImage(R.drawable.black_bg)
    .resetViewBeforeLoading()
    .cacheInMemory()
    .cacheOnDisc()
    .build();
  }

    /*public void showPopup(View v, final Movie movie, final MovieListAdapter movieListAdapter) {
      Context themeWrapper = new ContextThemeWrapper(context, R.style.style_popup_menu);
      PopupMenu popup = new PopupMenu(themeWrapper, v);
      MenuInflater inflater = popup.getMenuInflater();
      inflater.inflate(R.menu.attendees_list_item_options_menu, popup.getMenu());
      popup.show();
      popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
          switch (menuItem.getItemId()) {
            case R.id.removeAttendee:
              GoogleAnalyticsTrackerUtil_.getInstance_(context).trackRemoveFollower();
              movieListAdapter.remove(movie);
              return true;
            default:
              return false;
          }
        }
      });
    }*/
}

