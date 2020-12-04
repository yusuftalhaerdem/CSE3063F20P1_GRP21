package src;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Label{
    private static final Logger logger = Logger.getLogger( Label.class.getName());
    FileHandler fileHandler;

    private String labelText;
    private int labelID;
    private String datasetName;
    private int datasetID;
    private int lblPerIns;

    public Label(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        logger.addHandler(fileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
    }

    public void setLabelText(String text){
        labelText = text;
    }
    public String getLabelText(){
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

    public void Create(int labelID, String labelText, String datasetName, int datesetID, int lblPerIns){

        this.labelID = labelID;
        this.labelText=labelText;
        this.datasetID = datesetID;
        this.datasetName = datasetName;
        this.lblPerIns = lblPerIns;

    }
}
