package fis.project.st.services;

import fis.project.st.exceptions.NotExistingAccountException;
import fis.project.st.exceptions.WrongPasswordException;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.filters.Filters;
import org.dizitart.no2.objects.ObjectFilter;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.*;
import fis.project.st.exceptions.UsernameAlreadyExistsException;
import fis.project.st.model.User;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static fis.project.st.services.FileSystemService.getPathToFile;
import java.util.ArrayList;

public class UserService {

    private static ObjectRepository<User> userRepository;

    public static void initDatabase() {
        Nitrite database = Nitrite.builder()
                .filePath(getPathToFile("show-tracker.db").toFile())
                .openOrCreate("admin", "admin");

        userRepository = database.getRepository(User.class);

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
                if(user.checkIfMovieExists(moviename) == 1)
                    return 1;
            }
        }
        return 0;
    }

    public static int checkTvExists(String username, String tvname){
        for(User user : userRepository.find()){
            if(Objects.equals(username, user.getUsername())){
                if(user.checkIfTvExists(tvname) == 1)
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

    private static String encodePassword(String salt, String password) {
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
