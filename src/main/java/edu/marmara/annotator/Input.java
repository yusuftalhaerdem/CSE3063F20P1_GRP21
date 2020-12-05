package edu.marmara.annotator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.LinkedList;
import java.util.logging.Logger;

public class Input {
    private static final Logger logger = Logger.getLogger( Input.class.getName());

    private LinkedList<Label> labelLinkedList;
    private LinkedList<Instance> instanceLinkedList;
    private String inputFileName;


    Input( String inputFileName,LinkedList labelLinkedList, LinkedList instanceLinkedList){

        this.inputFileName=inputFileName;
        this.labelLinkedList = labelLinkedList;
        this.instanceLinkedList = instanceLinkedList;
    }


    public void getInputs(){
        try{

            JSONParser jsonParser=new JSONParser();
            FileReader reader=new FileReader(inputFileName);
            Object obj=jsonParser.parse(reader);
            JSONObject jsonObject=(JSONObject)obj;

            int datasetId=(int)(long)jsonObject.get("dataset id");

            String datasetName=(String)jsonObject.get("dataset name");
            int lblPerIns=(int)(long)jsonObject.get("maximum number of labels per instance");

            JSONArray classLabel=(JSONArray)jsonObject.get("class labels");
            for (int i=0;i<classLabel.size();i++){
                JSONObject address=(JSONObject)classLabel.get(i);

                int labelId=(int)(long)address.get("label id");
                String labelText=(String)address.get("label text");

                Label label = new Label(labelId, labelText, datasetName, datasetId, lblPerIns);
                labelLinkedList.add(label);
            }

            JSONArray instances=(JSONArray)jsonObject.get("instances");
            for (int i=0;i<instances.size();i++){
                JSONObject address=(JSONObject)instances.get(i);

                int instanceId=(int)(long)address.get("id");
                String instanceText=(String)address.get("instance");

                Instance instance = new Instance(instanceId, instanceText, datasetId, datasetName, lblPerIns);
                instanceLinkedList.add(instance);
            }

        } catch (Exception e){
            logger.warning("Please provide a valid filepath");
            System.exit(1);
        }
    }

}
