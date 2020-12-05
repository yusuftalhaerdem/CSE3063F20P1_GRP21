package edu.marmara.annotator;

import java.util.Scanner;
import java.util.logging.Logger;

public class User {
    private static final Logger logger = Logger.getLogger( User.class.getName());

    private int userID;
    private String userName;
    private String userType = "RandomBot";

    User() {
        this.userName = askUserName();
        this.userID = askUserId();
    }


    String askUserName(){
        System.out.println("Enter user name : ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    int askUserId(){
        System.out.println("Enter user id : ");
        int id = 0;
        try {
            Scanner sc = new Scanner(System.in);
            id = sc.nextInt();
            Main.log(logger, String.format("User %s created as %s", this.userName,this.userType));
        }catch (Exception x){
            logger.warning("Please provide a valid user id");
            System.exit(1);
        }
        return id;
    }

    public String getUserType() { return userType; }
    public void setUserID(int id){
        userID = id;
    }
    public void setUserName(String name){
        userName = name;
    }
    public int getUserID(){
        return userID;
    }
    public String  getUserName() {
        return userName;
    }


}