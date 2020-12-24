package edu.marmara.annotator;

import java.util.ArrayList;

public class Dataset {

    private int datasetID;
    private String datasetName;
    private String datasetPath;
    private ArrayList<Instance> instanceArrayList = new ArrayList<>();
    private ArrayList<Label> labelArrayList = new ArrayList<>();
    private ArrayList<Labelling> labellingArrayList = new ArrayList<>();
    private ArrayList<User> assignedUsersArrayList = new ArrayList<>();
    private DatasetMetrics evaluationMatrix = new DatasetMetrics();
    private int labelPerInstance;

    public Dataset(int datasetID, String datasetName, String datasetPath,
                   ArrayList<User> assignedUsersArrayList) {
        this.datasetID = datasetID;
        this.datasetName = datasetName;
        this.datasetPath = datasetPath;
        this.assignedUsersArrayList = assignedUsersArrayList;
    }

    public ArrayList<Labelling> getLabellingArrayList() {
        return labellingArrayList;
    }

    public void setLabellingArrayList(ArrayList<Labelling> labellingArrayList) {
        this.labellingArrayList = labellingArrayList;
    }

    public ArrayList<Instance> getInstanceArrayList() {
        return instanceArrayList;
    }

    public void setInstanceArrayList(ArrayList<Instance> instanceArrayList) {
        this.instanceArrayList = instanceArrayList;
    }

    public ArrayList<Label> getLabelArrayList() {
        return labelArrayList;
    }

    public void setLabelArrayList(ArrayList<Label> labelArrayList) {
        this.labelArrayList = labelArrayList;
    }

    public Dataset() {
    }

    public int getDatasetID() {
        return datasetID;
    }

    public void setDatasetID(int datasetID) {
        this.datasetID = datasetID;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public ArrayList<User> getAssignedUsersArrayList() {
        return assignedUsersArrayList;
    }

    public void setAssignedUsersArrayList(ArrayList<User> assignedUsersArrayList) {
        this.assignedUsersArrayList = assignedUsersArrayList;
    }

    public DatasetMetrics getEvaluationMatrix() {
        return evaluationMatrix;
    }

    public void setEvaluationMatrix(DatasetMetrics evaluationMatrix) {
        this.evaluationMatrix = evaluationMatrix;
    }

    public int getLabelPerInstance() {
        return labelPerInstance;
    }

    public void setLabelPerInstance(int labelPerInstance) {
        this.labelPerInstance = labelPerInstance;
    }

    public String getDatasetPath() {
        return datasetPath;
    }

    public void setDatasetPath(String datasetPath) {
        this.datasetPath = datasetPath;
    }

    public void getDataset() {

    }
}
