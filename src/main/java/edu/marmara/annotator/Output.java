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
    private static final Logger logger = Logger.getLogger(Output.class.getName());

    LinkedList<Product> productLinkedList;
    LinkedList<Label> labelLinkedList;
    LinkedList<Instance> instanceLinkedList;
    int datasetId;
    String datasetName;
    int lblPerIns;
    LinkedList<User> userLinkedList;
    int labelCount;
    int instanceCount;


    public Output(LinkedList<Product> productLinkedList, LinkedList<User> userLinkedList) {
        this.productLinkedList = productLinkedList;
        this.userLinkedList = userLinkedList;

        this.labelLinkedList = this.productLinkedList.get(0).getAllLabels();
        this.instanceLinkedList = this.productLinkedList.get(0).getAllInstances();
        this.labelCount = this.labelLinkedList.size();
        this.instanceCount = this.instanceLinkedList.size();
        this.datasetId = this.productLinkedList.get(0).getDatasetID();
        this.datasetName = this.productLinkedList.get(0).getDatasetName();
        this.lblPerIns = this.productLinkedList.get(0).getLblPerIns();

    }


    @JsonPropertyOrder({"dataset id", "dataset name", "maximum number of labels per instance", "class labels", "instances", "class label assignments", "users"})
    public void writeToFile(String filename) {
        OutputData outJson = createJsonObject();
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

        try {
            writer.writeValue(Paths.get(filename).toFile(), outJson);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OutputData createJsonObject() {

        JSONObject outJson = new JSONObject();
        outJson.put("dataset id", this.datasetId);
        outJson.put("dataset name", this.datasetName);
        outJson.put("maximum number of labels per instance", this.lblPerIns);


        JSONArray list1 = new JSONArray();

        for (int i = 0; i < this.labelCount; i++) {
            Map<Object, Object> elements = new LinkedHashMap<>();
            elements.put("label id", this.labelLinkedList.get(i).getLabelID());
            elements.put("label text", this.labelLinkedList.get(i).getLabelText());
            list1.add(elements);
        }
        outJson.put("class labels", list1);

        JSONArray list2 = new JSONArray();
        for (int i = 0; i < this.instanceCount; i++) {
            Map<Object, Object> elements = new LinkedHashMap<>();
            elements.put("id", this.instanceLinkedList.get(i).getInstanceID());
            elements.put("instance", this.instanceLinkedList.get(i).getInstanceText());
            list2.add(elements);
        }
        outJson.put("instances", list2);

        JSONArray list3 = new JSONArray();
        for (int i = 0; i < this.instanceLinkedList.size(); i++) {
            Map<Object, Object> elements = new LinkedHashMap<>();
            elements.put("instance id", this.productLinkedList.get(i).getInstanceID());
            ArrayList<Integer> temp = new ArrayList<>();
            for (int j=0; j< this.productLinkedList.get(i).getLabelList().size(); j++){
                temp.add(this.productLinkedList.get(i).getLabelList().get(j).getLabelID());
            }
            elements.put("class label ids", temp);
            elements.put("user id", this.productLinkedList.get(i).getUserID());
            elements.put("datetime", this.productLinkedList.get(i).getDateTime());
            list3.add(elements);

        }
        outJson.put("class label assignments", list3);

        JSONArray list4 = new JSONArray();
        for (int i = 0; i < this.userLinkedList.size(); i++) {
            Map<Object, Object> elements = new LinkedHashMap<>();
            elements.put("user id", this.userLinkedList.get(i).getUserID());
            elements.put("user name", this.userLinkedList.get(i).getUserName());
            elements.put("user type", this.userLinkedList.get(i).getUserType());
            list4.add(elements);
        }
        outJson.put("users", list4);


        OutputData json = new OutputData(this.datasetId, this.datasetName, this.lblPerIns, list1, list2, list3, list4);

        return json;

    }
}
