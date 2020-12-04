package edu.marmara.annotator;

import java.nio.file.Paths;
import java.util.*;

import java.io.IOException;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


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


    public Output(RandomLabeling label, User user){
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
    @JsonPropertyOrder({ "dataset id", "dataset name", "maximum number of labels per instance", "class labels", "instances", "class label assignments", "users"})
    public void writeToFile() throws IOException {
        outputData outJson = createJsonObject();
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

        try{
            writer.writeValue(Paths.get("output3.json").toFile(), outJson);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public outputData createJsonObject() {

        JSONObject outJson = new JSONObject();
        outJson.put("dataset id", this.datasetId);
        outJson.put("dataset name", this.datasetName);
        outJson.put("maximum number of labels per instance", this.lblPerIns);


        JSONArray list1 = new JSONArray();

        for (int i=0;i < this.labelCount;i++){
            Map<Object, Object> elements = new HashMap<>();
            elements.put("label id",this.labelLinkedList.get(i).getLabelID());
            elements.put("label text", this.labelLinkedList.get(i).getLabelText());
            list1.add(elements);
        }
        outJson.put("class labels",list1);

        JSONArray list2 = new JSONArray();
        for(int i=0;i < this.instanceCount; i++){
            Map<Object, Object> elements = new HashMap<>();
            elements.put("id", this.instanceLinkedList.get(i).getInstanceID());
            elements.put("instance", this.instanceLinkedList.get(i).getInstanceText());
            list2.add(elements);
        }
        outJson.put("instances",list2);

        JSONArray list3 = new JSONArray();
        for (int i=0; i < this.instanceLinkedList.size();i++){
            Map<Object, Object> elements = new HashMap<>();
            elements.put("instance id",this.instanceLinkedList.get(i).getInstanceID());
            elements.put("class label ids",this.instanceLinkedList.get(i).getLabels());
            elements.put("user id",this.user.getUserName());
            elements.put("datetime",this.instanceLinkedList.get(i).getDateTime());
            list3.add(elements);

        }
        outJson.put("class label assignments",list3);

        JSONArray list4 = new JSONArray();
        Map<Object, Object> elements = new HashMap<>();
        elements.put("user id",this.user.getUserID());
        elements.put("user name",this.user.getUserName());

        list4.add(elements);
        outJson.put("users",list4);


        outputData json = new outputData(this.datasetId, this.datasetName, this.lblPerIns, list1, list2, list3, list4);

        return json;

    }
}
