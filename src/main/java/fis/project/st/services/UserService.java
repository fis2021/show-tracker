package fis.project.st.services;

import fis.project.st.exceptions.NotExistingAccountException;
import fis.project.st.exceptions.WrongPasswordException;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import fis.project.st.exceptions.UsernameAlreadyExistsException;
import fis.project.st.model.User;
import fis.project.st.model.Show;
import static fis.project.st.controllers.LoginController.getCurrentUser;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

import static fis.project.st.services.FileSystemService.getPathToFile;
import java.util.ArrayList;

public class UserService {

    public static Nitrite database;
    private static ObjectRepository<User> userRepository;

    public static void initDatabase() {
        FileSystemService.initDirectory();
        database = Nitrite.builder()
                .filePath(getPathToFile("show-tracker.db").toFile())
                .openOrCreate("admin", "admin");

        userRepository = database.getRepository(User.class);
    }

    public static List<User> getAllUsers(){
        return userRepository.find().toList();
    }


    public static void addUser(String username, String password, ArrayList<String> movies, ArrayList<String> tvs) throws UsernameAlreadyExistsException {
        checkUserDoesNotAlreadyExist(username);
        userRepository.insert(new User(username, encodePassword(username, password), movies, tvs));
    }

    private static void checkUserDoesNotAlreadyExist(String username) throws UsernameAlreadyExistsException {
        for (User user : userRepository.find()) {
            if (Objects.equals(username, user.getUsername()))
                throw new UsernameAlreadyExistsException();
        }
    }

    public static void checkCredentials(String username, String password) throws WrongPasswordException, NotExistingAccountException {
        int username_found = 0;
        for(User user : userRepository.find()){
            if(Objects.equals(username, user.getUsername())){
                username_found = 1;
                String user_pass_entered = encodePassword(username, password); //encrypt argument password
                if(!user_pass_entered.equals(user.getPassword())) {
                    throw new WrongPasswordException();
                }
            }
        }
        if(username_found == 0){
            throw new NotExistingAccountException();
        }
    }

    public static void addMovieUserVote(String username, String rate, String moviename){
            for(User user : userRepository.find()){
                if(Objects.equals(username, user.getUsername())){

                    user.setMovieRate(rate, moviename);
                    userRepository.update(user);
                }
            }
    }

    public static void addTvsUserVote(String username, String rate, String tvsname){
        for(User user : userRepository.find()){
                if(Objects.equals(username, user.getUsername())){

                    user.setTvsRate(rate, tvsname);
                    userRepository.update(user);
                }
        }
    }

    public static ArrayList<String> getMovies(String username) {
        for (User user : userRepository.find()) {
            if (Objects.equals(username, user.getUsername())) {
                return user.getMovies();
            }
        }
        return null;
    }

    public static ArrayList<String> getMoviesRates(String username){
        for(User user : userRepository.find()){
            if(Objects.equals(username, user.getUsername())){
                return user.getMoviesRates();
            }
        }
        return null;
    }

    public static ArrayList<String> getTvs(String username){
        for(User user : userRepository.find()){
            if(Objects.equals(username, user.getUsername())){
                return user.getTvs();
            }
        }
        return null;
    }

    public static ArrayList<String> getTvsRates(String username){
        for(User user : userRepository.find()){
            if(Objects.equals(username, user.getUsername())){
                return user.getTvsRates();
            }
        }
        return null;
    }

    public static int checkMovieExists(String username, String moviename){
        for(User user : userRepository.find()){
            if(Objects.equals(username, user.getUsername())){
                if(user.checkIfMovieExists(moviename))
                    return 1;
            }
        }
        return 0;
    }

    public static int checkTvExists(String username, String tvname){
        for(User user : userRepository.find()){
            if(Objects.equals(username, user.getUsername())){
                if(user.checkIfTvExists(tvname))
                    return 1;
            }
        }
        return 0;
    }

    public static void AddMovieToUser(String username, String moviename){
        for(User user : userRepository.find()){
            if(Objects.equals(username, user.getUsername())){
                    user.addMovie(moviename);
                    userRepository.update(user);
            }
        }
    }

    public static void AddTvToUser(String username, String tvname){
        for(User user : userRepository.find()){
            if(Objects.equals(username, user.getUsername())){
                user.addTv(tvname);
                userRepository.update(user);
            }
        }
    }

    public static void addMovieUserComment(String username, String moviename, String comm){
        for(User user : userRepository.find()){
            if(Objects.equals(username, user.getUsername())){
                user.addMovieComment(comm, moviename);
                userRepository.update(user);
            }
        }
    }

    public static void addTvUserComment(String username, String tvname, String comm){
        for(User user : userRepository.find()){
            if(Objects.equals(username, user.getUsername())){
                user.addTvComment(comm, tvname);
                userRepository.update(user);
            }
        }
    }

