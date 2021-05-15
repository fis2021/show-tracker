package fis.project.st.controllers;

import fis.project.st.exceptions.ShowNotFoundException;
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
class NavBarTest {
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
    void clickOnLogoTest(FxRobot robot) {
        login(robot);

        robot.clickOn("#logo");
    }

    @Test
    void clickOnHomepageTest(FxRobot robot) {
        login(robot);

        robot.clickOn("#homepage");
    }

    @Test
    void clickOnWatchlistTest(FxRobot robot) {
        login(robot);

        robot.clickOn("#watchlist");
    }

    @Test
    void clickOnWatchNextTest(FxRobot robot) {
        login(robot);

        robot.clickOn("#watchNext");
    }

    @Test
    void successfulSearchTest(FxRobot robot) throws ShowNotFoundException {
        login(robot);

        robot.clickOn("#searchField");
        robot.write("Skins");
        robot.clickOn("#searchBtn");
        assertThat((NavBarController.getFoundShows().size())).isGreaterThan(0);
        robot.clickOn("#searchGrid");
    }

    @Test
    void noShowsFoundTest(FxRobot robot) {
        login(robot);
        try {
            robot.clickOn("#searchField");
            robot.write("");
            robot.clickOn("#searchBtn");
            assertThat(0).isEqualTo(NavBarController.getFoundShows().size());
            assertThat(robot.lookup("searchMessage").queryText()).hasText("Show not found!");
        } catch (ShowNotFoundException e) {
            e.printStackTrace();
        }

        try {
            robot.clickOn("#searchField");
            robot.write("adssaasda");
            robot.clickOn("#searchBtn");
            assertThat(0).isEqualTo(NavBarController.getFoundShows().size());
            assertThat(robot.lookup("searchMessage").queryText()).hasText("Show not found!");
        } catch (ShowNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void currentUserTest(FxRobot robot) {
        login(robot);

        assertThat(robot.lookup("#currentUser").queryText()).hasText(USERNAME);
    }
}