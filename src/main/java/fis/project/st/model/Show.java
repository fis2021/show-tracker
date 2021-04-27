package fis.project.st.model;

import fis.project.st.model.ShowUtil.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Show {
    private int id;
    private String poster_path;
    private String backdrop_path;
    private String overview;
    private float vote_average;
    private ArrayList<Genre> genres;
    private String status;
    private String name;
    private String type;

    public Show(int id, String poster_path, String backdrop_path, String overview, float vote_average, ArrayList<Genre> genres, String status, String name, String type) {
        this.id = id;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.overview = overview;
        this.vote_average = vote_average;
        this.genres = genres;
        this.status = status;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public float getVote_average() {
        return vote_average;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Show{" +
                "id=" + id +
                ", poster_path='" + poster_path + '\'' +
                ", backdrop_path='" + backdrop_path + '\'' +
                ", overview='" + overview + '\'' +
                ", vote_average=" + vote_average +
                ", genres=" + genres +
                ", status='" + status + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
