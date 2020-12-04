package edu.marmara.annotator;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Dataset{
    private static final Logger logger = Logger.getLogger( Dataset.class.getName());
    FileHandler fileHandler;

    private int datasetID;
    private String datasetName;
    private int maxNoOfLabelsPerInstance;

    public Dataset(FileHandler fileHandler){
        this.fileHandler = fileHandler;
        logger.addHandler(fileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);

    }


    void setDatasetID(int value){
        this.datasetID = value;
    }
    int getDatasetID(){
        return this.datasetID;
    }
    void setDatasetName(String value){
        this.datasetName = value;
    }
    String getDatasetName(){
        return this.datasetName;
    }
    void setMaxNoOfLabelsPerInstance(int value){
        this.maxNoOfLabelsPerInstance = value;
    }
    int getMaxNoOfLabelsPerInstance(){
        return this.maxNoOfLabelsPerInstance;
    }

}
