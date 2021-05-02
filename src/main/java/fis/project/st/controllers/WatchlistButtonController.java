package fis.project.st.controllers;

import fis.project.st.model.Show;
import fis.project.st.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WatchlistButtonController implements Initializable {
    @FXML
    private Button addBtn;

    @FXML
    private Button removeBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (checkAddedShow(User.getFollowedShows(), HomepageController.getSelectedShow())) {
            addBtn.toBack();
        }
    }

    @FXML
    public void handleAddToWatchlist() {
        User.getFollowedShows().add(HomepageController.getSelectedShow());
        HomepageController.getSelectedShow().setFollowed(true);
        addBtn.toBack();
        for (Show show : User.getFollowedShows())
            System.out.println(show.getName());
    }

    @FXML
    public void handleRemoveFromWatchlist() {
        User.getFollowedShows().remove(getAddedShow(User.getFollowedShows(), HomepageController.getSelectedShow()));
        HomepageController.getSelectedShow().setFollowed(false);
        removeBtn.toBack();
        for (Show show : User.getFollowedShows())
            System.out.println(show.getName());
    }

    public boolean checkAddedShow(ArrayList<Show> shows, Show show) {
        if (shows == null || show == null)
            return false;
        for (Show s : shows) {
            if (s.getId() == show.getId())
                return true;
        }
        return false;
    }

    public int getAddedShow(ArrayList<Show> shows, Show show) {
        if (shows == null || show == null)
            return -1;
        for (int index = 0; index < shows.size(); index++) {
            if (show.getId() == shows.get(index).getId())
                return index;
        }
        return -1;
    }
}
