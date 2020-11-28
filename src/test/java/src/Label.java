package src;

import java.util.LinkedList;

public class Label {

    private String labelText;
    private long labelID;
    private String datasetName;
    private long datasetID;
    private long lblPerIns;
    private LinkedList linkedList;

    public Label(){}

    public void setLabelText(String text){
        labelText = text;
    }
    public String getLabelText(){
        return labelText;
    }
    public void setLabelID(int labelID) {
        this.labelID = labelID;
    }
    public long getLabelID() {
        return labelID;
    }
    public String getDatasetName() {
        return datasetName;
    }
    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }
    public long getDatasetID() {
        return datasetID;
    }
    public void setDatasetID(long datasetID) {
        this.datasetID = datasetID;
    }
    public long getLblPerIns() {
        return lblPerIns;
    }
    public void setLblPerIns(long lblPerIns) {
        this.lblPerIns = lblPerIns;
    }

    public void Create(long labelID, String labelText, String datasetName, long datesetID, long lblPerIns){

        this.labelID = labelID;
        this.labelText=labelText;
        this.datasetID = datesetID;
        this.datasetName = datasetName;
        this.lblPerIns = lblPerIns;

    }


}
