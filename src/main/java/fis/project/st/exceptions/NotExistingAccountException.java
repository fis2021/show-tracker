package fis.project.st.exceptions;

public class NotExistingAccountException extends Exception {

    public NotExistingAccountException() {
        super("This username doesn't exist!");
    }
}

