package fis.project.st.controllers;

import fis.project.st.model.Show;
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

    private static ArrayList<Show> followedShows = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(checkAddedShow(followedShows, HomepageController.getSelectedShow())){
            addBtn.toBack();
        }
    }

    @FXML
    public void handleAddToWatchlist() {
        followedShows.add(HomepageController.getSelectedShow());
        HomepageController.getSelectedShow().setFollowed(true);
        addBtn.toBack();
        for(Show show : followedShows)
            System.out.println(show.getName());
    }

    @FXML
    public void handleRemoveFromWatchlist() {
        followedShows.remove(getAddedShow(followedShows, HomepageController.getSelectedShow()));
        HomepageController.getSelectedShow().setFollowed(false);
        removeBtn.toBack();
        for(Show show : followedShows)
            System.out.println(show.getName());
    }

    public boolean checkAddedShow(ArrayList<Show> shows, Show show) {
        if(shows == null || show == null)
            return false;
        for(Show s : shows) {
            if(s.getId() == show.getId())
                return true;
        }
        return false;
    }

    public int getAddedShow(ArrayList<Show> shows, Show show) {
        if(shows == null || show == null)
            return -1;
        for(int index = 0; index < shows.size(); index++) {
            if(show.getId() == shows.get(index).getId())
                return index;
        }
        return -1;
    }
}
