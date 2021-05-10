package fis.project.st.controllers;

import fis.project.st.model.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import fis.project.st.services.UserService;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static fis.project.st.controllers.LoginController.getCurrentUser;

public class WatchlistPageController implements Initializable {
    @FXML
    private GridPane watchGrid;

    @FXML
    private ListView<String> listview;

    private ClickListener clickListener;

    private static String customListName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<String> customListNames = UserService.getCustomListNames(getCurrentUser().getUsername());
        listview.setBackground(new Background(new BackgroundFill(Color.valueOf("#1B1A20"), null, null)));
        listview.setPadding(new Insets(0));
        for(String listname : customListNames){
            listview.getItems().add(listname);
        }
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
        shows = UserService.getUserShowsAddedToWatchlist(getCurrentUser().getUsername());
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

    public void handleCreateCustomList(ActionEvent event) throws IOException{
        Parent registerViewParent = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("customListPage.fxml")));
        Scene registerViewScene = new Scene(registerViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(registerViewScene);
        window.show();
    }

    public static void setCustomListName(String name){
        customListName = name;
    }

    public static String getCustomListName() {
        return customListName;
    }

    public void accesCustomList(ActionEvent event) throws IOException {
        ObservableList<String> shows;
        shows = listview.getSelectionModel().getSelectedItems();
        String str = "";
        for(String s : shows){
            str = str + s;
        }
        setCustomListName(str);
        Parent registerViewParent = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("customListView.fxml")));
        Scene registerViewScene = new Scene(registerViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(registerViewScene);
        window.show();
    }
}
