package edu.marmara.annotator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class DatasetMetrics {

    private double completenessPercentage;
    private Map<Label, Double> distribution = new LinkedHashMap<>();
    private Map<Label, Integer> uniqueInsNumber = new LinkedHashMap<>();
    private int numOfUsers;
    private Map<User, Double> userCompletenessPercentage = new LinkedHashMap<>();
    private Map<User, Double> userConsistencyPercentage = new LinkedHashMap<>();

    DatasetMetrics() {
    }

    public void calculateAll(ArrayList<Dataset> datasetArrayList) {
        for (Dataset dataset : datasetArrayList) {
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
        for (Labelling labelling : dataset.getLabellingArrayList()) {
            if (!temp.contains(labelling.getInstance().getInstanceID())) {
                temp.add(labelling.getInstance().getInstanceID());
            }
        }
        count = temp.size();
        double completenessPercentage;
        completenessPercentage = ((double) count) / ((double) dataset.getInstanceArrayList().size());
        dataset.getEvaluationMatrix().setCompletenessPercentage(completenessPercentage);
    }

    private void calculateDistribution(Dataset dataset) {
        int labeledInstances = dataset.getLabellingArrayList().size();
        Map<Label, Double> distributionMap;
        Map<Label, Double> temp = new LinkedHashMap<>();
        for (Labelling labelling : dataset.getLabellingArrayList()) {
            Label finalLabel = labelling.getFinalLabel();
            if (!temp.containsKey(finalLabel)) {
                for (Labelling labelling1 : dataset.getLabellingArrayList()) {
                    if (labelling1.getFinalLabel() == finalLabel) {
                        if (temp.containsKey(finalLabel)) {
                            temp.put(finalLabel, temp.get(finalLabel) + 1.0);
                        } else {
                            temp.put(finalLabel, 1.0);
                        }
                    }
                }
            }
        }
        double finalLabelNumber = labeledInstances;
        distributionMap = new LinkedHashMap<>(temp);
        distributionMap.replaceAll((k, v) -> v / finalLabelNumber);
        dataset.getEvaluationMatrix().setDistribution(distributionMap);
    }

    private void calculateUniqueInsNumber(Dataset dataset) {
        Map<Label, Integer> map = new LinkedHashMap<>();
        for (Label label : dataset.getLabelArrayList()) {
            ArrayList<Integer> instances = new ArrayList<>();
            for (Labelling labelling : dataset.getLabellingArrayList()) {
                if (labelling.getLabelArrayList().contains(label) && !instances.contains(labelling.getInstance().getInstanceID())) {
                    instances.add(labelling.getInstance().getInstanceID());
                }
            }
            map.put(label, instances.size());
        }
        dataset.getEvaluationMatrix().setUniqueInsNumber(map);
    }

    private void calculateNumOfUsers(Dataset dataset) {
        int numOfUsers = dataset.getAssignedUsersArrayList().size();
        dataset.getEvaluationMatrix().setNumOfUsers(numOfUsers);
    }

    private void calculateUserCompletenessPercentage(Dataset dataset) {
        Map<User, Double> map = new LinkedHashMap<>();
        double labeledInstances = dataset.getLabellingArrayList().size();
        for (Labelling labelling : dataset.getLabellingArrayList()) {
            ArrayList<Integer> instanceIDs = new ArrayList<>();
            User currentUser = labelling.getUser();
            for (Labelling labelling1 : dataset.getLabellingArrayList()) {
                if (labelling1.getUser() == currentUser) {
                    instanceIDs.add(labelling.getInstance().getInstanceID());
                }
            }
            double percentage = labeledInstances == 0 ? 0 : ((double) instanceIDs.size()) / labeledInstances;
            map.put(currentUser, percentage);
        }
        dataset.getEvaluationMatrix().setUserCompletenessPercentage(map);
    }

    private void calculateUserConsistencyPercentage(Dataset dataset) {
        Map<User, Double> userConsistencyPercentage = new LinkedHashMap<>();
        for (User user : dataset.getAssignedUsersArrayList()) {
            double consistencyPercentage = user.getEvaluationMatrix().getConsistencyPercentage();
            userConsistencyPercentage.put(user, consistencyPercentage);
            dataset.getEvaluationMatrix().setUserConsistencyPercentage(userConsistencyPercentage);
        }
    }

    public double getCompletenessPercentage() {
        return completenessPercentage;
    }

    public void setCompletenessPercentage(double completenessPercentage) {
        this.completenessPercentage = completenessPercentage;
    }

    public Map<Label, Double> getDistribution() {
        return distribution;
    }

    public void setDistribution(Map<Label, Double> distribution) {
        this.distribution = distribution;
    }

    public Map<Label, Integer> getUniqueInsNumber() {
        return uniqueInsNumber;
    }

    public void setUniqueInsNumber(Map<Label, Integer> uniqueInsNumber) {
        this.uniqueInsNumber = uniqueInsNumber;
    }

    public int getNumOfUsers() {
        return numOfUsers;
    }

    public void setNumOfUsers(int numOfUsers) {
        this.numOfUsers = numOfUsers;
    }

    public Map<User, Double> getUserCompletenessPercentage() {
        return userCompletenessPercentage;
    }

    public void setUserCompletenessPercentage(Map<User, Double> userCompletenessPercentage) {
        this.userCompletenessPercentage = userCompletenessPercentage;
    }

    public Map<User, Double> getUserConsistencyPercentage() {
        return userConsistencyPercentage;
    }

    public void setUserConsistencyPercentage(Map<User, Double> userConsistencyPercentage) {
        this.userConsistencyPercentage = userConsistencyPercentage;
    }
}
