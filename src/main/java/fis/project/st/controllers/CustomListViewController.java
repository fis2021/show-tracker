package fis.project.st.controllers;

import fis.project.st.model.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import fis.project.st.services.UserService;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.controlsfx.control.spreadsheet.Grid;

import static fis.project.st.controllers.LoginController.getCurrentUser;
import static fis.project.st.controllers.WatchlistPageController.getCustomListName;

public class CustomListViewController implements Initializable{

    @FXML
    private Text custom_list_name;

    @FXML
    private GridPane watchGrid;

    private ClickListener clickListener;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        custom_list_name.setText("Welcome to " + getCustomListName());
        clickListener = new ClickListener() {
            @Override
            public void onClickListener(Show show) {
                HomepageController.setSelectedShow(HomepageController.getShow(show));
            }

            @Override
            public void onClickListener(TV tv) {
            }

            @Override
            public void onClickListener(Movie movie) {
            }
        };
        int column = 1, row = 1;
        watchGrid.setVgap(10);
        watchGrid.setHgap(10);
        ArrayList<Show> shows;
        shows = UserService.getUserCustomList(getCurrentUser().getUsername(), getCustomListName());
        assert shows != null;
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
