package fis.project.st.controllers;

import fis.project.st.exceptions.NoSimilarShowsException;
import fis.project.st.model.Show;
import fis.project.st.requests.requests;
import fis.project.st.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static fis.project.st.controllers.LoginController.getCurrentUser;

public class SimilarButtonController {
    private static ArrayList<Show> similarShows;

    @FXML
    public void handleGetSimilar(ActionEvent event) throws IOException {
        if(HomepageController.getSelectedShow().getType().equals("tv")) {
            requests req = new requests();
            String responseBody = req.getData("/tv/" + HomepageController.getSelectedShow().getId() + "/similar?", "");
            similarShows = req.getBaseData(responseBody, "tv");
        } else {
            requests req = new requests();
            String responseBody = req.getData("/movie/" + HomepageController.getSelectedShow().getId() + "/similar?", "");
            similarShows = req.getBaseData(responseBody, "movies");
        }

        for (Show show : similarShows) {
            if (show.getType().equals("movie")) {
                if (UserService.checkMovieExists(getCurrentUser().getUsername(), show.getName()) == 0) {
                    UserService.AddMovieToUser(getCurrentUser().getUsername(), show.getName());
                }
            } else {
                if (UserService.checkTvExists(getCurrentUser().getUsername(), show.getName()) == 0) {
                    UserService.AddTvToUser(getCurrentUser().getUsername(), show.getName());
                }
            }
        }

        Parent homepageViewParent = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("similarPage.fxml")));
        Scene homepageViewScene = new Scene(homepageViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(homepageViewScene);
    }

    public static ArrayList<Show> getSimilarShows() throws NoSimilarShowsException {
        if(similarShows.size() == 0) throw new NoSimilarShowsException();
        return similarShows;
    }
}
