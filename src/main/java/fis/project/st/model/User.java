package fis.project.st.model;

import org.dizitart.no2.objects.Id;

import java.util.ArrayList;
import java.util.HashMap;


public class User {

    @Id
    private String username;
    private String password;
    private ArrayList<String> movies;
    private ArrayList<String> tvs;
    private ArrayList<String> moviesRates;
    private ArrayList<String> tvsRates;
    private ArrayList<String> movieComments;
    private ArrayList<String> tvComments;
    private ArrayList<Show> watchlist;
    private HashMap<String, ArrayList<Show>> customLists;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, ArrayList<String> movies, ArrayList<String> tvs) {
        this.username = username;
        this.password = password;
        this.movies = movies;
        this.tvs = tvs;
        this.moviesRates = new ArrayList<String>();
        for (int i = 0; i < 1000; i++) {
            moviesRates.add("0");
        }
        this.tvsRates = new ArrayList<String>();
        for (int i = 0; i < 1000; i++) {
            tvsRates.add("0");
        }
        this.movieComments = new ArrayList<String>();
        for (int i = 0; i < 1000; i++) {
            movieComments.add("");
        }
        this.tvComments = new ArrayList<String>();
        for (int i = 0; i < 1000; i++) {
            tvComments.add("");
        }
        this.watchlist = new ArrayList<Show>();
        this.customLists = new HashMap<String, ArrayList<Show>>();
    }

    public User() {
    }

    public boolean isShowDuplicateCustomList(ArrayList<Show> showlist, String showname){
        for(Show show : showlist){
            if(show.getName().equals(showname)){
                return true;
            }
        }
        return false;
    }

    public void addToCustomLists(String customlistname, Show show){

        if(customLists.containsKey(customlistname)) {
            if(!isShowDuplicateCustomList(customLists.get(customlistname), show.getName())) {
                customLists.get(customlistname).add(show);
            }
        }else{
            ArrayList<Show> shows = new ArrayList<>();
            shows.add(show);
            customLists.put(customlistname, shows);
        }
    }

    public ArrayList<String> getCustomListNames(){
        ArrayList<String> names = new ArrayList<>();
        for(String str : customLists.keySet()){
            names.add(str);
        }
        return names;
    }

    public ArrayList<Show> getCustomList(String customlistname){
        return customLists.get(customlistname);
    }

    public void addToWatchlist(Show show){
        watchlist.add(show);
    }

    public boolean isInWatchlist(Show givenshow){
        for(Show show : watchlist){
            if(show.getName().equals(givenshow.getName())){     //default show.equals not working!
                return true;
            }
        }
        return false;
    }

    public void removeFromWatchlist(Show givenshow){
        for(Show show : watchlist){
            if(show.getName().equals(givenshow.getName())){     //default show.equals not working!
                watchlist.remove(show);
                break;      //Avoiding ConcurrentModificationException
            }
        }
    }

    public ArrayList<Show> getWatchlist() {
        return watchlist;
    }

    public boolean checkIfMovieExists(String moviename) {

        for (String movie : movies) {
            if (movie.equals(moviename)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIfTvExists(String tvname) {

        for (String tv : tvs) {
            if (tv.equals(tvname)) {
                return true;
            }
        }
        return false;
    }

    public void addMovieComment(String comm, String moviename) {

        int index = movies.indexOf(moviename);
        movieComments.set(index, comm);
    }

    public ArrayList<String> getMovieComments() {
        return movieComments;
    }

    public void addTvComment(String comm, String tvname) {

        int index = tvs.indexOf(tvname);
        tvComments.set(index, comm);
    }

    public ArrayList<String> getTvComments() {
        return tvComments;
    }

    public String getMovieComment(String moviename) {

        int index = movies.indexOf(moviename);
        return movieComments.get(index);
    }

    public String getTvComment(String tvname) {

        int index = tvs.indexOf(tvname);
        return tvComments.get(index);
    }

    public void addMovie(String moviename) {
        movies.add(moviename);
    }

    public void addTv(String tvname) {
        tvs.add(tvname);
    }

    public void setMovieRate(String rate, String movie) {

        int index = movies.indexOf(movie);
        moviesRates.set(index, rate);
    }

    public void setTvsRate(String rate, String tvsname) {

        int index = tvs.indexOf(tvsname);
        tvsRates.set(index, rate);
    }

    public ArrayList<String> getMovies() {
        return movies;
    }

    public ArrayList<String> getTvs() {
        return tvs;
    }

    public ArrayList<String> getMoviesRates() {
        return moviesRates;
    }

    public ArrayList<String> getTvsRates() {
        return tvsRates;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        return password != null ? password.equals(user.password) : user.password == null;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
