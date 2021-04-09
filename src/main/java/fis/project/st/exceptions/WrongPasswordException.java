package fis.project.st.exceptions;

public class WrongPasswordException extends Exception {

    private String username;

    public WrongPasswordException(String username) {
        super(String.format("The password for %s username is incorrect!", username));
        this.username = username;
    }


}
