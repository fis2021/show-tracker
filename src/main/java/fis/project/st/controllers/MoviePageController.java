package fis.project.st.controllers;

import fis.project.st.model.Movie;
import fis.project.st.model.Show;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.controlsfx.control.Rating;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import static fis.project.st.controllers.LoginController.getCurrentUser;
import static fis.project.st.controllers.HomepageController.getSelectedShow;
import fis.project.st.model.User;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;

import fis.project.st.services.UserService;

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

    @FXML
    private Rating user_vote_field;

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
        //database rating
        ArrayList<String> movies = UserService.getMovies(getCurrentUser().getUsername());
        ArrayList<String> moviesRates = UserService.getMoviesRates(getCurrentUser().getUsername());
        int index = movies.indexOf(movie.getName());
        user_vote_field.setRating(Double.parseDouble(moviesRates.get(index)));
        user_vote_field.ratingProperty().addListener(new ChangeListener<Number>() { //action event
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                UserService.addMovieUserVote(getCurrentUser().getUsername(), t1.toString(), movie.getName());
            }
        });
    }
}
