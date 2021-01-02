package edu.marmara.annotator;

import java.util.ArrayList;

public class User {

    private int userID;
    private String userName;
    private String userType;
    private String userPassword;
    private double consistencyCheckProbability;
    private UserMetrics evaluationMatrix = new UserMetrics();


    public User(int userID, String userName,String userPassword,String userType, double consistencyCheckProbability) {
        Log log = Log.getInstance();

        this.userID = userID;
        this.userName = userName;
        this.userPassword=userPassword;
        this.userType = userType;
        this.consistencyCheckProbability = consistencyCheckProbability;

        log.log(String.format("User %s created as %s.", this.userName, this.userType));
    }

    public String getUserType() {
        return userType;
    }

    public void setUserID(int id) {
        userID = id;
    }

    public void setUserName(String name) {
        userName = name;
    }

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public double getConsistencyCheckProbability() {
        return consistencyCheckProbability;
    }

    public void setConsistencyCheckProbability(double consistencyCheckProbability) {
        this.consistencyCheckProbability = consistencyCheckProbability;
    }

    public UserMetrics getEvaluationMatrix() {
        return evaluationMatrix;
    }

    public void setEvaluationMatrix(UserMetrics evaluationMatrix) {
        this.evaluationMatrix = evaluationMatrix;
    }


    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}