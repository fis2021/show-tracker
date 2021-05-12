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
import javafx.scene.control.TextField;
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

    @FXML
    private Button share_list_button;

    @FXML
    private TextField username_share;

    @FXML
    private Text typein_message;

    @FXML
    private Button submit_button;

    @FXML
    private Text action_message;

    private ClickListener clickListener;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        username_share.setVisible(false);
        typein_message.setVisible(false);
        submit_button.setVisible(false);

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

    public void handleShareList(){
        share_list_button.setVisible(false);
        username_share.setVisible(true);
        typein_message.setVisible(true);
        submit_button.setVisible(true);
    }

    public void handleSubmitShare(){
        if(username_share.getText().equals(getCurrentUser().getUsername())){
            action_message.setText("You can not share a list with yourself!");
        }else
        if(username_share.getText().equals("")){
            action_message.setText("Please enter a valid username!");
        }else
            if (!UserService.checkUserExists(username_share.getText())) {
                action_message.setText("This user does not exists!");
            }
        else{
            ArrayList<Show> currentCustomListShows = UserService.getUserCustomList(getCurrentUser().getUsername(), getCustomListName());
            UserService.addToUserACustomList(username_share.getText(), getCustomListName(), currentCustomListShows, getCurrentUser().getUsername());
            action_message.setText("Share complete!");
            assert currentCustomListShows != null;
            for (Show show : currentCustomListShows) {
                if (show.getType().equals("movie")) {
                    if (UserService.checkMovieExists(username_share.getText(), show.getName()) == 0) {
                        UserService.AddMovieToUser(username_share.getText(), show.getName());
                    }
                } else {
                    if (UserService.checkTvExists(username_share.getText(), show.getName()) == 0) {
                        UserService.AddTvToUser(username_share.getText(), show.getName());
                    }
                }
            }
            }
    }

}