    public static ArrayList<String> getMovieUserComments(String username){
        for(User user : userRepository.find()){
            if(Objects.equals(username, user.getUsername())){
                return user.getMovieComments();
            }
        }
        return null;
    }

    public static ArrayList<String> getTvUserComments(String username){
        for(User user : userRepository.find()){
            if(Objects.equals(username, user.getUsername())){
                return user.getTvComments();
            }
        }
        return null;
    }

    public static ArrayList<String> getUsersCommentsPerMovie(String moviename) {
        ArrayList<String> movieComments = new ArrayList<String>();
        for (User user : userRepository.find()) {
            if (user.checkIfMovieExists(moviename)) {
                if (!user.getMovieComment(moviename).equals("")) {
                    if (user.getUsername().equals(getCurrentUser().getUsername())) {
                        movieComments.add("Your comment:\n" + user.getMovieComment(moviename) + "\n--------\n");
                    } else {
                        movieComments.add(user.getUsername() + " commented:\n" + user.getMovieComment(moviename) + "\n--------\n");
                    }
                }
            }
        }
        return movieComments;
    }

    public static ArrayList<String> getUsersCommentsPerTv(String tvname){
        ArrayList<String> tvComments = new ArrayList<String>();
        for(User user : userRepository.find()) {
            if (user.checkIfTvExists(tvname)) {
                if (!user.getTvComment(tvname).equals("")) {
                    if(user.getUsername().equals(getCurrentUser().getUsername())){
                        tvComments.add("Your comment:\n" + user.getTvComment(tvname) + "\n--------\n");
                    }else{
                        tvComments.add(user.getUsername() + " commented:\n" + user.getTvComment(tvname) + "\n--------\n");
                    }
                }
            }
        }
        return tvComments;
    }

    public static void addShowToUserWatchlist(String username, Show givenshow){
        for(User user : userRepository.find()){
            if(Objects.equals(user.getUsername(), username)){
                if(!user.isInWatchlist(givenshow)) {
                    user.addToWatchlist(givenshow);
                    userRepository.update(user);
                }
            }
        }
    }

    public static ArrayList<Show> getUserShowsAddedToWatchlist(String username){
        for(User user : userRepository.find()){
            if(Objects.equals(user.getUsername(), username)){
                return user.getWatchlist();
            }
        }
        return null;
    }

    public static boolean checkIfShowIsInWatchlist(String username, Show show){
        for(User user : userRepository.find()){
            if(Objects.equals(user.getUsername(), username)){
                if(user.isInWatchlist(show)){
                    return true;
                }
            }
        }
        return false;
    }

    public static void deleteShowFromWatchlist(String username, Show show){
        for(User user : userRepository.find()){
            if(Objects.equals(user.getUsername(), username)){
                if(user.isInWatchlist(show)){
                    user.removeFromWatchlist(show);
                    userRepository.update(user);
                }
            }
        }
    }

    public static void addToUserCustomList(String username, String customlistname, Show show){
        for(User user : userRepository.find()){
            if(Objects.equals(user.getUsername(), username)){
                user.addToCustomLists(customlistname, show);
                userRepository.update(user);
            }
        }
    }

    public static void addToUserACustomList(String username, String customlistname, ArrayList<Show> shows, String fromusername){
        for(User user : userRepository.find()){
            if(Objects.equals(user.getUsername(), username)){
                user.addACustomList(customlistname, shows, fromusername);
                userRepository.update(user);
            }
        }
    }

    public static ArrayList<String> getCustomListNames(String username){
        for(User user : userRepository.find()){
            if(Objects.equals(user.getUsername(), username)){
                return user.getCustomListNames();
            }
        }
        return null;
    }

    public static ArrayList<Show> getUserCustomList(String username, String customlistname){
        for(User user : userRepository.find()){
            if(Objects.equals(user.getUsername(), username)){
                return user.getCustomList(customlistname);
            }
        }
        return null;
    }



    public static boolean isUserCustomListDuplicate(String username, String customlistname){
        for(User user : userRepository.find()){
            if(Objects.equals(user.getUsername(), username)){
                if(user.isCustomListDuplicate(customlistname)){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkUserExists(String username){
        for(User user : userRepository.find()){
            if(Objects.equals(user.getUsername(), username)){
                return true;
            }
        }
        return false;
    }

    public static String encodePassword(String salt, String password) {
        MessageDigest md = getMessageDigest();
        md.update(salt.getBytes(StandardCharsets.UTF_8));

        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

        // This is the way a password should be encoded when checking the credentials
        return new String(hashedPassword, StandardCharsets.UTF_8)
                .replace("\"", ""); //to be able to save in JSON format
    }

    private static MessageDigest getMessageDigest() {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 does not exist!");
        }
        return md;
    }

}
