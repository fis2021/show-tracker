package fis.project.st.controllers;

import fis.project.st.model.Show;
import fis.project.st.requests.requests;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import fis.project.st.exceptions.UsernameAlreadyExistsException;
import fis.project.st.services.UserService;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable{

    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private Text registrationMessage;

    private static ArrayList<Show> movies;
    private static ArrayList<Show> tvs;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        requests reqMovies = new requests();
        String movieResponse = reqMovies.getData("/discover/movie?", 10);
        movies = reqMovies.getBaseData(movieResponse, "movies");
        requests reqTV = new requests();
        String tvResponse = reqTV.getData("/discover/tv?", 10);
        tvs = reqTV.getBaseData(tvResponse, "tv");
        }

    public ArrayList<String> getmovieNames(){
        ArrayList<String> movienames = new ArrayList<>();

        for(Show show : movies){
            movienames.add(show.getName());
        }
        return movienames;
    }

    public ArrayList<String> gettvsNames(){
        ArrayList<String> tvsnames = new ArrayList<>();

        for(Show show : tvs){
            tvsnames.add(show.getName());
        }
        return tvsnames;
    }

    public void handleRegisterAction() {
        try {
            UserService.addUser(usernameField.getText(), passwordField.getText(), getmovieNames(), gettvsNames());
            registrationMessage.setText("Account created successfully!");
        } catch (UsernameAlreadyExistsException e) {
            registrationMessage.setText(e.getMessage());
        }
    }
    public void changeSceneLogin(ActionEvent event) throws IOException {   //goes to login scene
        Parent registerViewParent = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        Scene registerViewScene = new Scene(registerViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(registerViewScene);
        window.show();
    }

}
