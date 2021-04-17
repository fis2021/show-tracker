package fis.project.st.controllers;

import fis.project.st.model.Show;
import fis.project.st.requests.requests;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomepageController implements Initializable {
    @FXML
    private Text username;

    @FXML
    private HBox gridMovies;

    @FXML
    private HBox gridTV;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setText(LoginController.getCurrentUser().getUsername());
        try {
            requests reqMovies = new requests();
            String movieResponse = reqMovies.getData("/discover/movie?");
            ArrayList<Show> movies = reqMovies.getBaseData(movieResponse, "movies");
            createGrid(movies, gridMovies);
            requests reqTV = new requests();
            String tvResponse = reqTV.getData("/discover/tv?");
            ArrayList<Show> tv = reqMovies.getBaseData(tvResponse, "tv");
            createGrid(tv, gridTV);

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
            showLayoutController.setData(show);
            gridMovies.getChildren().add(anchorPane);
            gridMovies.setSpacing(20);
        }
    }
}
