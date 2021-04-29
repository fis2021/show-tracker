package fis.project.st.controllers;

import fis.project.st.exceptions.ShowNotFoundException;
import fis.project.st.model.Show;
import fis.project.st.model.Movie;
import fis.project.st.model.TV;
import fis.project.st.services.UserService;
import static fis.project.st.controllers.LoginController.getCurrentUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SearchController implements Initializable {
    @FXML
    private GridPane searchGrid;

    @FXML
    private Text errorMsg;

    private ClickListener clickListener;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clickListener = show -> HomepageController.setSelectedShow(HomepageController.getShow(show));
        int column = 0, row = 1;
        searchGrid.setVgap(10);
        searchGrid.setHgap(10);
        ArrayList<Show> shows;
        try {
            shows = NavBarController.getFoundShows(); //array of shows found on a search
            for (Show show : shows) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/showLayout.fxml"));
                AnchorPane anchorPane = null;
                try {
                    anchorPane = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (column == 6) { column = 0; row++; }

                ShowLayoutController showLayoutController = fxmlLoader.getController();
                showLayoutController.setData(show, clickListener);

                searchGrid.add(anchorPane, column++, row);

                searchGrid.setMinHeight(Region.USE_COMPUTED_SIZE);
                searchGrid.setMinWidth(Region.USE_COMPUTED_SIZE);
                searchGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                searchGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                searchGrid.setMaxHeight(Region.USE_PREF_SIZE);
                searchGrid.setMaxWidth(Region.USE_PREF_SIZE);
            }
        } catch (ShowNotFoundException e) {
            errorMsg.setText(e.getMessage());
        }
    }
}
