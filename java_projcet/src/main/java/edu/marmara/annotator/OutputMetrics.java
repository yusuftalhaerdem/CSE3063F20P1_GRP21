package edu.marmara.annotator;

import java.util.ArrayList;

public class OutputMetrics{
    ArrayList<Object> datasetMetrics;
    ArrayList<Object> instanceMetrics;
    ArrayList<Object> userMetrics;


    public OutputMetrics(ArrayList<Object> datasetMetrics, ArrayList<Object> instanceMetrics, ArrayList<Object> userMetrics) {
        this.datasetMetrics = datasetMetrics;
        this.instanceMetrics = instanceMetrics;
        this.userMetrics = userMetrics;
    }

    public ArrayList<Object> getDatasetMetrics() {
        return datasetMetrics;
    }

    public void setDatasetMetrics(ArrayList<Object> datasetMetrics) {
        this.datasetMetrics = datasetMetrics;
    }

    public ArrayList<Object> getInstanceMetrics() {
        return instanceMetrics;
    }

    public void setInstanceMetrics(ArrayList<Object> instanceMetrics) {
        this.instanceMetrics = instanceMetrics;
    }

    public ArrayList<Object> getUserMetrics() {
        return userMetrics;
    }

    public void setUserMetrics(ArrayList<Object> userMetrics) {
        this.userMetrics = userMetrics;
    }
}
