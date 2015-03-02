package com.movie.watch.constants;


public class Constants {
  //main rotten tomatoes url
  public static final String BASE_URL = "http://api.rottentomatoes.com/";
  public static final String MOVIE_URL = "http://api.rottentomatoes.com/api/public/v1.0/lists/"; //box_office.json?limit=16&country=us&apikey=wtcku8jhpcj3zh6gtexnb94x";

  //search Url
  public static final String SEARCH_URL = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?q=";

  //trailers url
  public static final String TRAILERS_URL = "http://api.rottentomatoes.com/api/public/v1.0/movies/";

  //rt api key -- append to urls
  public static final String API_KEY = "&apikey=wtcku8jhpcj3zh6gtexnb94x";

  //movie folders
  public static final String BOX_OFFICE_PATH = "box_office.json";
  public static final String IN_THEATRES_PATH = "in_theaters.json";
  public static final String OPENING_PATH = "opening.json";
  public static final String UPCOMING_MOVIES_PATH = "upcoming.json";

  //dvd folders
  public static final String TOP_RENTALS_PATH = "dvds/top_rentals.json?limit=16";
  public static final String CURRENT_RELEASES_PATH = "dvds/current_releases.json?page_limit=16";//&page=1";
  public static final String NEW_RELEASES_PATH = "dvds/new_releases.json?page_limit=16";//&page=1";
  public static final String UPCOMING_DVDS_PATH = "dvds/upcoming.json?page_limit=16";//&page=1";

  //TMDB
  public static final String TMDB_API_KEY = "?api_key=b452ceeb800e4bc73b22bbbb83635f87";

}
