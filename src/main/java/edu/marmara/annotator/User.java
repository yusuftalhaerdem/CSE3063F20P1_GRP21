package edu.marmara.annotator;

import java.util.Scanner;

public class User {

    private int userID;
    private String userName;


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
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }



    // set Methods to attributes
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