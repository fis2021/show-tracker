package fis.project.st.controllers;

import fis.project.st.model.Show;
import fis.project.st.model.TV;
import fis.project.st.model.TVUtil.Episode;
import fis.project.st.model.TVUtil.Season;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TVPageController implements Initializable {
    @FXML
    private Text title;

    @FXML
    private Text overview;

    @FXML
    private Text first_air_date;

    @FXML
    private Text last_air_date;

    @FXML
    private Text number_of_episodes;

    @FXML
    private Text number_of_seasons;

    @FXML
    private Text status;

    @FXML
    private ImageView showBackdrop;

    @FXML
    private Text vote_average;

    @FXML
    private VBox seasonGrid;

    @FXML
    private Text episodeName;

    @FXML
    private Text air_date;

    @FXML
    private Text episode_number;

    @FXML
    private Text season_number;

    @FXML
    private Text overviewEp;

    @FXML
    private ImageView episodeback;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TV tv = (TV) HomepageController.getSelectedShow();
        title.setText(tv.getName());
        title.setWrappingWidth(600);
        overview.setText(tv.getOverview());
        overview.setWrappingWidth(600);
        first_air_date.setText(tv.getFirst_air_date());
        last_air_date.setText(tv.getLast_air_date());
        number_of_episodes.setText(String.valueOf(tv.getNumber_of_episodes()));
        number_of_seasons.setText(String.valueOf(tv.getNumber_of_seasons()));
        status.setText(tv.getStatus());
        Image image = new Image("https://image.tmdb.org/t/p/w500" + tv.getBackdrop_path());
        showBackdrop.setImage(image);
        vote_average.setText(String.valueOf(tv.getVote_average()));

        ArrayList<Season> seasons = tv.getSeasons();
        for(Season season : seasons) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/seasonLayout.fxml"));
            try {
                AnchorPane anchorPane = fxmlLoader.load();
                SeasonLayoutController seasonLayoutController = fxmlLoader.getController();
                seasonLayoutController.setData(season);
                seasonGrid.getChildren().add(anchorPane);
                seasonGrid.setSpacing(10);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Episode episode = tv.getLast_episode_to_air();
        episodeName.setText(episode.getName());
        air_date.setText(episode.getAir_date());
        episode_number.setText(String.valueOf(episode.getEpisode_number()));
        season_number.setText(String.valueOf(episode.getSeason_number()));
        overviewEp.setText(episode.getOverview());
        image = new Image("https://image.tmdb.org/t/p/w500" + episode.getStill_path());
        episodeback.setImage(image);
    }
}
