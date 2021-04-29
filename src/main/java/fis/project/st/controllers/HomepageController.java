package fis.project.st.controllers;

import fis.project.st.model.Show;
import fis.project.st.requests.requests;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomepageController implements Initializable {
    private static Show selectedShow;
    @FXML
    private HBox gridMovies;
    @FXML
    private HBox gridTV;

    private ClickListener clickListener;

    public static Show getSelectedShow() {
        return selectedShow;
    }
    public static void setSelectedShow(Show selectedShow) {
        HomepageController.selectedShow = selectedShow;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clickListener = show -> {
            selectedShow = getShow(show);
        };
        try {
            requests reqMovies = new requests();
            String movieResponse = reqMovies.getData("/discover/movie?", 10);
            ArrayList<Show> movies = reqMovies.getBaseData(movieResponse, "movies"); //20 movies
            createGrid(movies, gridMovies);
            requests reqTV = new requests();
            String tvResponse = reqTV.getData("/discover/tv?", 10);
            ArrayList<Show> tvs = reqTV.getBaseData(tvResponse, "tv"); //20 tvs
            createGrid(tvs, gridTV);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createGrid(ArrayList<Show> shows, HBox gridMovies) throws IOException {
        for (Show show : shows) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/showLayout.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();

            ShowLayoutController showLayoutController = fxmlLoader.getController();
            showLayoutController.setData(show, clickListener);
            gridMovies.getChildren().add(anchorPane);
            gridMovies.setSpacing(20);
        }
    }

    public static Show getShow(Show show) {
        Show showData;
        if (show.getType().equals("movie")) {
            requests reqMovie = new requests();
            String movieResponse = reqMovie.getData("/movie/" + show.getId() + "?", 10);
            showData = reqMovie.getMovieById(movieResponse);
        } else {
            requests reqTv = new requests();
            String tvResponse = reqTv.getData("/tv/" + show.getId() + "?", 10);
            showData = reqTv.getTVById(tvResponse);
        }

        return showData;
    }

}
