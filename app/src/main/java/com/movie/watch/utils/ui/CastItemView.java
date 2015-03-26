package com.movie.watch.utils.ui;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movie.watch.R;
import com.movie.watch.adapter.CreditGridAdapter;
import com.movie.watch.model.credits.Cast;
import com.movie.watch.utils.MovieInfoParser;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.grid_item_credit)
public class CastItemView extends LinearLayout {

  private Context context;

  @ViewById
  protected ImageView creditProfileImage;
  @ViewById
  protected TextView creditProfileName;
  @ViewById
  protected TextView creditProfileCharacter;

  @Bean
  protected MovieInfoParser movieInfoParser;

  public CastItemView(Context context) {
    super(context);
    this.context = context;
  }

  public void bind(final Cast cast, final CreditGridAdapter creditGridAdapter) {
    setProfileImage(cast.getProfilePath());
    setProfileName(cast.getName());
    setProfileCharacter(cast.getCharacter());
  }

  private void setProfileImage(String profilePath) {
    if (profilePath != null) {
      String url = movieInfoParser.getProfileUrl(profilePath);
      movieInfoParser.displayPoster(url, creditProfileImage);
    }
  }

  private void setProfileName(String name) {
    creditProfileName.setText(name);
  }

  private void setProfileCharacter(String character) {
    creditProfileCharacter.setText(character);
  }
}

