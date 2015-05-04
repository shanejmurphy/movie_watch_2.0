package com.movie.watch.constants;


public class Constants {
  //main rotten tomatoes url
  public static final String ROTTEN_TOMATOES_BASE_URL = "http://api.rottentomatoes.com/api/public/v1.0/";

  //search Url
  public static final String SEARCH_PATH = "movies.json?q=";

  //trailers url
  public static final String TRAILERS_URL = "http://api.rottentomatoes.com/api/public/v1.0/movies/";

  //rt api key -- append to urls
  public static final String RT_API_KEY = "wtcku8jhpcj3zh6gtexnb94x";

  //movie folders
  public static final String BOX_OFFICE_PATH = "box_office.json";
  public static final String IN_THEATRES_PATH = "in_theaters.json";
  public static final String OPENING_PATH = "opening.json";
  public static final String UPCOMING_PATH = "upcoming.json";

  //http://api.rottentomatoes.com/api/public/v1.0/lists/dvds/top_rentals.json?limit=16&country=ie&apikey=wtcku8jhpcj3zh6gtexnb94x
  //http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json?limit=16&country=us&apikey=wtcku8jhpcj3zh6gtexnb94x
  //http://www.google.com/movies?near=53.343205,%20-6.434563&hl=en&q=cinderella


  //dvd folders
  public static final String TOP_RENTALS_PATH = "top_rentals.json";
  public static final String CURRENT_RELEASES_PATH = "current_releases.json";//&page=1";
  public static final String NEW_RELEASES_PATH = "new_releases.json";//&page=1";
  //public static final String UPCOMING_DVDS_PATH = "upcoming.json";//&page=1";

  //TMDB
  public static final String TMDB_BACK_DROP_PATH = "https://image.tmdb.org/t/p/original/";
  public static final String TMDB_POSTER_PATH = "https://image.tmdb.org/t/p/w185/";
  public static final String TMDB_PROFILE_PATH = "https://image.tmdb.org/t/p/w185/";
  public static final String OLD_TMDB_API_KEY = "?api_key=b452ceeb800e4bc73b22bbbb83635f87";
  public static final String TMDB_API_KEY = "?api_key=187805121faeae75c16844146e69f2d1";
  public static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
  public static final String TMDB_FIND_MOVIE_BASE_URL = "http://api.themoviedb.org/3/find/";

  //Youtube clips
  public static final String YOUTUBE_TRAILERS_BASE_URL = "http://www.youtube.com/watch?v=";

  //Showtimes
  public static final String SHOWTIMES_BASE_URL = "http://www.google.com/";

  //Google services
  public static final String GOOGLE_APIS_BASE_URL = "https://maps.googleapis.com/maps/api/";

  //no of results to return from Rotten Tomatoes Requests
  public static final int MAX_RESULTS_PER_PAGE = 16;
  //showtimes
  //http://www.google.com/movies?near={lat},{lon}&hl=en&q={movieTitle}"

}
