package fis.project.st.controllers;

import fis.project.st.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import fis.project.st.services.UserService;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static fis.project.st.controllers.LoginController.getCurrentUser;

public class CustomListPageController{

    @FXML
    private GridPane watchGrid;
    @FXML
    private TextField custom_list_name;
    @FXML
    private Text message_field;
    @FXML
    private Button bt1;
    @FXML
    private Text clicked_show_message;

    private static String customListName;

    private ClickListener clickListener;

    public void giveToUserChoices() {

        if(!UserService.isUserCustomListDuplicate(getCurrentUser().getUsername(), custom_list_name.getText())){
        if(!custom_list_name.getText().equals("")){
            customListName = custom_list_name.getText();
            bt1.setVisible(false);
            message_field.setText("Please choose what shows you want to add: ");
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
            shows = UserService.getUserShowsAddedToWatchlist(getCurrentUser().getUsername()); //array of shows found on a search
            assert shows != null;
            for (Show show : shows) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/showLayoutLabelCustomList.fxml"));
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

                ShowLayoutLabelCustomListController showLayoutController = fxmlLoader.getController();
                showLayoutController.setData(show, clickListener);

                watchGrid.add(anchorPane, column++, row);

                watchGrid.setMinHeight(Region.USE_COMPUTED_SIZE);
                watchGrid.setMinWidth(Region.USE_COMPUTED_SIZE);
                watchGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                watchGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                watchGrid.setMaxHeight(Region.USE_PREF_SIZE);
                watchGrid.setMaxWidth(Region.USE_PREF_SIZE);
            }
        }else{
            message_field.setText("Please enter a valid name!");
        }
    }else{
            message_field.setText("A custom list with this name already exists!");
        }
    }

    public static String getCustomListName() {
        return customListName;
    }

}
