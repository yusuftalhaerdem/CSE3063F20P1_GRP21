package src;

import com.fasterxml.jackson.core.JsonToken;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;

public class Input {

    private JSONObject jsonObject;
    private LinkedList labelLinkedList, instanceLinkedList;
    FileReader reader;
    Object obj;
    JSONParser jsonParser;

    private int datasetId,  lblPerIns;
    private String datasetName, inputFileName;

    JSONArray instanceJson;

    Input( String inputFileName,LinkedList labelLinkedList, LinkedList instanceLinkedList){
        this.inputFileName=inputFileName;
        this.labelLinkedList = labelLinkedList;
        this.instanceLinkedList = instanceLinkedList;
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

            createLabels();
            createInstances();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    void createLabels(){
        JSONArray classLabel = (JSONArray)jsonObject.get("class labels");
        for (int i=0;i<classLabel.size();i++){
            JSONObject address=(JSONObject)classLabel.get(i);

            Label label = new Label();
            labelLinkedList.add(label);
            label.Create(((Long)address.get("label id")).intValue(), (String)address.get("label text"), datasetName, datasetId, lblPerIns);

        }
    }

    void createInstances(){
        instanceJson = (JSONArray)jsonObject.get("instances");
        for (int i=0;i<instanceJson.size();i++){
            JSONObject address = (JSONObject)instanceJson.get(i);

            Instance instance = new Instance();
            instanceLinkedList.add(instance);
            instance.Create(((Long)address.get("id")).intValue(), (String)address.get("instance"), datasetId, datasetName, lblPerIns);

        }
    }
}
