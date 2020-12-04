package edu.marmara.annotator;

import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Instance {
    private static final Logger logger = Logger.getLogger( Instance.class.getName());
    FileHandler fileHandler;

    private String instanceText;
    private int instanceID;
    private int datasetID;
    private int lblPerIns;
    private String datasetName;
    private String dateTime;
    private LinkedList<Integer> labels;

    public Instance(FileHandler fileHandler){
        this.fileHandler = fileHandler;
        logger.addHandler(fileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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
        labels = new LinkedList<>();

    }

}
