package fis.project.st.services;

import fis.project.st.exceptions.NotExistingAccountException;
import fis.project.st.exceptions.WrongPasswordException;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import fis.project.st.exceptions.UsernameAlreadyExistsException;
import fis.project.st.model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static fis.project.st.services.FileSystemService.getPathToFile;

public class UserService {

    private static ObjectRepository<User> userRepository;

    public static void initDatabase() {
        Nitrite database = Nitrite.builder()
                .filePath(getPathToFile("show-tracker.db").toFile())
                .openOrCreate("admin", "admin");

        userRepository = database.getRepository(User.class);
    }

    public static void addUser(String username, String password) throws UsernameAlreadyExistsException {
        checkUserDoesNotAlreadyExist(username);
        userRepository.insert(new User(username, encodePassword(username, password)));
    }

    private static void checkUserDoesNotAlreadyExist(String username) throws UsernameAlreadyExistsException {
        for (User user : userRepository.find()) {
            if (Objects.equals(username, user.getUsername()))
                throw new UsernameAlreadyExistsException(username);
        }
    }

    public static void checkCredentials(String username, String password) throws WrongPasswordException, NotExistingAccountException {
        int username_found = 0;
        for(User user : userRepository.find()){
            if(Objects.equals(username, user.getUsername())){
                username_found = 1;
                String user_pass_entered = encodePassword(username, password); //encrypt argument password
                if(!user_pass_entered.equals(user.getPassword())) {
                    throw new WrongPasswordException(username);
                }
            }
        }
        if(username_found == 0){
            throw new NotExistingAccountException(username);
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
