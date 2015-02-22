package com.movie.watch.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movie.watch.Movie;
import com.movie.watch.R;
import com.movie.watch.adapter.MovieListAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.list_item_card)
public class MovieItemView extends RelativeLayout {

  @ViewById
  protected TextView cardTitle;
  @ViewById
  protected ImageView cardImage;

  private Context context;

  public MovieItemView(Context context) {
    super(context);
    this.context = context;
  }

  public void bind(final Movie movie, final MovieListAdapter movieListAdapter) {
    String title = movie.getTitle();
    cardTitle.setText(title);
    displayPoster(movie);
  }

  private void displayPoster(final Movie movie) {
    DisplayImageOptions options = buildDisplayImageOptions();
    ImageLoader imageLoader = ImageLoader.getInstance();
    imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    imageLoader.displayImage(movie.getProfileImg(), cardImage, options, new ImageLoadingListener() {
      @Override
      public void onLoadingStarted() {
      }

      @Override
      public void onLoadingFailed(FailReason failReason) {
      }

      @Override
      public void onLoadingComplete(Bitmap loadedImage) {
        movie.setBitmap(loadedImage);
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

