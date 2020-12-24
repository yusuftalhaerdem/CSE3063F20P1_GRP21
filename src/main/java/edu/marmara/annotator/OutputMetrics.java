package edu.marmara.annotator;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;

@JsonPropertyOrder({"datasetMetrics", "instanceMetrics","userMetrics"})
public class OutputMetrics{
    ArrayList<DatasetMetrics> datasetMetrics;
    ArrayList<InstanceMetrics> instanceMetrics;
    ArrayList<UserMetrics> userMetrics;


    public OutputMetrics(ArrayList<DatasetMetrics> datasetMetrics, ArrayList<InstanceMetrics> instanceMetrics, ArrayList<UserMetrics> userMetrics) {
        this.datasetMetrics = datasetMetrics;
        this.instanceMetrics = instanceMetrics;
        this.userMetrics = userMetrics;
    }

    public ArrayList<DatasetMetrics> getDatasetMetrics() {
        return datasetMetrics;
    }

    public void setDatasetMetrics(ArrayList<DatasetMetrics> datasetMetrics) {
        this.datasetMetrics = datasetMetrics;
    }

    public ArrayList<InstanceMetrics> getInstanceMetrics() {
        return instanceMetrics;
    }

    public void setInstanceMetrics(ArrayList<InstanceMetrics> instanceMetrics) {
        this.instanceMetrics = instanceMetrics;
    }

    public ArrayList<UserMetrics> getUserMetrics() {
        return userMetrics;
    }

    public void setUserMetrics(ArrayList<UserMetrics> userMetrics) {
        this.userMetrics = userMetrics;
    }
}
