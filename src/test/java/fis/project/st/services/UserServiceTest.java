package fis.project.st.services;

import fis.project.st.controllers.LoginController;
import fis.project.st.exceptions.NotExistingAccountException;
import fis.project.st.exceptions.UsernameAlreadyExistsException;
import fis.project.st.exceptions.WrongPasswordException;
import fis.project.st.model.Show;
import fis.project.st.model.User;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.assertions.api.Assertions.assertThat;

class UserServiceTest {

    public static final String ADMIN = "admin";
    public static final ArrayList<String> movies = new ArrayList<String>() {
        {
            add("a");
            add("b");
            add("c");
        }
    };
    public static final ArrayList<String> tvs = new ArrayList<String>(){
        {
            add("a1");
            add("b1");
            add("c1");
        }
    };
    public static final Show show = new Show("this is a show name");
    public static final Show show1 = new Show("this is another show name");
    public static final String listName = "this is a list name";
    public static final String listName1 = "this is a new list name";

    @BeforeAll
    static void beforeAll() {
        System.out.println("before class");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("after class");
    }

    @BeforeEach
    void setUp() throws IOException {
        FileSystemService.APPLICATION_FOLDER = ".test-show-tracker";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
    }

    @AfterEach
    void tearDown() {
        UserService.database.close();
    }

    @Test
    @DisplayName("Database is initialized and there are no users")
    void testDatabaseInitializedAndNoUserPersisted() {
        assertThat(UserService.getAllUsers()).isNotNull();
        assertThat(UserService.getAllUsers()).isEmpty();
    }

    @Test
    void testUserIsAddedToDatabase() throws UsernameAlreadyExistsException {

        UserService.addUser(ADMIN, ADMIN, movies, tvs);
        assertThat(UserService.getAllUsers()).isNotEmpty();
        assertThat(UserService.getAllUsers()).size().isEqualTo(1);
        User user = UserService.getAllUsers().get(0);
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(ADMIN);
        assertThat(user.getPassword()).isEqualTo(UserService.encodePassword(ADMIN, ADMIN));
        assertThat(user.getMovies()).isEqualTo(movies);
        assertThat(user.getTvs()).isEqualTo(tvs);
        assertThat(UserService.checkUserExists(ADMIN)).isEqualTo(true);
    }

