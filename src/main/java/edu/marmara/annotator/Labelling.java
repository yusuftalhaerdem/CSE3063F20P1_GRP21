package edu.marmara.annotator;

import java.util.ArrayList;

class Labelling {

    private int datasetID;
    private Instance instance;
    private ArrayList<Label> labelArrayList;
    private Label finalLabel;
    private User user;
    private String dateTime;
    private double timeSpent;


    Labelling(Dataset dataset, Instance instance, ArrayList<Label> labelArrayList, User user, String dateTime, double timeSpent, Label finalLabel) {
        this.datasetID = dataset.getDatasetID();
        this.instance = instance;
        this.labelArrayList = labelArrayList;
        this.user = user;
        this.dateTime = dateTime;
        this.timeSpent = timeSpent;
        this.finalLabel = finalLabel;
    }

    Labelling(Dataset dataset, Instance instance, ArrayList<Label> labelArrayList, User user, String dateTime) {
        this.datasetID = dataset.getDatasetID();
        this.instance = instance;
        this.labelArrayList = labelArrayList;
        this.user = user;
        this.dateTime = dateTime;
    }

    public Labelling() {
    }

    public int getDataset() {
        return datasetID;
    }

    public void setDataset(int dataset) {
        this.datasetID = dataset;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public ArrayList<Label> getLabelArrayList() {
        return labelArrayList;
    }

    public void setLabelArrayList(ArrayList<Label> labelArrayList) {
        this.labelArrayList = labelArrayList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(double timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Label getFinalLabel() {
        return finalLabel;
    }

    public void setFinalLabel(Label finalLabel) {
        this.finalLabel = finalLabel;
    }
}