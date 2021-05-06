package fis.project.st.controllers;

import fis.project.st.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

import static fis.project.st.controllers.LoginController.getCurrentUser;

public class WatchlistButtonController implements Initializable {
    @FXML
    private Button addBtn;

    @FXML
    private Button removeBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (UserService.checkIfShowIsInWatchlist(getCurrentUser().getUsername(), HomepageController.getSelectedShow())) {
            addBtn.toBack();
        }
    }

    public void handleAddToWatchlist() {
        UserService.addShowToUserWatchlist(getCurrentUser().getUsername(), HomepageController.getSelectedShow());
        addBtn.toBack();
    }


    public void handleRemoveFromWatchlist() {
        UserService.deleteShowFromWatchlist(getCurrentUser().getUsername(), HomepageController.getSelectedShow());
        removeBtn.toBack();
    }

}
