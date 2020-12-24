package edu.marmara.annotator;

import java.util.ArrayList;

public class User {

    private int userID;
    private String userName;
    private String userType = "RandomBot";
    private ArrayList<Integer> assignedDataset;
    private double consistencyCheckProbability;
    private UserMetrics evaluationMatrix = new UserMetrics();


    public User(int userID, String userName, String userType, ArrayList<Integer> assignedDataset, double consistencyCheckProbability) {
        Log log = Log.getInstance();

        this.userID = userID;
        this.userName = userName;
        this.userType = userType;
        this.assignedDataset = assignedDataset;
        this.consistencyCheckProbability = consistencyCheckProbability;
        this.evaluationMatrix.setUserID(this.getUserID());

        log.log(String.format("User %s created as %s.", this.userName, this.userType));
    }

    User() {
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

    public ArrayList<Integer> getAssignedDataset() {
        return assignedDataset;
    }

    public void setAssignedDataset(ArrayList<Integer> assignedDataset) {
        this.assignedDataset = assignedDataset;
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


}