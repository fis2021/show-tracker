package fis.project.st.controllers;

import fis.project.st.model.TV;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class EpisodeLayoutController {
    @FXML
    private ImageView backdrop;

    @FXML
    private Text episode_title;

    @FXML
    private Text name;

    @FXML
    private Text episode_number;

    @FXML
    private Text season_number;

    private TV tv;

    private ClickListener clickListener;

    @FXML
    public void click(MouseEvent event) throws IOException {
        clickListener.onClickListener(tv);
        Parent homepageViewParent = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("tvPage.fxml")));
        Scene homepageViewScene = new Scene(homepageViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(homepageViewScene);
        window.show();
    }

    public void setData(TV show, ClickListener clickListener) {
        this.tv = show;
        this.clickListener = clickListener;
        name.setText(show.getName());
        episode_title.setText(show.getLast_episode_to_air().getName());
        episode_number.setText(String.valueOf(show.getLast_episode_to_air().getEpisode_number()));
        season_number.setText(String.valueOf(show.getLast_episode_to_air().getSeason_number()));
        Image image = new Image("https://image.tmdb.org/t/p/w500" + show.getLast_episode_to_air().getStill_path());
        backdrop.setImage(image);
    }
}


