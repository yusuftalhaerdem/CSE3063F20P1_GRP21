package edu.marmara.annotator;

import java.util.Scanner;
import java.util.logging.Logger;

public class User {
    private static final Logger logger = Logger.getLogger( User.class.getName());

    private int userID;
    private String userName,userType;


    User() {
        this.userName = askUserName();
        this.userID = askUserId();
        this.userType = "Random Labeling";

    }

    String askUserName(){
        System.out.println("Enter user name : ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    int askUserId(){
        System.out.println("Enter user id : ");
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }



    // set Methods to attributes
    public String getUserType() { return userType; }
    public void setUserID(int id){
        userID = id;
    }
    public void setUserName(String name){
        userName = name;
    }
    // get methods for attributes
    public int getUserID(){
        return userID;
    }
    public String  getUserName() {
        return userName;
    }


}