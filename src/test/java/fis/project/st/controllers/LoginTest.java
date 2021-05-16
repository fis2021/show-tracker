package fis.project.st.controllers;

import fis.project.st.exceptions.UsernameAlreadyExistsException;
import fis.project.st.model.User;
import fis.project.st.services.FileSystemService;
import fis.project.st.services.UserService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class LoginTest {
    public static final String USERNAME_1 = "user1";
    public static final String PASSWORD_1 = "password1";

    public static final String USERNAME_2 = "user2";
    public static final String PASSWORD_2 = "password2";

    @BeforeAll
    static void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER = ".test-show-tracker";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        populateDatabase();
    }

    @AfterAll
    static void tearDown() {
        UserService.database.close();
        System.out.println("After each");
    }

    @Start
    void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        primaryStage.setTitle("Show tracker");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private static void populateDatabase() throws UsernameAlreadyExistsException {
        User user1 = new User(USERNAME_1, PASSWORD_1);
        User user2 = new User(USERNAME_2, PASSWORD_2);
        UserService.addUser(user1.getUsername(), user1.getPassword(), null, null);
        UserService.addUser(user2.getUsername(), user2.getPassword(), null, null);
    }

    @Test
    void successfulLoginTest(FxRobot robot) {
        robot.clickOn("#username");
        robot.write(USERNAME_1);
        robot.clickOn("#password");
        robot.write(PASSWORD_1);
        robot.clickOn("#loginBtn");
        assertThat(LoginController.getCurrentUser().getUsername()).isEqualTo(USERNAME_1);
    }

    @Test
    void wrongPasswordTest(FxRobot robot) {
        robot.clickOn("#username");
        robot.write(USERNAME_1);
        robot.clickOn("#password");
        robot.write(PASSWORD_2);
        robot.clickOn("#loginBtn");
        assertThat(robot.lookup("#loginMessage").queryText()).hasText("The password is incorrect. Please try again!");
    }

    @Test
    void noExistAccountTest(FxRobot robot) {
        robot.clickOn("#username");
        robot.write(USERNAME_1 + "1");
        robot.clickOn("#password");
        robot.write(PASSWORD_1);
        robot.clickOn("#loginBtn");
        assertThat(robot.lookup("#loginMessage").queryText()).hasText("This username doesn't exist!");
    }

    @Test
    void registerRedirectTest(FxRobot robot) {
        robot.clickOn("#goToRegisterBtn");
    }

    @Test
    void homepageRedirectTest(FxRobot robot) {
        robot.clickOn("#username");
        robot.write(USERNAME_1);
        robot.clickOn("#password");
        robot.write(PASSWORD_1);
        robot.clickOn("#loginBtn");
    }
}