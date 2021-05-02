package fis.project.st.model;

import org.dizitart.no2.objects.Id;

import java.util.ArrayList;

public class User {
    private static ArrayList<Show> followedShows = new ArrayList<>();
    @Id
    private String username;
    private String password;
    private ArrayList<String> movies;
    private ArrayList<String> tvs;
    private ArrayList<String> moviesRates;
    private ArrayList<String> tvsRates;

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
        for (int i = 0; i < 10000; i++) {
            moviesRates.add("0");
        }
        this.tvsRates = new ArrayList<String>();
        for (int i = 0; i < 10000; i++) {
            tvsRates.add("0");
        }
    }

    public User() {
    }

    public static ArrayList<Show> getFollowedShows() {
        return followedShows;
    }

    public static void setFollowedShows(ArrayList<Show> followedShows) {
        User.followedShows = followedShows;
    }

    public int checkIfMovieExists(String moviename) {

        for (String movie : movies) {
            if (movie.equals(moviename)) {
                return 1;
            }
        }
        return 0;
    }

    public int checkIfTvExists(String tvname) {

        for (String tv : tvs) {
            if (tv.equals(tvname)) {
                return 1;
            }
        }
        return 0;
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
