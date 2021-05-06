package fis.project.st.controllers;

import fis.project.st.model.Movie;
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

public class MovieLayoutController {

    @FXML
    private ImageView backdrop;

    @FXML
    private Text movie_title;

    @FXML
    private Text release_date;

    private Movie movie;

    private ClickListener clickListener;

    @FXML
    void click(MouseEvent event) throws IOException {
        clickListener.onClickListener(movie);
        Parent homepageViewParent = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("moviePage.fxml")));
        Scene homepageViewScene = new Scene(homepageViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(homepageViewScene);
        window.show();
    }

    public void setData(Movie show, ClickListener clickListener) {
        this.movie = show;
        this.clickListener = clickListener;
        movie_title.setText(show.getName());
        release_date.setText(show.getRelease_date());
        Image image = new Image("https://image.tmdb.org/t/p/w500" + show.getBackdrop_path());
        backdrop.setImage(image);
    }
}
