package fis.project.st.model;

import fis.project.st.model.ShowUtil.Genre;

import java.util.ArrayList;
import java.util.Objects;

public class Movie extends Show {
    private String release_date;
    private int runtime;
    private boolean video;

    public Movie(int id, String poster_path, String backdrop_path, String overview, float vote_average, ArrayList<Genre> genres, String status, String name, String release_date, int runtime, boolean video, String type) {
        super(id, poster_path, backdrop_path, overview, vote_average, genres, status, name, type);
        this.release_date = release_date;
        this.runtime = runtime;
        this.video = video;
    }

    public String getRelease_date() {
        return release_date;
    }

    public int getRuntime() {
        return runtime;
    }

    public boolean isVideo() {
        return video;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        if (!super.equals(o)) return false;
        Movie movie = (Movie) o;
        return getRuntime() == movie.getRuntime() && isVideo() == movie.isVideo() && getRelease_date().equals(movie.getRelease_date());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getRelease_date(), getRuntime(), isVideo());
    }

    @Override
    public String toString() {
        return "Movie{" +
                ", release_date='" + release_date + '\'' +
                ", runtime=" + runtime +
                ", video=" + video +
                '}';
    }
}
