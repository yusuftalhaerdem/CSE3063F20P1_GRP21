package edu.marmara.annotator;

import org.json.simple.JSONArray;


public class OutputData {

    private int datasetId;
    private String datasetName;
    private int lblPerIns;
    private JSONArray classLabels;
    private JSONArray instances;
    private JSONArray labelAssignments;
    private JSONArray users;

    public OutputData(int datasetId, String datasetName, int lblPerIns, JSONArray classLabels,
                      JSONArray instances, JSONArray labelAssignments, JSONArray users){

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

    public JSONArray getClassLabels() {
        return classLabels;
    }

    public JSONArray getInstances() {
        return instances;
    }

    public JSONArray getLabelAssignments() {
        return labelAssignments;
    }

    public JSONArray getUsers() {
        return users;
    }

    public OutputData(){
    }
}
