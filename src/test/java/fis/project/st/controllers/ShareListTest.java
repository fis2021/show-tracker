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
public class ShareListTest {
    public static final String USERNAME = "user1";
    public static final String PASSWORD = "password1";
    public static final String USERNAME1 = "user2";
    public static final String PASSWORD1 = "password1";

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
        User user2 = new User(USERNAME1, PASSWORD1);
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
        UserService.addUser(user2.getUsername(), user2.getPassword(), m, t);
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
    void createCustomTest(FxRobot robot) {
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

        robot.clickOn("#listView");
        robot.dropBy(0,-90);
        robot.clickOn();
        robot.clickOn("#viewListButton");

        robot.clickOn("#shareListButton");
        assertThat(robot.lookup("#shareListButton").queryButton()).isInvisible();
        robot.clickOn("#shareUsername");
        robot.write(USERNAME1);
        robot.clickOn("#submitButton");
        assertThat(robot.lookup("#shareMessage").queryText()).hasText("Share complete!");

        robot.clickOn("#watchlist");

        robot.clickOn("#listView");
        robot.dropBy(0,-90);
        robot.clickOn();
        robot.clickOn("#viewListButton");

        robot.clickOn("#shareListButton");
        assertThat(robot.lookup("#shareListButton").queryButton()).isInvisible();

        robot.clickOn("#shareUsername");
        robot.write("");
        robot.clickOn("#submitButton");
        assertThat(robot.lookup("#shareMessage").queryText()).hasText("Please enter a valid username!");

        robot.clickOn("#shareUsername");
        robot.write(USERNAME);
        robot.clickOn("#submitButton");
        assertThat(robot.lookup("#shareMessage").queryText()).hasText("You can not share a list with yourself!");

        robot.clickOn("#shareUsername");
        robot.write(USERNAME + "3");
        robot.clickOn("#submitButton");
        assertThat(robot.lookup("#shareMessage").queryText()).hasText("This user does not exists!");

    }
}
