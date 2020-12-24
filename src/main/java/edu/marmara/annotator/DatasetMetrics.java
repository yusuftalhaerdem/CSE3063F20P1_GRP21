package edu.marmara.annotator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class DatasetMetrics{

    private int datasetId;
    private double completenessPercentage;
    private Map<String, Double> distribution;
    private Map<String,ArrayList<Integer>> uniqueInsNumber;
    private int numOfUsers;
    private Map<String,Double> userCompletenessPercentage;
    private Map<String,String> userConsistencyPercentage;

    DatasetMetrics(){}

    public void calculateAll(ArrayList<Dataset> datasetArrayList){
        for (Dataset dataset : datasetArrayList){
            dataset.getEvaluationMatrix().setDatasetId(dataset.getDatasetID());
            calculateCompletenessPercentage(dataset);
            calculateDistribution(dataset);
            calculateUniqueInsNumber(dataset);
            calculateNumOfUsers(dataset);
            calculateUserCompletenessPercentage(dataset);
            calculateUserConsistencyPercentage(dataset);
        }
    }

    private void calculateCompletenessPercentage(Dataset dataset) {
        int count = 0;
        ArrayList<Integer> temp = new ArrayList<>();
        for (Labelling labelling : dataset.getLabellingArrayList()){
            if(!temp.contains(labelling.getInstance().getInstanceID())){
                temp.add(labelling.getInstance().getInstanceID());
            }
        }
        count = temp.size();
        double completenessPercentage;
        completenessPercentage = ((double)count)/((double)dataset.getInstanceArrayList().size());
        dataset.getEvaluationMatrix().setCompletenessPercentage(completenessPercentage);
    }

    private void calculateDistribution(Dataset dataset) {
        int labeledInstances = dataset.getLabellingArrayList().size();
        Map<String,Double> distributionMap;
        Map<String,Double> temp = new LinkedHashMap<>();
        for(Labelling labelling : dataset.getLabellingArrayList()){
            Label finalLabel = labelling.getFinalLabel();
            if (!temp.containsKey(finalLabel.getLabelText())){
                for(Labelling labelling1 : dataset.getLabellingArrayList()){
                    if(labelling1.getFinalLabel() == finalLabel){
                        if(temp.containsKey(finalLabel.getLabelText())){
                            temp.put(finalLabel.getLabelText(),temp.get(finalLabel.getLabelText()) + 1.0);
                        }else {
                            temp.put(finalLabel.getLabelText(),1.0);
                        }
                    }
                }
            }
        }
        double finalLabelNumber = labeledInstances;
        distributionMap = new LinkedHashMap<>(temp);
        distributionMap.replaceAll((k, v) -> v/ finalLabelNumber);
        dataset.getEvaluationMatrix().setDistribution(distributionMap);
    }
    private void calculateUniqueInsNumber(Dataset dataset) {
        Map<String,ArrayList<Integer>> map = new LinkedHashMap<>();
        for (Label label : dataset.getLabelArrayList()){
            ArrayList<Integer> instances = new ArrayList<>();
            for(Labelling labelling : dataset.getLabellingArrayList()){
                if(labelling.getLabelArrayList().contains(label) && !instances.contains(labelling.getInstance().getInstanceID())){
                    instances.add(labelling.getInstance().getInstanceID());
                }
            }
            map.put(label.getLabelText(),instances);
        }
        dataset.getEvaluationMatrix().setUniqueInsNumber(map);
    }
    private void calculateNumOfUsers(Dataset dataset) {
        int numOfUsers = dataset.getAssignedUsersArrayList().size();
        dataset.getEvaluationMatrix().setNumOfUsers(numOfUsers);
    }
    private void calculateUserCompletenessPercentage(Dataset dataset) {
        Map<String,Double> map = new LinkedHashMap<>();
        double labeledInstances = dataset.getLabellingArrayList().size();
        for(Labelling labelling : dataset.getLabellingArrayList()){
            ArrayList<Integer> instanceIDs = new ArrayList<>();
            User currentUser = labelling.getUser();
            for(Labelling labelling1 : dataset.getLabellingArrayList()){
                if(labelling1.getUser() == currentUser){
                    instanceIDs.add(labelling.getInstance().getInstanceID());
                }
            }
            double percentage = labeledInstances == 0 ? 0 : ((double) instanceIDs.size()) / labeledInstances;
            map.put(currentUser.getUserName(),percentage);
        }
        dataset.getEvaluationMatrix().setUserCompletenessPercentage(map);
    }

    private void calculateUserConsistencyPercentage(Dataset dataset) {
        Map<String,String> userConsistencyPercentage = new LinkedHashMap<>();
        for(User user : dataset.getAssignedUsersArrayList()){
            Map<Integer,String> temp = user.getEvaluationMatrix().getDatasetCompletenessPercentage();
            if (temp != null){
                userConsistencyPercentage.put(user.getUserName(),temp.get(user.getUserID()));
                dataset.getEvaluationMatrix().setUserConsistencyPercentage(userConsistencyPercentage);
            }
        }
    }

    public int getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(int datasetId) {
        this.datasetId = datasetId;
    }

    public double getCompletenessPercentage() {
        return completenessPercentage;
    }

    public void setCompletenessPercentage(double completenessPercentage) {
        this.completenessPercentage = completenessPercentage;
    }

    public Map<String, Double> getDistribution() {
        return distribution;
    }

    public void setDistribution(Map<String, Double> distribution) {
        this.distribution = distribution;
    }

    public Map<String,ArrayList<Integer>>  getUniqueInsNumber() {
        return uniqueInsNumber;
    }

    public void setUniqueInsNumber(Map<String,ArrayList<Integer>>  uniqueInsNumber) {
        this.uniqueInsNumber = uniqueInsNumber;
    }

    public int getNumOfUsers() {
        return numOfUsers;
    }

    public void setNumOfUsers(int numOfUsers) {
        this.numOfUsers = numOfUsers;
    }

    public Map<String, Double> getUserCompletenessPercentage() {
        return userCompletenessPercentage;
    }

    public void setUserCompletenessPercentage(Map<String, Double> userCompletenessPercentage) {
        this.userCompletenessPercentage = userCompletenessPercentage;
    }

    public Map<String,String> getUserConsistencyPercentage() {
        return userConsistencyPercentage;
    }

    public void setUserConsistencyPercentage(Map<String,String> userConsistencyPercentage) {
        this.userConsistencyPercentage = userConsistencyPercentage;
    }
}
