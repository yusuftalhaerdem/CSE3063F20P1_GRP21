package edu.marmara.annotator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class OutputCreator {
    private static final Logger logger = Logger.getLogger( OutputCreator.class.getName());
    FileHandler fileHandler;

    RandomLabeling randomLabeling;
    LinkedList<Label> labelLinkedList;
    LinkedList<Instance> instanceLinkedList;
    Dataset datasetInfo;
    User user;
    int labelCount;
    int instanceCount;


    public OutputCreator(User user,FileHandler fileHandler) throws IOException, ParseException {
        this.fileHandler = fileHandler;
        logger.addHandler(fileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);


        this.randomLabeling = new RandomLabeling(fileHandler);
        this.labelLinkedList = randomLabeling.getLabelLinkedList();
        this.instanceLinkedList = randomLabeling.getInstanceLinkedList();
        this.datasetInfo = randomLabeling.getDatasetInfo();
        this.labelCount = randomLabeling.getLabelCount();
        this.instanceCount = randomLabeling.getInstanceCount();
        this.user = user;

        createJsonFile();


    }


    private void createJsonFile() {

        JSONObject outJson = new JSONObject();
        outJson.put("dataset id", this.datasetInfo.getDatasetID());
        outJson.put("dataset name", this.datasetInfo.getDatasetName());
        outJson.put("maximum number of labels per instance", this.datasetInfo.getMaxNoOfLabelsPerInstance());


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

        try (FileWriter file = new FileWriter("output2.json")) {
            String jsonString = outJson.toJSONString();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            file.write(objectMapper.writeValueAsString(objectMapper.readTree(jsonString)));
            file.flush();
        } catch (IOException e) {

        }


    }
}
