package fis.project.st.exceptions;

public class WrongPasswordException extends Exception {

    public WrongPasswordException(){
        super("The password is incorrect. Please try again!");
    }
}
