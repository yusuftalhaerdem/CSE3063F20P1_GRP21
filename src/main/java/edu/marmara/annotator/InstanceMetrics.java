package edu.marmara.annotator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class InstanceMetrics{

    private int instanceID;
    private int datasetID;
    private int totalNumberOfLabels;
    private int uniqueNumberOfLabels;
    private int uniqueUsers;
    private Map<String,Double> labelPercentage;
    private Map<String,Double> MostFrequent;
    private double entropy;


    InstanceMetrics(){}

    public void calculateAll(ArrayList<Dataset> datasetArrayList){

        for (Dataset dataset : datasetArrayList){
            ArrayList<Labeling> labelingArrayList = dataset.getLabellingArrayList();
            calculateTotalNumberOfLabels(labelingArrayList);
            calculateUniqueNumberOfLabels(labelingArrayList);
            calculateUniqueUsers(labelingArrayList);
            calculateLabelPercentage(labelingArrayList);
            calculateMostFrequent(labelingArrayList);
            calculateEntropy(labelingArrayList);
        }

    }

    private void calculateTotalNumberOfLabels(ArrayList<Labeling> labelingArrayList) {
        for(Labeling labeling : labelingArrayList){
            Instance currentInstance = labeling.getInstance();
            int totalLabels = 0;
            for(Labeling labeling1 : labelingArrayList){
                if(currentInstance == labeling1.getInstance()){
                    totalLabels += labeling1.getLabelArrayList().size();
                }
            }
            currentInstance.getEvaluationMatrix().setTotalNumberOfLabels(totalLabels);
        }
    }

    private void calculateUniqueNumberOfLabels(ArrayList<Labeling> labelingArrayList) {
        for(Labeling labeling : labelingArrayList){
            Instance currentInstance = labeling.getInstance();
            ArrayList<Label> allLabels = new ArrayList<>();
            for(Labeling labeling1 : labelingArrayList){
                if(currentInstance == labeling1.getInstance()){
                    allLabels.addAll(labeling1.getLabelArrayList());
                }
            }
            HashSet<Label> setLabels = new HashSet<>(allLabels);
            currentInstance.getEvaluationMatrix().setUniqueNumberOfLabels(setLabels.size());
        }
    }

    private void calculateUniqueUsers(ArrayList<Labeling> labelingArrayList) {
        for(Labeling labeling : labelingArrayList){
            Instance currentInstance = labeling.getInstance();
            ArrayList<User> uniqueUser = new ArrayList<>();
            for(Labeling labeling1 : labelingArrayList){
                if(currentInstance == labeling1.getInstance() && !uniqueUser.contains(labeling1.getUser())){
                    uniqueUser.add(labeling1.getUser());
                }
            }
            currentInstance.getEvaluationMatrix().setUniqueUsers(uniqueUser.size());
        }
    }

    private void calculateMostFrequent(ArrayList<Labeling> labelingArrayList) {
        int allLabels = this.getTotalNumberOfLabels();
        for (Labeling labeling : labelingArrayList) {
            Instance currentInstance = labeling.getInstance();
            Map<String, Double> temp = new LinkedHashMap<>();
            for (Labeling labeling1 : labelingArrayList) {
                for (Label label : labeling1.getLabelArrayList()) {
                    if (currentInstance == labeling1.getInstance()) {
                        if (temp.containsKey(label.getLabelText())) {
                            temp.put(label.getLabelText(), (temp.get(label.getLabelText()) + 1));
                        } else {
                            temp.put(label.getLabelText(), 1.0);
                        }
                    }
                }
            }
            Map.Entry <String,Double> entryWithMaxValue  = null;
            for(Map.Entry<String,Double> currentEntry : temp.entrySet()){
                if(entryWithMaxValue  == null || currentEntry.getValue()
                        .compareTo(entryWithMaxValue.getValue())
                        > 0){
                    entryWithMaxValue = currentEntry;
                }
            }
            Map<String,Double> max = new LinkedHashMap<>();
            assert entryWithMaxValue != null;
            max.put(entryWithMaxValue.getKey(),entryWithMaxValue.getValue()/allLabels);
            currentInstance.getEvaluationMatrix().setMostFrequent(max);
        }
    }

    private void calculateLabelPercentage(ArrayList<Labeling> labelingArrayList) {
        for(Labeling labeling : labelingArrayList){
            Map<String ,Double> labelFreq;
            Instance currentInstance = labeling.getInstance();
            double labelNumber = 0;
            for(Labeling labeling1 : labelingArrayList){
                if(currentInstance == labeling1.getInstance()){
                    labelNumber += labeling1.getLabelArrayList().size();
                }
            }
            Map<String,Double> temp = new LinkedHashMap<>();
            for(Labeling labeling1 : labelingArrayList){
                for(Label label : labeling1.getLabelArrayList()){
                    if(currentInstance == labeling1.getInstance()){
                        if(temp.containsKey(label.getLabelText())){
                            temp.put(label.getLabelText(), (temp.get(label.getLabelText()) +1));
                        }else {
                            temp.put(label.getLabelText(),1.0);
                        }
                    }
                }
            }
            double finalLabelNumber = labelNumber;
            labelFreq = new LinkedHashMap<>(temp);
            labelFreq.replaceAll((k, v) -> v/ finalLabelNumber);
            currentInstance.getEvaluationMatrix().setLabelPercentage(labelFreq);
        }
    }

    private void calculateEntropy(ArrayList<Labeling> labelingArrayList) {
        for(Labeling labeling : labelingArrayList){
            Instance currentInstance = labeling.getInstance();
            int uniqueLabels = currentInstance.getEvaluationMatrix().getUniqueNumberOfLabels();
            Map<String,Double> labelPercentage = currentInstance.getEvaluationMatrix().getLabelPercentage();
            double entropy = 0;
            for(String key : labelPercentage.keySet()){
                entropy += -labelPercentage.get(key)*(Math.log(labelPercentage.get(key)) / Math.log(uniqueLabels));
            }
            currentInstance.getEvaluationMatrix().setEntropy(((Double)entropy).isNaN() ? 0.0 : entropy);
        }
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

    public int getTotalNumberOfLabels() {
        return totalNumberOfLabels;
    }

    public void setTotalNumberOfLabels(int totalNumberOfLabels) {
        this.totalNumberOfLabels = totalNumberOfLabels;
    }

    public int getUniqueNumberOfLabels() {
        return uniqueNumberOfLabels;
    }

    public void setUniqueNumberOfLabels(int uniqueNumberOfLabels) {
        this.uniqueNumberOfLabels = uniqueNumberOfLabels;
    }

    public int getUniqueUsers() {
        return uniqueUsers;
    }

    public void setUniqueUsers(int uniqueUsers) {
        this.uniqueUsers = uniqueUsers;
    }

    public Map<String, Double> getMostFrequent() {
        return MostFrequent;
    }

    public void setMostFrequent(Map<String, Double> MostFrequent) {
        this.MostFrequent = MostFrequent;
    }

    public Map<String, Double> getLabelPercentage() {
        return labelPercentage;
    }

    public void setLabelPercentage(Map<String, Double> labelPercentage) {
        this.labelPercentage = labelPercentage;
    }

    public double getEntropy() {
        return entropy;
    }

    public void setEntropy(double entropy) {
        this.entropy = entropy;
    }
}
