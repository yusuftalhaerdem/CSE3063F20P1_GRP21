package edu.marmara.annotator;

import java.util.ArrayList;

public class Instance {

    private String instanceText;
    private int instanceID;
    private int datasetID;      //gidecekkkkk
    private String datasetName;         //bu gidecek ve dataset olacak
    private int maxLabelPerInstance;
    private ArrayList<Label> labels = new ArrayList<>();
    private Label finalLabel;
    private InstanceMetrics evaluationMatrix = new InstanceMetrics();

    Instance() {
    }

    public InstanceMetrics getEvaluationMatrix() {
        return evaluationMatrix;
    }


    public Instance(int instanceID, String instanceText, int datasetID, String datasetName, int lblPerIns) {

        this.instanceText = instanceText;
        this.instanceID = instanceID;
        this.datasetID = datasetID;
        this.datasetName = datasetName;
        this.maxLabelPerInstance = lblPerIns;
        this.finalLabel = findFinalLabel(labels);
        labels = new ArrayList<>();

    }

    public String getInstanceText() {
        return instanceText;
    }

    public void setInstanceText(String instanceText) {
        this.instanceText = instanceText;
    }

    public int getInstanceID() {
        return instanceID;
    }

    public void setInstanceID(int instanceID) {
        this.instanceID = instanceID;
    }

    public int getDatasetID() {
        return datasetID;
    }

    public void setDatasetID(int datasetID) {
        this.datasetID = datasetID;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public int getMaxLabelPerInstance() {
        return maxLabelPerInstance;
    }

    public void setMaxLabelPerInstance(int maxLabelPerInstance) {
        this.maxLabelPerInstance = maxLabelPerInstance;
    }

    public ArrayList<Label> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<Label> labels) {
        this.labels = labels;
    }

    public void setEvaluationMatrix(InstanceMetrics evaluationMatrix) {
        this.evaluationMatrix = evaluationMatrix;
    }

    public Label getLabel(int LocationID) {
        return labels.get(LocationID);
    }

    public Label getFinalLabel() {
        return finalLabel;
    }

    public void setFinalLabel(Label finalLabel) {
        this.finalLabel = finalLabel;
    }

    private Label findFinalLabel(ArrayList<Label> labelArrayList) {
        Label finalLabel = null;
        int max = 0;
        for (Label label : labelArrayList) {
            int current = 0;
            if (finalLabel != label) {
                for (Label label1 : labelArrayList) {
                    if (label1 == label) {
                        current++;
                    }
                }
            }
            if (max < current) {
                finalLabel = label;
                max = current;
            }
        }
        return finalLabel;
    }
}
