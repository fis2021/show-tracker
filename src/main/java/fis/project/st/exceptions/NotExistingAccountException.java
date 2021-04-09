package fis.project.st.exceptions;

public class NotExistingAccountException extends Exception {

    private String username;

    public NotExistingAccountException(String username) {
        super(String.format("The %s username doesn't exist!", username));
        this.username = username;
    }


}
