package src;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class User {
    private static final Logger logger = Logger.getLogger( User.class.getName());
    FileHandler fileHandler = new FileHandler("app.log",true);

    private int userID;
    private String userName;
    private String userType;

    public FileHandler getFileHandler() {
        return fileHandler;
    }

    User() throws IOException {
        logger.addHandler(fileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
    }

    // set Methods to attributes
    public void setUserID(int id){
        userID = id;
    }
    public void setUserName(String name){
        userName = name;
    }
    public void setUserType(String type){
        userType = type;
    }
    // get methods for attributes
    public int getUserID(){
        return userID;
    }
    public String  getUserName() {
        return userName;
    }
    public String getUserType(){
        return userType;
    }


}
