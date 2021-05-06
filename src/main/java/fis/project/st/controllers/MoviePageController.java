package fis.project.st.controllers;

import fis.project.st.exceptions.UsernameAlreadyExistsException;
import fis.project.st.model.Movie;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.controlsfx.control.Rating;
import javafx.scene.control.TextField;
import static fis.project.st.controllers.LoginController.getCurrentUser;

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

    @FXML
    private TextField comment_field;

    @FXML
    private TextArea users_comments_area;

    @FXML
    private Text added_comm_message;


    private Movie movie;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movie = (Movie) HomepageController.getSelectedShow();
        title.setText(movie.getName());
        title.setWrappingWidth(400);
        overview.setText(movie.getOverview());
        overview.setWrappingWidth(600);
        Image image = new Image("https://image.tmdb.org/t/p/w500" + movie.getBackdrop_path());
        showBackdrop.setImage(image);
        vote_average.setText(String.valueOf(movie.getVote_average()));
        release_date.setText(movie.getRelease_date());
        runtime.setText(String.valueOf(movie.getRuntime()));
        //database rating
        ArrayList<String> movies = UserService.getMovies(getCurrentUser().getUsername());
        int index = movies.indexOf(movie.getName());
        ArrayList<String> moviesRates = UserService.getMoviesRates(getCurrentUser().getUsername());
        user_vote_field.setRating(Double.parseDouble(moviesRates.get(index)));
        //database comment
        ArrayList<String> movieUserCommentsPerMovie = UserService.getUsersCommentsPerMovie(movie.getName());
        String usersComments = "";
        for (String s : movieUserCommentsPerMovie) {
            usersComments = usersComments + s;
        }
        users_comments_area.setText(usersComments);

        user_vote_field.ratingProperty().addListener(new ChangeListener<Number>() { //action event
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if(UserService.checkIfShowIsInWatchlist(getCurrentUser().getUsername(), movie)) {
                    UserService.addMovieUserVote(getCurrentUser().getUsername(), t1.toString(), movie.getName());
                }
                else{
                    added_comm_message.setText("You must follow the show!");
                    user_vote_field.setRating(0.0);
                }
            }
        });


    }

    public void addComment() {
        if (UserService.checkIfShowIsInWatchlist(getCurrentUser().getUsername(), movie)) {

            UserService.addMovieUserComment(getCurrentUser().getUsername(), movie.getName(), comment_field.getText());
            added_comm_message.setText("Your comment was added!");
            comment_field.setText("");
            ArrayList<String> movieUserCommentsPerMovie = UserService.getUsersCommentsPerMovie(movie.getName());
            String usersComments = "";
            for (String s : movieUserCommentsPerMovie) {
                usersComments = usersComments + s;
            }
            users_comments_area.setText(usersComments);
        } else {
            added_comm_message.setText("You must follow the show!");
        }
    }
}
