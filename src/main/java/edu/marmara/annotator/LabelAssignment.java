package edu.marmara.annotator;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LabelAssignment {
    private static final Logger logger = Logger.getLogger( LabelAssignment.class.getName());
    FileHandler fileHandler;

    private int instanceID;
    private int[] labelID;

    public LabelAssignment(){}

    public LabelAssignment(FileHandler fileHandler, int instanceID, int[] labelID){
        this.fileHandler = fileHandler;

        logger.addHandler(fileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);

        this.instanceID=instanceID;
        this.labelID=labelID;
    }
    //set methods for attributes
    public void setInstanceID(int id){
        instanceID = id;
    }
    public void setLabelID(int id){
        labelID[instanceID] = id;
    }
    //get methods for attributes
    public int getInstanceID(){
        return instanceID;
    }
    public int getLabelID(){
        return labelID[instanceID];
    }

}
