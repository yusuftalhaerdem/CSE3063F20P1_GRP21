package edu.marmara.annotator;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Output {

    private void writeToFile(Object object, String filename) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        try {
            writer.writeValue(Paths.get(filename).toFile(), object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @JsonPropertyOrder({"datasetId", "datasetName", "lblPerIns", "classLabels", "instances", "labelAssignments", "users"})
    public void outputDataset(String filename, ArrayList<Dataset> datasetArrayList){
        int datasetId;
        String datasetName;
        ArrayList<Label> labelArrayList;
        ArrayList<Instance> instanceArrayList;
        ArrayList<Labelling> assignedLabels;
        ArrayList<User> assignedUsers;
        int lblPerIns;

        ArrayList<OutputDataset> outJson = new ArrayList<>();
        for (Dataset currentDataset : datasetArrayList) {
            datasetId =  currentDataset.getDatasetID();
            datasetName = currentDataset.getDatasetName();
            labelArrayList = currentDataset.getLabelArrayList();
            instanceArrayList = currentDataset.getInstanceArrayList();
            assignedLabels = currentDataset.getLabellingArrayList();
            assignedUsers = currentDataset.getAssignedUsersArrayList();
            lblPerIns = currentDataset.getLabelPerInstance();

            ArrayList<Object> list1 = new ArrayList<>();
            for (Label label : labelArrayList) {
                Map<Object, Object> elements = new LinkedHashMap<>();
                elements.put("label id", label.getLabelID());
                elements.put("label text", label.getLabelText());
                list1.add(elements);
            }

            ArrayList<Object> list2 = new ArrayList<>();
            for (Instance instance : instanceArrayList) {
                Map<Object, Object> elements = new LinkedHashMap<>();
                elements.put("id", instance.getInstanceID());
                elements.put("instance", instance.getInstanceText());
                list2.add(elements);
            }

            ArrayList<Object> list3 = new ArrayList<>();
            for (Labelling labelling : assignedLabels) {
                Map<Object, Object> elements = new LinkedHashMap<>();
                elements.put("instance id", labelling.getInstance().getInstanceID());
                ArrayList<Integer> temp = new ArrayList<>();
                for (Label label : labelling.getLabelArrayList()) {
                    temp.add(label.getLabelID());
                }
                elements.put("class label ids", temp);
                elements.put("user id", labelling.getUser().getUserID());
                elements.put("datetime", labelling.getDateTime());
                list3.add(elements);
            }
            ArrayList<Object> list4 = new ArrayList<>();
            for (User user: assignedUsers) {
                Map<Object, Object> elements = new LinkedHashMap<>();
                elements.put("user id", user.getUserID());
                elements.put("user name", user.getUserName());
                elements.put("user type", user.getUserType());
                list4.add(elements);
            }

            OutputDataset json = new OutputDataset(datasetId, datasetName, lblPerIns, list1, list2, list3, list4);
            outJson.add(json);
        }
        JSONObject out = new JSONObject();
        out.put("Datasets",outJson);

        writeToFile(out,filename);
    }

    public void outputMetrics(String fileName, ArrayList<Dataset> datasetArrayList, ArrayList<User> userArrayList) {

        ArrayList<DatasetMetrics> datasetMetrics = new ArrayList<>();
        for(Dataset dataset : datasetArrayList){
            DatasetMetrics metrics = dataset.getEvaluationMatrix();
            datasetMetrics.add(metrics);
        }

        ArrayList<InstanceMetrics> instanceMetrics = new ArrayList<>();
        for(Dataset dataset: datasetArrayList){
            ArrayList<Instance> instances = dataset.getInstanceArrayList();
            for(Instance instance: instances){
                InstanceMetrics metrics = instance.getEvaluationMatrix();
                instanceMetrics.add(metrics);
            }
        }

        ArrayList<UserMetrics> userMetrics = new ArrayList<>();
        for(User user: userArrayList){
            UserMetrics metrics = user.getEvaluationMatrix();
            userMetrics.add(metrics);
        }
        OutputMetrics out = new OutputMetrics(datasetMetrics, instanceMetrics, userMetrics);

        writeToFile(out, fileName);
    }
}
