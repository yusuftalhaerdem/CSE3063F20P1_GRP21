package src;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Instance {

    private String instanceText;
    private int instanceID;
    private int datasetID;
    private int lblPerIns;
    private String datasetName;
    private LinkedList<Integer> labels;

    public Instance() {
    }

    public String getInstanceText() {
        return instanceText;
    }

    public void setInstanceText(String instanceText) {
        this.instanceText = instanceText;
    }

    public int getLblPerIns() {
        return lblPerIns;
    }

    public void setLblPerIns(int lblPerIns) {
        this.lblPerIns = lblPerIns;
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


    public void Create(int instanceID, String instanceText, int datasetID, String datasetName, int lblPerIns) {

        this.datasetID = datasetID;
        this.datasetName = datasetName;
        this.instanceText = instanceText;
        this.instanceID = instanceID;
        this.lblPerIns = lblPerIns;
        labels = new LinkedList<Integer>();

    }

}
