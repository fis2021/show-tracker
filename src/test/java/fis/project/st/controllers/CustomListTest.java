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

import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class CustomListTest {
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

    private void setup(FxRobot robot) {
        robot.clickOn("#username");
        robot.write(USERNAME);
        robot.clickOn("#password");
        robot.write(PASSWORD);
        robot.clickOn("#loginBtn");
    }

    private void clickOnMovie(FxRobot robot) {
        robot.clickOn("#gridMovies");
        robot.clickOn("#watchlistButton");
    }

    private void clickOnTv(FxRobot robot) {
        robot.clickOn("#gridTV");
        robot.clickOn("#watchlistButton");
    }

    @Test
    void successfulCreateCustomTest(FxRobot robot) {
        setup(robot);
        clickOnMovie(robot);
        robot.clickOn("#homepage");
        clickOnTv(robot);

        robot.clickOn("#watchlist");
        robot.clickOn("#createCustomButton");
        robot.clickOn("#customName");
        robot.write("List Name");
        robot.clickOn("#customButton");
        assertThat(robot.lookup("#customButton").queryButton()).isInvisible();
        assertThat(robot.lookup("#customMessage").queryText()).hasText("Please choose what shows you want to add: ");
        robot.clickOn("#watchCustomGrid");
        robot.clickOn("#watchlist");

        robot.clickOn("#listView");
        robot.dropBy(0,-90);
        robot.clickOn();
        robot.clickOn("#viewListButton");

        robot.clickOn("#customListGrid");

        robot.clickOn("#watchlist");
        robot.clickOn("#createCustomButton");
        robot.clickOn("#customName");
        robot.write("");
        robot.clickOn("#customButton");
        assertThat(robot.lookup("#customMessage").queryText()).hasText("Please enter a valid name!");

        robot.clickOn("#customName");
        robot.write("a(b");
        robot.clickOn("#customButton");
        assertThat(robot.lookup("#customMessage").queryText()).hasText("Please enter a valid name!");

        robot.clickOn("#customName");
        robot.dropBy(20, 0);
        robot.clickOn();
        robot.eraseText("a(b".length());
        robot.write("List Name");
        robot.clickOn("#customButton");
        assertThat(robot.lookup("#customMessage").queryText()).hasText("A custom list with this name already exists!");

    }
}