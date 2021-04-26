package fis.project.st.model;

import fis.project.st.model.ShowUtil.Genre;
import fis.project.st.model.TVUtil.Episode;
import fis.project.st.model.TVUtil.Season;

import java.util.ArrayList;
import java.util.Objects;

public class TV extends Show{
    private String first_air_date;
    private String last_air_date;
    private Episode last_episode_to_air;
    private Episode next_episode_to_air;
    private int number_of_episodes;
    private int number_of_seasons;
    private ArrayList<Season> seasons;

    public TV(int id, String poster_path, String backdrop_path, String overview, float vote_average, ArrayList<Genre> genres, String status, String name, String first_air_date, String last_air_date, Episode last_episode_to_air, Episode next_episode_to_air, int number_of_episodes, int number_of_seasons, ArrayList<Season> seasons, String type) {
        super(id, poster_path, backdrop_path, overview, vote_average, genres, status, name, type);
        this.first_air_date = first_air_date;
        this.last_air_date = last_air_date;
        this.last_episode_to_air = last_episode_to_air;
        this.next_episode_to_air = next_episode_to_air;
        this.number_of_episodes = number_of_episodes;
        this.number_of_seasons = number_of_seasons;
        this.seasons = seasons;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public String getLast_air_date() {
        return last_air_date;
    }

    public Episode getLast_episode_to_air() {
        return last_episode_to_air;
    }

    public Episode getNext_episode_to_air() {
        return next_episode_to_air;
    }

    public int getNumber_of_episodes() {
        return number_of_episodes;
    }

    public int getNumber_of_seasons() {
        return number_of_seasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    @Override
    public String toString() {
        return "TV{" +
                "first_air_date='" + first_air_date + '\'' +
                ", last_air_date='" + last_air_date + '\'' +
                ", last_episode_to_air=" + last_episode_to_air +
                ", next_episode_to_air=" + next_episode_to_air +
                ", number_of_episodes=" + number_of_episodes +
                ", number_of_seasons=" + number_of_seasons +
                ", seasons=" + seasons +
                '}';
    }
}
