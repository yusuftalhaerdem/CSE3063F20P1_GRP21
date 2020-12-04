package edu.marmara.annotator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.LinkedList;

public class Input {

    private JSONObject jsonObject;
    private LinkedList labelLinkedList, instanceLinkedList;
    FileReader reader;
    Object obj;
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
            System.out.println("dataset id = " + datasetId);
            String datasetName=(String)jsonObject.get("dataset name");
            System.out.println("dataset name = " + datasetName);
            int lblPerIns=(int)(long)jsonObject.get("maximum number of labels per instance");
            System.out.println("label per instance = " + lblPerIns);

            //this.createLabels(datasetName, datasetId, lblPerIns);
            //this.createInstances(datasetId,datasetName,lblPerIns);

            JSONArray classLabel=(JSONArray)jsonObject.get("class labels");
            for (int i=0;i<classLabel.size();i++){
                JSONObject address=(JSONObject)classLabel.get(i);

                int labelId=(int)(long)address.get("label id");
                String labelText=(String)address.get("label text");

                System.out.println("Label Id -> " + labelId);
                System.out.println("Label Text -> " + labelText);

                Label label = new Label();
                label.Create( labelId, labelText, datasetName, datasetId, lblPerIns);
                labelLinkedList.add(label);



            }

            JSONArray instances=(JSONArray)jsonObject.get("instances");
            for (int i=0;i<instances.size();i++){
                JSONObject address=(JSONObject)instances.get(i);

                int instanceId=(int)(long)address.get("id");
                String instanceText=(String)address.get("instance");

                Instance instance = new Instance();
                instance.Create(instanceId, instanceText, datasetId, datasetName, lblPerIns);
                instanceLinkedList.add(instance);

            }



        } catch (Exception e){
            e.printStackTrace();
        }
    }

    void createLabels(String datasetName,int datasetId,int lblPerIns){
        JSONArray classLabel = (JSONArray)jsonObject.get("class labels");
        for (int i=0;i<classLabel.size();i++){
            JSONObject address=(JSONObject)classLabel.get(i);

            Label label = new Label();
            labelLinkedList.add(label);
            label.Create((int) address.get("label id"), (String)address.get("label text"), datasetName, datasetId, lblPerIns);

        }
    }

    void createInstances(int datasetId,String datasetName,int lblPerIns){
        JSONArray instanceJson = (JSONArray)jsonObject.get("instances");
        for (int i=0;i<instanceJson.size();i++){
            JSONObject address = (JSONObject)instanceJson.get(i);

            Instance instance = new Instance();
            instanceLinkedList.add(instance);
            instance.Create((int) address.get("id"), (String) address.get("instance"), datasetId, datasetName, lblPerIns);

        }
    }


}
