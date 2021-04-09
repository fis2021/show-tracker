package fis.project.st.controllers;

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

import java.io.IOException;

public class RegistrationController {

    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private Text registrationMessage;

    @FXML
    public void handleRegisterAction() {
        try {
            UserService.addUser(usernameField.getText(), passwordField.getText());
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
