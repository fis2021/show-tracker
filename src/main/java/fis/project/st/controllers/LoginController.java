package fis.project.st.controllers;

import fis.project.st.exceptions.NotExistingAccountException;
import fis.project.st.exceptions.WrongPasswordException;
import fis.project.st.model.User;
import fis.project.st.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;


public class LoginController {

    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private Text loginMessage;

    private static User currentUser;

    @FXML
    public void handleLoginAction(ActionEvent event) {    //login button
        try {
            UserService.checkCredentials(usernameField.getText(), passwordField.getText());
            loginMessage.setText("Login successful!");
            currentUser = new User(usernameField.getText(), "");
            Parent homepageViewParent = FXMLLoader.load(getClass().getClassLoader().getResource("homepage.fxml"));
            Scene homepageViewScene = new Scene(homepageViewParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(homepageViewScene);
            window.show();
        } catch (WrongPasswordException e) {
            loginMessage.setText(e.getMessage());
        } catch (NotExistingAccountException e) {
            loginMessage.setText(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeSceneRegister(ActionEvent event) throws IOException {   //goes to register scene
        Parent registerViewParent = FXMLLoader.load(getClass().getClassLoader().getResource("register.fxml"));
        Scene registerViewScene = new Scene(registerViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(registerViewScene);
        window.show();
    }

    public static User getCurrentUser() {
        return currentUser;
    }

}
