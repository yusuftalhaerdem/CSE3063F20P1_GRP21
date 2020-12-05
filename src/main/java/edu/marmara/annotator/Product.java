package edu.marmara.annotator;

import java.util.LinkedList;
import java.util.logging.Logger;

public class Product {
    private static final Logger logger = Logger.getLogger(RandomLabeling.class.getName());

    private Label label;
    private String userName, userType;
    private int userID;
    private String dateTime;
    private String datasetName;
    private int datasetID;
    private int lblPerIns;
    private String instanceText;
    private int instanceID;
    private LinkedList<Label> labelList = new LinkedList<>();
    private LinkedList<Label> allLabels;
    private LinkedList<Instance> allInstances;

    public void createProduct(Label label, User user, String dateTime, String datasetName, int datasetID, int lblPerIns, String instanceText,
                              int instanceID, LinkedList<Label> labelLinkedList, LinkedList<Instance> allInstances) {
        this.label = label;
        this.userName = user.getUserName();
        this.userType = user.getUserType();
        this.userID = user.getUserID();
        this.dateTime = dateTime;
        this.datasetName = datasetName;
        this.datasetID = datasetID;
        this.lblPerIns = lblPerIns;
        this.instanceText = instanceText;
        this.instanceID = instanceID;
        this.allInstances = allInstances;
        this.allLabels = labelLinkedList;
        this.labelList.add(label);
        Main.log(logger, ("user id:" + this.userID + " " + this.userName + " tagged instance id:" +
                this.instanceID + " with class label:" + this.label.getLabelID() + " " + this.label.getLabelText()
        + " instance: " +"\"" + this.instanceText + "\""));

    }

    public LinkedList<Instance> getAllInstances() {
        return allInstances;
    }

    public void setAllInstances(LinkedList<Instance> allInstances) {
        this.allInstances = allInstances;
    }

    public Label getLabel() {
        return label;
    }

    public Label getALabel(int labelID) {
        return allLabels.get(labelID);
    }
    public LinkedList<Label> getAllLabels(){
        return allLabels;
    }

    public void setAllLabels(LinkedList<Label> allLabels) {
        this.allLabels = allLabels;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public LinkedList<Label> getLabelList() {
        return labelList;
    }

    public void setLabelList(LinkedList<Label> labelList) {
        this.labelList = labelList;
    }

    private LinkedList<Integer> labels;

    public LinkedList<Integer> getLabels() {
        return labels;
    }

    public void setLabels(LinkedList<Integer> labels) {
        this.labels = labels;
    }


    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public int getDatasetID() {
        return datasetID;
    }

    public void setDatasetID(int datasetID) {
        this.datasetID = datasetID;
    }

    public int getLblPerIns() {
        return lblPerIns;
    }

    public void setLblPerIns(int lblPerIns) {
        this.lblPerIns = lblPerIns;
    }

    public String getInstanceText() {
        return instanceText;
    }

    public void setInstanceText(String instanceText) {
        this.instanceText = instanceText;
    }

    public int getInstanceID() {
        return instanceID;
    }

    public void setInstanceID(int instanceID) {
        this.instanceID = instanceID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }



}