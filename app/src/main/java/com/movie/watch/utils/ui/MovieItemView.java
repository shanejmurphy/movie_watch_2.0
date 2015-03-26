package com.movie.watch.utils.ui;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movie.watch.R;
import com.movie.watch.adapter.MovieListAdapter;
import com.movie.watch.model.Cast;
import com.movie.watch.model.Movie;
import com.movie.watch.model.Ratings;
import com.movie.watch.utils.MovieInfoParser;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.List;

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

  @Bean
  protected MovieInfoParser movieInfoParser;

  public MovieItemView(Context context) {
    super(context);
    this.context = context;
  }

  public void bind(final Movie movie, final MovieListAdapter movieListAdapter) {
    cardTitle.setText(movie.getTitle());
    setCastText(movie.getCast());
    setMiscText(movie);
    setCriticScoreText(movie.getRatings());
    setAudienceScoreText(movie.getRatings());
    movieInfoParser.displayPoster(movie.getPosters().getProfile(), cardImage);
  }

  private void setCastText(List<Cast> castList) {
    String cast = movieInfoParser.getCast(castList);
    cardCast.setText(cast);
  }

  private void setMiscText(Movie movie) {
    String misc = movie.getMpaaRating() + " - " + movieInfoParser.getRuntime(movie.getRuntime(), false);
    cardMisc.setText(misc);
  }

  private void setCriticScoreText(Ratings rating) {
    Integer score = rating.getCriticsScore();
    if (score >= 0) {
      cardCriticScore.setText(score + PERCENTAGE_VALUE);
      cardCriticScore.setCompoundDrawablesWithIntrinsicBounds(movieInfoParser.getCriticRatingImage(rating), 0, 0, 0);
    }
  }

  private void setAudienceScoreText(Ratings rating) {
    Integer score = rating.getAudienceScore();
    if (score >= 0) {
      cardAudienceScore.setText(score + PERCENTAGE_VALUE);
      cardAudienceScore.setCompoundDrawablesWithIntrinsicBounds(movieInfoParser.getAudienceRatingImage(rating), 0, 0, 0);
    }
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

