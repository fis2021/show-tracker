package fis.project.st.controllers;

import fis.project.st.services.FileSystemService;
import fis.project.st.services.UserService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class RegistrationTest {
    public static final String USERNAME = "user";
    public static final String PASSWORD = "password";

    @BeforeAll
    static void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER = ".test-show-tracker";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
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

    @Test
    void successfulRegistrationTest(FxRobot robot) {
        robot.clickOn("#goToRegisterBtn");
        robot.clickOn("#username");
        robot.write(USERNAME);
        robot.clickOn("#password");
        robot.write(PASSWORD);
        robot.clickOn("#registerBtn");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Account created successfully!");

        assertThat(UserService.getAllUsers()).size().isEqualTo(1);
    }

    @Test
    void moreUsersTest(FxRobot robot) {
        robot.clickOn("#goToRegisterBtn");
        robot.clickOn("#username");
        robot.write(USERNAME + "1");
        robot.clickOn("#password");
        robot.write(PASSWORD);
        robot.clickOn("#registerBtn");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Account created successfully!");

        assertThat(UserService.getAllUsers()).size().isEqualTo(2);

        robot.clickOn("#username");
        robot.write("2");
        robot.clickOn("#password");
        robot.eraseText(PASSWORD.length());
        robot.write(PASSWORD);
        robot.clickOn("#registerBtn");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Account created successfully!");

        assertThat(UserService.getAllUsers()).size().isEqualTo(3);

    }

    @Test
    void takenUsernameTest(FxRobot robot) {
        robot.clickOn("#goToRegisterBtn");
        robot.clickOn("#username");
        robot.write(USERNAME);
        robot.clickOn("#password");
        robot.write(PASSWORD);
        robot.clickOn("#registerBtn");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("An account with this username already exists!");

        assertThat(UserService.getAllUsers()).size().isEqualTo(3);
    }

    @Test
    void backButtonTest(FxRobot robot) {
        robot.clickOn("#goToRegisterBtn");
        robot.clickOn("#loginSceneBtn");
    }
}