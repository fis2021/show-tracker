package fis.project.st.model.TVUtil;

import java.util.Objects;

public class Episode {
    String air_date;
    int episode_number;
    int id;
    String name;
    String overview;
    int season_number;
    String still_path;

    public Episode(String air_date, int episode_number, int id, String name, String overview, int season_number, String still_path) {
        this.air_date = air_date;
        this.episode_number = episode_number;
        this.id = id;
        this.name = name;
        this.overview = overview;
        this.season_number = season_number;
        this.still_path = still_path;
    }

    public String getAir_date() {
        return air_date;
    }

    public int getEpisode_number() {
        return episode_number;
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

    public int getSeason_number() {
        return season_number;
    }

    public String getStill_path() {
        return still_path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Episode)) return false;
        Episode episode = (Episode) o;
        return getEpisode_number() == episode.getEpisode_number() && getId() == episode.getId() && getSeason_number() == episode.getSeason_number() && getAir_date().equals(episode.getAir_date()) && getName().equals(episode.getName()) && getOverview().equals(episode.getOverview()) && getStill_path().equals(episode.getStill_path());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAir_date(), getEpisode_number(), getId(), getName(), getOverview(), getSeason_number(), getStill_path());
    }

    @Override
    public String toString() {
        return "Episode{" +
                "air_date='" + air_date + '\'' +
                ", episode_number=" + episode_number +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", overview='" + overview + '\'' +
                ", season_number=" + season_number +
                ", still_path='" + still_path + '\'' +
                '}';
    }
}
