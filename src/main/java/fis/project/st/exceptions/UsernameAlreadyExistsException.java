package fis.project.st.exceptions;

public class UsernameAlreadyExistsException extends Exception {

    public UsernameAlreadyExistsException() {
        super("An account with this username already exists!");
    }
}
