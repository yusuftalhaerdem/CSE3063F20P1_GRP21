package edu.marmara.annotator;

import java.util.LinkedList;

public class Instance {

    private String instanceText;
    private int instanceID;
    private int datasetID;
    private String datasetName;
    private int maxPerLabel;
    private LinkedList<Integer> labels;

    public Instance(int instanceID, String instanceText, int datasetID, String datasetName, int lblPerIns) {

        this.instanceText = instanceText;
        this.instanceID = instanceID;
        this.datasetID = datasetID;
        this.datasetName = datasetName;
        this.maxPerLabel = lblPerIns;
        labels = new LinkedList<>();

    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public int getMaxPerLabel() {
        return maxPerLabel;
    }

    public void setMaxPerLabel(int maxPerLabel) {
        this.maxPerLabel = maxPerLabel;
    }


    public int getDatasetID() {
        return datasetID;
    }

    public void setDatasetID(int datasetID) {
        this.datasetID = datasetID;
    }

    public void setLabels(LinkedList<Integer> labels) {
        this.labels = labels;
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

    public LinkedList<Integer> getLabels() {
        return labels;
    }

    public void setLabel(int labelID) {
        labels.add(labelID);
    }


}
