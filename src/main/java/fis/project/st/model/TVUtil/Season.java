package fis.project.st.model.TVUtil;

import java.util.Objects;

public class Season {
    private String air_date;
    private int episode_count;
    private int id;
    private String name;
    private String overview;
    private String poster_path;
    private int season_number;

    public Season(String air_date, int episode_count, int id, String name, String overview, String poster_path, int season_number) {
        this.air_date = air_date;
        this.episode_count = episode_count;
        this.id = id;
        this.name = name;
        this.overview = overview;
        this.poster_path = poster_path;
        this.season_number = season_number;
    }

    public String getAir_date() {
        return air_date;
    }

    public int getEpisode_count() {
        return episode_count;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public int getSeason_number() {
        return season_number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Season)) return false;
        Season season = (Season) o;
        return getEpisode_count() == season.getEpisode_count() && getId() == season.getId() && getSeason_number() == season.getSeason_number() && getAir_date().equals(season.getAir_date()) && getName().equals(season.getName()) && getOverview().equals(season.getOverview()) && getPoster_path().equals(season.getPoster_path());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAir_date(), getEpisode_count(), getId(), getName(), getOverview(), getPoster_path(), getSeason_number());
    }

    @Override
    public String toString() {
        return "Season{" +
                "air_date='" + air_date + '\'' +
                ", episode_count=" + episode_count +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", overview='" + overview + '\'' +
                ", poster_path='" + poster_path + '\'' +
                ", season_number=" + season_number +
                '}';
    }
}