    @Test
    @DisplayName("User can not be added twice")
    void testUserCanNotBeAddedTwice(){
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            UserService.addUser(ADMIN, ADMIN, movies, tvs);
            UserService.addUser(ADMIN, ADMIN, movies, tvs);
        });
    }

    @Test
    @DisplayName("If a user is already added, we can log in with his credentials")
    void testCheckCredentials() throws UsernameAlreadyExistsException, NotExistingAccountException, WrongPasswordException {

        UserService.addUser(ADMIN, ADMIN, movies, tvs);
        assertThrows(NotExistingAccountException.class, () -> {
            UserService.checkCredentials(ADMIN + "a", ADMIN);
        });
        assertThrows(WrongPasswordException.class, () -> {
            UserService.checkCredentials(ADMIN, ADMIN + "a");
        });

        UserService.checkCredentials(ADMIN , ADMIN);
    }


    @Test
    @DisplayName("Test if a show, either movie or Tv, is saved in the database on registration")
    void testIfShowIsSavedInTheDatabase() throws UsernameAlreadyExistsException {

        UserService.addUser(ADMIN, ADMIN, movies, tvs);
        assertThat(UserService.getMovies(ADMIN)).isNotNull();
        assertThat(UserService.getTvs(ADMIN)).isNotNull();
        assertThat(UserService.getMovies(ADMIN)).size().isEqualTo(3);
        assertThat(UserService.getTvs(ADMIN)).size().isEqualTo(3);
        assertThat(movies).isEqualTo(UserService.getMovies(ADMIN));
        assertThat(tvs).isEqualTo(UserService.getTvs(ADMIN));
    }

    @Test
    @DisplayName("When a user rates a movie, their rating star saves in the database")
    void testIfMovieRateIsSavedInTheDatabase() throws UsernameAlreadyExistsException {
        int index = 1;
        String rate = "2.0";
        UserService.addUser(ADMIN, ADMIN, movies, tvs);
        UserService.addMovieUserVote(ADMIN, rate, movies.get(index));
        ArrayList<String> userVotes = UserService.getMoviesRates(ADMIN);
        assertThat(userVotes).isNotNull();
        assertThat(userVotes).isNotEmpty();
        assertThat(userVotes.get(index)).isNotNull();
        assertThat(rate).isEqualTo(userVotes.get(index));
    }

    @Test
    @DisplayName("When a user rates a TV, their rating star saves in the database")
    void testIfTvRateIsSavedInTheDatabase() throws UsernameAlreadyExistsException {
        int index = 1;
        String rate = "2.0";
        UserService.addUser(ADMIN, ADMIN, movies, tvs);
        UserService.addTvsUserVote(ADMIN, rate, tvs.get(index));
        ArrayList<String> userVotes = UserService.getTvsRates(ADMIN);
        assertThat(userVotes).isNotNull();
        assertThat(userVotes).isNotEmpty();
        assertThat(userVotes.get(index)).isNotNull();
        assertThat(rate).isEqualTo(userVotes.get(index));
    }

    @Test
    @DisplayName("When a user leaves a comment to a movie, their comment saves in the database")
    void testIfMovieCommentIsSavedInTheDatabase() throws UsernameAlreadyExistsException {
        int index = 1;
        String comm = "abc";
        UserService.addUser(ADMIN, ADMIN, movies, tvs);
        UserService.addMovieUserComment(ADMIN, movies.get(index), comm);
        assertThat(UserService.getMovieUserComments(ADMIN)).isNotEmpty();
        assertThat(UserService.getMovieUserComments(ADMIN).get(index)).isNotNull();
        assertThat(comm).isEqualTo(UserService.getMovieUserComments(ADMIN).get(index));
    }

    @Test
    @DisplayName("When a user leaves a comment to a TV, their comment saves in the database")
    void testIfTvCommentIsSavedInTheDatabase() throws UsernameAlreadyExistsException {
        int index = 1;
        String comm = "abc";
        UserService.addUser(ADMIN, ADMIN, movies, tvs);
        UserService.addTvUserComment(ADMIN, tvs.get(index), comm);
        assertThat(UserService.getTvUserComments(ADMIN)).isNotEmpty();
        assertThat(UserService.getTvUserComments(ADMIN).get(index)).isNotNull();
        assertThat(comm).isEqualTo(UserService.getTvUserComments(ADMIN).get(index));
    }

    @Test
    @DisplayName("Test if a show, either movie or Tv, exists the database")
    void testCheckShowExists() throws UsernameAlreadyExistsException {
        int index = 1;
        UserService.addUser(ADMIN, ADMIN, movies, tvs);
        assertThat(1).isEqualTo(UserService.checkMovieExists(ADMIN, movies.get(index)));
        assertThat(1).isEqualTo(UserService.checkTvExists(ADMIN, tvs.get(index)));
        assertThat(0).isEqualTo(UserService.checkMovieExists(ADMIN, movies.get(index) + "a"));
        assertThat(0).isEqualTo(UserService.checkMovieExists(ADMIN, movies.get(index) + "a"));
    }

    @Test
    @DisplayName("Test if a movie can be added later to a user")
    void testIfMovieCanBeAddedToUser() throws UsernameAlreadyExistsException {
        String movie = "movie";
        UserService.addUser(ADMIN, ADMIN, movies, tvs);
        UserService.AddMovieToUser(ADMIN, movie);
        assertThat(UserService.getMovies(ADMIN)).isNotEmpty();
        assertThat(UserService.getMovies(ADMIN)).size().isEqualTo(4); // 3 at registration + 1
        assertThat(UserService.getMovies(ADMIN).contains(movie)).isEqualTo(true);
    }

    @Test
    @DisplayName("Test if a tv can be added later to a user")
    void testIfTvCanBeAddedToUser() throws  UsernameAlreadyExistsException{
        String tv = "tv";
        UserService.addUser(ADMIN, ADMIN, movies, tvs);
        UserService.AddTvToUser(ADMIN, tv);
        assertThat(UserService.getTvs(ADMIN)).isNotEmpty();
        assertThat(UserService.getTvs(ADMIN)).size().isEqualTo(4); // 3 at registration + 1
        assertThat(UserService.getTvs(ADMIN).contains(tv)).isEqualTo(true);
    }

    @Test
    @DisplayName("Test if multiple comments on the same movie saves in the database")
    void testIfCommentsOnAMovieSavesInTheDatabase() throws UsernameAlreadyExistsException {
        int index = 1;
        String comm = "abc";
        String comm1 = "def";
        LoginController.setCurrentUser(new User(ADMIN, ADMIN));
        UserService.addUser(ADMIN, ADMIN, movies, tvs);
        UserService.addUser(ADMIN + "a", ADMIN, movies, tvs);
        assertThat(movies.get(index)).isNotNull();
        UserService.addMovieUserComment(ADMIN, movies.get(index), comm);
        UserService.addMovieUserComment(ADMIN + "a", movies.get(index), comm1);
        assertThat(UserService.getUsersCommentsPerMovie(movies.get(index)).get(0)).isNotNull();
        assertThat(UserService.getUsersCommentsPerMovie(movies.get(index)).get(1)).isNotNull();
        assertThat(UserService.getUsersCommentsPerMovie(movies.get(index))).size().isEqualTo(2);
        assertThat(UserService.getUsersCommentsPerMovie(movies.get(index)).get(0).contains(comm)).isEqualTo(true);
        assertThat(UserService.getUsersCommentsPerMovie(movies.get(index)).get(1).contains(comm1)).isEqualTo(true);
    }

    @Test
    @DisplayName("Test if multiple comments on the same tv saves in the database")
    void testIfCommentsOnATvSavesInTheDatabase() throws UsernameAlreadyExistsException{
        int index = 1;
        String comm = "abc";
        String comm1 = "def";
        LoginController.setCurrentUser(new User(ADMIN, ADMIN));
        UserService.addUser(ADMIN, ADMIN, movies, tvs);
        UserService.addUser(ADMIN + "a", ADMIN, movies, tvs);
        assertThat(tvs.get(index)).isNotNull();
        UserService.addTvUserComment(ADMIN, tvs.get(index), comm);
        UserService.addTvUserComment(ADMIN + "a", tvs.get(index), comm1);
        assertThat(UserService.getUsersCommentsPerTv(tvs.get(index)).get(0)).isNotNull();
        assertThat(UserService.getUsersCommentsPerTv(tvs.get(index)).get(1)).isNotNull();
        assertThat(UserService.getUsersCommentsPerTv(tvs.get(index))).size().isEqualTo(2);
        assertThat(UserService.getUsersCommentsPerTv(tvs.get(index)).get(0).contains(comm)).isEqualTo(true);
        assertThat(UserService.getUsersCommentsPerTv(tvs.get(index)).get(1).contains(comm1)).isEqualTo(true);
    }

    @Test
    @DisplayName("Test if a show is added to watchlist and it's saved in the database")
    void testIfShowIsAddedToWatchlist() throws UsernameAlreadyExistsException {
        UserService.addUser(ADMIN, ADMIN, movies, tvs);
        UserService.addShowToUserWatchlist(ADMIN, show);
        assertThat(UserService.checkIfShowIsInWatchlist(ADMIN, show)).isEqualTo(true);
    }

    @Test
    @DisplayName("Test if multiple shows that are added to a watchlist are all saved in the database")
    void testIfAllShowsAddedToWatchlistAreSavedInTheDatabase() throws UsernameAlreadyExistsException {
        UserService.addUser(ADMIN, ADMIN, movies, tvs);
        UserService.addShowToUserWatchlist(ADMIN, show);
        UserService.addShowToUserWatchlist(ADMIN, show1);
        ArrayList<Show> addedShows = UserService.getUserShowsAddedToWatchlist(ADMIN);
        assertThat(addedShows).size().isNotNull();
        assertThat(addedShows).size().isEqualTo(2);
        assertThat(addedShows.get(0).getName().contains(show.getName())).isEqualTo(true);
        assertThat(addedShows.get(1).getName().contains(show1.getName())).isEqualTo(true);
    }

    @Test
    @DisplayName("Test if a show is deleted from user watchlist and changes are applied to database too")
    void testIfAShowIsDeletedFromWatchlist() throws UsernameAlreadyExistsException {
        UserService.addUser(ADMIN, ADMIN, movies, tvs);
        UserService.addShowToUserWatchlist(ADMIN, show);
        UserService.deleteShowFromWatchlist(ADMIN, show);
        assertThat(UserService.checkIfShowIsInWatchlist(ADMIN, show)).isEqualTo(false);
    }

    @Test
    @DisplayName("Test if one or multiple shows are added to a new customlist created by user")
    void testIfShowIsAddedToCustomList() throws UsernameAlreadyExistsException {
        UserService.addUser(ADMIN, ADMIN, movies, tvs);
        UserService.addToUserCustomList(ADMIN, listName, show);
        UserService.addToUserCustomList(ADMIN, listName, show1);
        assertThat(UserService.getUserCustomList(ADMIN, listName)).isNotNull();
        assertThat(UserService.getUserCustomList(ADMIN, listName).get(0)).isNotNull();
        assertThat(UserService.getUserCustomList(ADMIN, listName).get(1)).isNotNull();
        assertThat(UserService.getUserCustomList(ADMIN, listName)).size().isEqualTo(2);
        assertThat(UserService.getUserCustomList(ADMIN, listName).get(0).getName()).isEqualTo(show.getName());
        assertThat(UserService.getUserCustomList(ADMIN, listName).get(1).getName()).isEqualTo(show1.getName());
    }

    @Test
    @DisplayName("Test if multiple names of customlists are saved in the database")
    void testIfCustomListsNamesAreSaved() throws UsernameAlreadyExistsException {
        UserService.addUser(ADMIN, ADMIN, movies, tvs);
        UserService.addToUserCustomList(ADMIN, listName, show);
        UserService.addToUserCustomList(ADMIN, listName1, show);
        assertThat(UserService.getCustomListNames(ADMIN)).isNotNull();
        assertThat(UserService.getCustomListNames(ADMIN)).size().isEqualTo(2);
        assertThat(UserService.getCustomListNames(ADMIN)).contains(listName);
        assertThat(UserService.getCustomListNames(ADMIN)).contains(listName1);
    }

    @Test
    @DisplayName("Custom lists with the same name won't be added")
    void testIfCustomListIsDuplicate() throws UsernameAlreadyExistsException {
        UserService.addUser(ADMIN, ADMIN, movies, tvs);
        UserService.addToUserCustomList(ADMIN, listName, show);
        UserService.addToUserCustomList(ADMIN, listName, show1);
        assertThat(UserService.isUserCustomListDuplicate(ADMIN, listName)).isEqualTo(true);
        assertThat(UserService.getCustomListNames(ADMIN)).size().isEqualTo(1);
    }

    @Test
    @DisplayName("Test if a custom list is shared properly")
    void testIfCustomListIsShared() throws UsernameAlreadyExistsException {
        UserService.addUser(ADMIN, ADMIN, movies, tvs);
        String fromUser = "this is a name";
        ArrayList<Show> shows = new ArrayList<>();
        shows.add(show);
        shows.add(show1);
        UserService.addToUserACustomList(ADMIN, listName, shows, fromUser);
        assertThat(UserService.getCustomListNames(ADMIN).get(0)).isNotNull();
        assertThat(UserService.getCustomListNames(ADMIN)).size().isEqualTo(1);
        assertThat(UserService.getCustomListNames(ADMIN).get(0)).contains(listName + " (Shared from " + fromUser + ")");
    }
}