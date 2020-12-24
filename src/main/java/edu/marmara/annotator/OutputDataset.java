package edu.marmara.annotator;

import java.util.ArrayList;

public class OutputDataset {
    private int datasetId;
    private String datasetName;
    private int lblPerIns;
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
