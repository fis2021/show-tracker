package fis.project.st.controllers;

import fis.project.st.model.Show;
import fis.project.st.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WatchlistPageController implements Initializable {
    @FXML
    private GridPane watchGrid;

    private ClickListener clickListener;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clickListener = show -> HomepageController.setSelectedShow(HomepageController.getShow(show));
        int column = 1, row = 1;
        watchGrid.setVgap(10);
        watchGrid.setHgap(10);
        ArrayList<Show> shows;
        shows = User.getFollowedShows(); //array of shows found on a search
        for (Show show : shows) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/showLayoutLabel.fxml"));
            AnchorPane anchorPane = null;
            try {
                anchorPane = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (column == 6) {
                column = 1;
                row++;
            }

            ShowLayoutLabelController showLayoutController = fxmlLoader.getController();
            showLayoutController.setData(show, clickListener);

            watchGrid.add(anchorPane, column++, row);

            watchGrid.setMinHeight(Region.USE_COMPUTED_SIZE);
            watchGrid.setMinWidth(Region.USE_COMPUTED_SIZE);
            watchGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
            watchGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);
            watchGrid.setMaxHeight(Region.USE_PREF_SIZE);
            watchGrid.setMaxWidth(Region.USE_PREF_SIZE);
        }
    }
}
