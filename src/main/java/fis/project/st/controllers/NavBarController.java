package fis.project.st.controllers;

import fis.project.st.exceptions.ShowNotFoundException;
import fis.project.st.model.Show;
import fis.project.st.requests.requests;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static fis.project.st.controllers.LoginController.getCurrentUser;

public class NavBarController implements Initializable {
    private static ArrayList<Show> foundShows;
    @FXML
    private Text username;
    @FXML
    private TextField searchField;

    public static ArrayList<Show> getFoundShows() throws ShowNotFoundException {
        if (foundShows.size() == 0) throw new ShowNotFoundException();
        return foundShows;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setText(getCurrentUser().getUsername());
    }

    @FXML
    public void handleHomepageScene(ActionEvent event) throws IOException {
        Parent homepageViewParent = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("homepage.fxml")));
        Scene homepageViewScene = new Scene(homepageViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(homepageViewScene);
        window.show();
    }

    @FXML
    public void handleHomepageSceneLogo(MouseEvent event) throws IOException {
        Parent homepageViewParent = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("homepage.fxml")));
        Scene homepageViewScene = new Scene(homepageViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(homepageViewScene);
        window.show();
    }

    @FXML
    public void handleSearch(MouseEvent event) throws IOException {
        foundShows = new ArrayList<>();
        String search = searchField.getText();
        requests req = new requests();
        String response = "";
        if (search.length() != 0) {
            response = req.getData("/search/multi?query=" + search.replace(" ", "-").toLowerCase() + "&", 5);
        }
        if (response.length() > 20)
            foundShows = req.getBaseData(response, "");
        Parent homepageViewParent = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("searchPage.fxml")));
        Scene homepageViewScene = new Scene(homepageViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(homepageViewScene);
        window.show();
    }
}


