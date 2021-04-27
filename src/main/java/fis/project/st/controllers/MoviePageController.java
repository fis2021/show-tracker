package fis.project.st.controllers;

import fis.project.st.model.Movie;
import fis.project.st.model.Show;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class MoviePageController implements Initializable {

    @FXML
    private Text title;

    @FXML
    private Text overview;

    @FXML
    private ImageView showBackdrop;

    @FXML
    private Text vote_average;

    @FXML
    private Text release_date;
    @FXML
    private Text runtime;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Movie movie = (Movie) HomepageController.getSelectedShow();
        title.setText(movie.getName());
        title.setWrappingWidth(600);
        overview.setText(movie.getOverview());
        overview.setWrappingWidth(600);
        Image image = new Image("https://image.tmdb.org/t/p/w500" + movie.getBackdrop_path());
        showBackdrop.setImage(image);
        vote_average.setText(String.valueOf(movie.getVote_average()));
        release_date.setText(movie.getRelease_date());
        runtime.setText(String.valueOf(movie.getRuntime()));
    }
}
