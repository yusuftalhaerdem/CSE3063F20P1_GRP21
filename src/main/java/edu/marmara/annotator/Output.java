package edu.marmara.annotator;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;


public class Output {
    private static final Logger logger = Logger.getLogger( Output.class.getName());

    RandomLabeling label;
    LinkedList<Label> labelLinkedList;
    LinkedList<Instance> instanceLinkedList;
    int datasetId;
    String datasetName;
    int lblPerIns;
    User user;
    int labelCount;
    int instanceCount;


    public Output(RandomLabeling label, User user) throws IOException, ParseException {
        this.label = label;
        this.user = user;

        this.labelLinkedList = this.label.labelLinkedList;
        this.instanceLinkedList = this.label.instanceLinkedList;
        this.labelCount = this.label.labelCount;
        this.instanceCount = this.label.instanceCount;
        this.datasetId = this.label.instanceLinkedList.get(0).getDatasetID();
        this.datasetName = this.label.instanceLinkedList.get(0).getDatasetName();
        this.lblPerIns = this.label.instanceLinkedList.get(0).getLblPerIns();

    }

    public void writeToFile(){
        JSONObject outJson = createJsonObject();

        try (FileWriter file = new FileWriter("output2.json")) {
            file.write(outJson.toJSONString());
            file.flush();

        } catch (IOException e) {

        }
    }

    private JSONObject createJsonObject() {

        JSONObject outJson = new JSONObject();
        outJson.put("dataset id", this.datasetId);
        outJson.put("dataset name", this.datasetName);
        outJson.put("maximum number of labels per instance", this.lblPerIns);


        List<Map<Object, Object>> list = new ArrayList<>();

        for (int i=0;i < this.labelCount;i++){
            Map<Object, Object> elements = new HashMap<>();
            elements.put("label id",this.labelLinkedList.get(i).getLabelID());
            elements.put("label text", this.labelLinkedList.get(i).getLabelText());
            list.add(elements);
        }
        outJson.put("class labels",list);

        list = new ArrayList<>();
        for(int i=0;i < this.instanceCount; i++){
            Map<Object, Object> elements = new HashMap<>();
            elements.put("id", this.instanceLinkedList.get(i).getInstanceID());
            elements.put("instance", this.instanceLinkedList.get(i).getInstanceText());
            list.add(elements);
        }
        outJson.put("instances",list);

        list = new ArrayList<>();
        for (int i=0; i < this.instanceLinkedList.size();i++){
            Map<Object, Object> elements = new HashMap<>();
            elements.put("instance id",this.instanceLinkedList.get(i).getInstanceID());
            elements.put("class label ids",this.instanceLinkedList.get(i).getLabels());
            elements.put("user id",this.user.getUserName());
            elements.put("datetime",this.instanceLinkedList.get(i).getDateTime());
            list.add(elements);

        }
        outJson.put("class label assignments",list);

        list = new ArrayList<>();
        Map<Object, Object> elements = new HashMap<>();
        elements.put("user id",this.user.getUserID());
        elements.put("user name",this.user.getUserName());

        list.add(elements);
        outJson.put("users",list);

        return outJson;

    }
}
