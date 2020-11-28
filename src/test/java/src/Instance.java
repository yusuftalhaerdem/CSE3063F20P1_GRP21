package src;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Instance {

    private String instanceText;
    private long instanceID;
    private long datasetID;
    private long lblPerIns;
    private String datasetName;
    private LinkedList linkedList;

    public Instance(){}

    public String getInstanceText() {
        return instanceText;
    }
    public void setInstanceText(String instanceText) {
        this.instanceText = instanceText;
    }
    public long getLblPerIns() {
        return lblPerIns;
    }
    public void setLblPerIns(long lblPerIns) {
        this.lblPerIns = lblPerIns;
    }
    public long getDatasetID() { return datasetID; }
    public void setDatasetID(int datasetID) { this.datasetID = datasetID; }
    public String getDatasetName() { return datasetName; }
    public void setDatasetName(String datasetName) { this.datasetName = datasetName; }
    public long getInstanceID() {
        return instanceID;
    }
    public void setInstanceID(int instanceID) {
        this.instanceID = instanceID;
    }

    public void Create(long instanceID, String instanceText, int datasetID, String datasetName, long lblPerIns){

        this.datasetID = datasetID;
        this.datasetName = datasetName;
        this.instanceText = instanceText;
        this.instanceID = instanceID;
        this.lblPerIns = lblPerIns;

    }

}
