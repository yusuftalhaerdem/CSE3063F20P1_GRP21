package edu.marmara.annotator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Input {
    private static final Logger logger = Logger.getLogger( Input.class.getName());
    FileHandler fileHandler;

    private JSONObject jsonObject;
    private LinkedList labelLinkedList, instanceLinkedList;
    FileReader reader;
    Object obj;
    JSONParser jsonParser;
    private Dataset datasetInfo;

    private int datasetId,  lblPerIns;
    private String datasetName, inputFileName;

    JSONArray instanceJson;

    Input(FileHandler fileHandler, String inputFileName, LinkedList labelLinkedList, LinkedList instanceLinkedList, Dataset datasetInfo){
        this.fileHandler = fileHandler;
        logger.addHandler(fileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);

        this.inputFileName=inputFileName;
        this.labelLinkedList = labelLinkedList;
        this.instanceLinkedList = instanceLinkedList;
        this.datasetInfo = datasetInfo;
    }

    public void getInputs(){
        try{

            jsonParser=new JSONParser();
            reader=new FileReader(inputFileName);
            obj = jsonParser.parse(reader);
            jsonObject=(JSONObject)obj;

            datasetId = (int)(long)jsonObject.get("dataset id");

            datasetName = (String)jsonObject.get("dataset name");

            lblPerIns = (int)(long)jsonObject.get("maximum number of labels per instance");

            datasetInfo.setDatasetID(datasetId);
            datasetInfo.setDatasetName(datasetName);
            datasetInfo.setMaxNoOfLabelsPerInstance(lblPerIns);

            createLabels();
            createInstances();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    void createLabels() throws IOException {
        JSONArray classLabel = (JSONArray)jsonObject.get("class labels");
        for (int i=0;i<classLabel.size();i++){
            JSONObject address=(JSONObject)classLabel.get(i);

            Label label = new Label(fileHandler);
            labelLinkedList.add(label);
            label.Create(((Long)address.get("label id")).intValue(), (String)address.get("label text"), datasetName, datasetId, lblPerIns);

        }
    }

    void createInstances() throws IOException {
        instanceJson = (JSONArray)jsonObject.get("instances");
        for (int i=0;i<instanceJson.size();i++){
            JSONObject address = (JSONObject)instanceJson.get(i);

            Instance instance = new Instance(fileHandler);
            instanceLinkedList.add(instance);
            instance.Create(((Long)address.get("id")).intValue(), (String)address.get("instance"), datasetId, datasetName, lblPerIns);

        }
    }

}
