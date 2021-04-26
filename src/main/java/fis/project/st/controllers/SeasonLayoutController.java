package fis.project.st.controllers;

import fis.project.st.model.TVUtil.Season;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class SeasonLayoutController {
    @FXML
    private ImageView seasonPoster;

    @FXML
    private Text name;

    @FXML
    private Text air_date;

    @FXML
    private Text episode_count;

    @FXML
    private Text season_number;

    @FXML
    private Text overview;

    private Season season;

    public void setData(Season season) {
        this.season = season;
        Image image = new Image("https://image.tmdb.org/t/p/w500" + season.getPoster_path());
        seasonPoster.setImage(image);
        name.setText(season.getName());
        air_date.setText(season.getAir_date());
        episode_count.setText(String.valueOf(season.getEpisode_count()));
        season_number.setText(String.valueOf(season.getSeason_number()));
        overview.setText(season.getOverview());
    }

}
