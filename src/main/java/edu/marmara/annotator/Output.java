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
    public void outputDataset(String filename, ArrayList<Dataset> datasetArrayList) {
        int datasetId;
        String datasetName;
        ArrayList<Label> labelArrayList;
        ArrayList<Instance> instanceArrayList;
        ArrayList<Labelling> assignedLabels;
        ArrayList<User> assignedUsers;
        int lblPerIns;

        ArrayList<OutputDataset> outJson = new ArrayList<>();
        for (Dataset currentDataset : datasetArrayList) {
            datasetId = currentDataset.getDatasetID();
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
            for (User user : assignedUsers) {
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
        out.put("Datasets", outJson);

        writeToFile(out, filename);
    }

    @JsonPropertyOrder({"datasetMetrics", "instanceMetrics", "userMetrics"})
    public void outputMetrics(String fileName, ArrayList<Dataset> datasetArrayList, ArrayList<User> userArrayList) {

        ArrayList<Object> datasets = new ArrayList<>();
        for (Dataset dataset : datasetArrayList) {
            Map<String, Object> datasetMetrics = new LinkedHashMap<>();
            DatasetMetrics metrics = dataset.getEvaluationMatrix();
            datasetMetrics.put("Dataset id", dataset.getDatasetID());
            datasetMetrics.put("Completeness percentage", metrics.getCompletenessPercentage() * 100.0);

            ArrayList<Object> list1 = new ArrayList<>();
            Map<Object, Object> temp = new LinkedHashMap<>();
            for (Label label : metrics.getDistribution().keySet()) {
                temp.put(label.getLabelText(), ("%" + metrics.getDistribution().get(label) * 100.0));
            }
            list1.add(temp);
            datasetMetrics.put("Label distributions", list1);

            ArrayList<Object> list2 = new ArrayList<>();
            temp = new LinkedHashMap<>();
            for (Label label : metrics.getUniqueInsNumber().keySet()) {
                temp.put(label.getLabelText(), metrics.getUniqueInsNumber().get(label));
            }
            list2.add(temp);
            datasetMetrics.put("Number of unique instances", list2);

            datasetMetrics.put("Number of Users", metrics.getNumOfUsers());

            ArrayList<Object> list3 = new ArrayList<>();
            temp = new LinkedHashMap<>();
            for (User user : metrics.getUserCompletenessPercentage().keySet()) {
                temp.put(user.getUserName(), metrics.getUserCompletenessPercentage().get(user));
            }
            list3.add(temp);
            datasetMetrics.put("User completeness Percentage", list3);

            ArrayList<Object> list4 = new ArrayList<>();
            temp = new LinkedHashMap<>();
            for (User user : metrics.getUserConsistencyPercentage().keySet()) {
                temp.put(user.getUserName(), metrics.getUserConsistencyPercentage().get(user));
            }
            list4.add(temp);
            datasetMetrics.put("User consistency percentage", list4);

            datasets.add(datasetMetrics);
        }

        ArrayList<Object> instances = new ArrayList<>();
        for (Dataset dataset : datasetArrayList) {
            ArrayList<Instance> instanceArrayList = dataset.getInstanceArrayList();
            for (Instance instance : instanceArrayList) {
                Map<String, Object> instanceMetrics = new LinkedHashMap<>();
                InstanceMetrics metrics = instance.getEvaluationMatrix();
                instanceMetrics.put("Instance id", instance.getInstanceID());
                instanceMetrics.put("Dataset id", instance.getDatasetID());
                instanceMetrics.put("Total number of assignments", metrics.getTotalNumberOfLabels());
                instanceMetrics.put("Number of unique labels", metrics.getUniqueNumberOfLabels());
                instanceMetrics.put("Number of unique users", metrics.getUniqueUsers());

                ArrayList<Object> list = new ArrayList<>();
                Map<Object, Object> temp = new LinkedHashMap<>();
                for (Label label : metrics.getLabelPercentage().keySet()) {
                    temp.put(label.getLabelText(), metrics.getLabelPercentage().get(label));
                }
                list.add(temp);
                instanceMetrics.put("Most frequent label", temp);
                instanceMetrics.put("Class labels", metrics.getLabelPercentage().entrySet());
                instanceMetrics.put("Entropy", metrics.getEntropy());

                instances.add(instanceMetrics);
            }
        }

        ArrayList<Object> users = new ArrayList<>();
        for (User user : userArrayList) {
            UserMetrics metrics = user.getEvaluationMatrix();
            Map<String, Object> userMetrics = new LinkedHashMap<>();
            userMetrics.put("User id", user.getUserID());
            userMetrics.put("User Name", user.getUserName());
            userMetrics.put("Number of datasets", metrics.getNumOfDatasets());

            ArrayList<Object> list = new ArrayList<>();
            Map<Object, Object> temp = new LinkedHashMap<>();
            for (Dataset dataset : metrics.getDatasetCompletenessPercentage().keySet()) {
                temp.put(dataset.getDatasetID(), ("%" + metrics.getDatasetCompletenessPercentage().get(dataset) * 100.0));
            }
            list.add(temp);
            userMetrics.put("Dataset completeness percentages", list);

            userMetrics.put("Total labeled instances", metrics.getLabeledInstances());
            userMetrics.put("Total unique labeled instances", metrics.getUnqLabeledInstances());
            userMetrics.put("Consistency percentage", ("%" + metrics.getConsistencyPercentage() * 100.0));
            userMetrics.put("Average time spend in labeling", metrics.getAvgTime());
            userMetrics.put("Std of time spent in labeling", metrics.getStd());

            users.add(userMetrics);
        }


        OutputMetrics metrics = new OutputMetrics(datasets, instances, users);
        writeToFile(metrics, fileName);
    }

}
