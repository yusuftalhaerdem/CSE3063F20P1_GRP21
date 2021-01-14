package edu.marmara.annotator;

public class Label {

    private String labelText;
    private int labelID;
    private String datasetName; //buna da gerek kalmayacak
    private int datasetID;      //bu gidecek dataset gelecek
    private int lblPerIns;      //bunu hiç kullanmıyoruz sanırım sorulup silinsin
    private int numberOfTimes;

    Label(int labelID, String labelText, String datasetName, int datesetID, int lblPerIns) {
        this.labelID = labelID;
        this.labelText = labelText;
        this.datasetID = datesetID;
        this.datasetName = datasetName;
        this.lblPerIns = lblPerIns;
    }

    Label() {
    }

    public void setLabelText(String text) {
        labelText = text;
    }

    public String getLabelText() {
        return labelText;
    }

    public void setLabelID(int labelID) {
        this.labelID = labelID;
    }

    public int getLabelID() {
        return labelID;
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

    public int getNumberOfTimes() {
        return numberOfTimes;
    }

    public void setNumberOfTimes(int numberOfTimes) {
        this.numberOfTimes = numberOfTimes;
    }
}

