package fis.project.st.controllers;

import fis.project.st.exceptions.UsernameAlreadyExistsException;
import fis.project.st.model.Show;
import fis.project.st.model.User;
import fis.project.st.requests.requests;
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

import java.util.ArrayList;

@ExtendWith(ApplicationExtension.class)
class HomepageTest {
    public static final String USERNAME = "user1";
    public static final String PASSWORD = "password1";

    @BeforeAll
    static void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER = ".test-show-tracker";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        populateDatabase();
    }

    private static void populateDatabase() throws UsernameAlreadyExistsException {
        User user1 = new User(USERNAME, PASSWORD);
        requests reqMovies = new requests();
        String movieResponse = reqMovies.getData("/discover/movie?", "");
        ArrayList<Show> movies = reqMovies.getBaseData(movieResponse, "movies");
        ArrayList<String> m = new ArrayList<>();
        for (Show show : movies) {
            m.add(show.getName());
        }
        requests reqTV = new requests();
        String tvResponse = reqTV.getData("/discover/tv?", "");
        ArrayList<Show> tvs = reqTV.getBaseData(tvResponse, "tv");
        ArrayList<String> t = new ArrayList<>();
        for (Show show : tvs) {
            t.add(show.getName());
        }
        UserService.addUser(user1.getUsername(), user1.getPassword(), m, t);
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

    private void login(FxRobot robot) {
        robot.clickOn("#username");
        robot.write(USERNAME);
        robot.clickOn("#password");
        robot.write(PASSWORD);
        robot.clickOn("#loginBtn");
    }

    @Test
    void clickOnMovieTest(FxRobot robot) {
        login(robot);

        robot.clickOn("#gridMovies");
    }

    @Test
    void clickOnTVTest(FxRobot robot) {
        login(robot);

        robot.clickOn("#gridTV");
    }
}