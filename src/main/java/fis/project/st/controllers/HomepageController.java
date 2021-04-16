package fis.project.st.controllers;

import fis.project.st.model.Show;
import fis.project.st.requests.requests;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomepageController implements Initializable {
    @FXML
    private Text username;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setText(LoginController.getCurrentUser().getUsername());
    }
}
