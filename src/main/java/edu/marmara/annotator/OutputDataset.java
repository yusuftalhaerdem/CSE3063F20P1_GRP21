package edu.marmara.annotator;

import java.util.ArrayList;

public class OutputDataset {
    private int datasetId;          //bu gidecek
    private String datasetName;     //ve bu da eşlik edecek
    private int lblPerIns;          //buna gerek var mı bilmiyorum bile
    private ArrayList<Object> classLabels;
    private ArrayList<Object> instances;
    private ArrayList<Object> labelAssignments;
    private ArrayList<Object> users;

    public OutputDataset(int datasetId, String datasetName, int lblPerIns,
                         ArrayList<Object> classLabels, ArrayList<Object> instances, ArrayList<Object> labelAssignments, ArrayList<Object> users) {
        this.datasetId = datasetId;
        this.datasetName = datasetName;
        this.lblPerIns = lblPerIns;
        this.classLabels = classLabels;
        this.instances = instances;
        this.labelAssignments = labelAssignments;
        this.users = users;
    }

    public int getDatasetId() {
        return datasetId;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public int getLblPerIns() {
        return lblPerIns;
    }

    public ArrayList<Object> getClassLabels() {
        return classLabels;
    }

    public ArrayList<Object> getInstances() {
        return instances;
    }

    public ArrayList<Object> getLabelAssignments() {
        return labelAssignments;
    }

    public ArrayList<Object> getUsers() {
        return users;
    }

}
