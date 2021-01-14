package edu.marmara.annotator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class UserMetrics {

    private ArrayList<User> allUsers;   //bu bir user olacak diye düşünyüorum ve muhtemelen öyle ama kodu fazla karıştırmayalım değilse de
    private int numOfDatasets;      //bunu arraylist olarak tutabiliriz bence ama gerek yok sanırım
    private Map<Dataset, Double> datasetCompletenessPercentage;
    private int labeledInstances;
    private int unqLabeledInstances;
    private double consistencyPercentage;
    private double avgTime;
    private double std;

    UserMetrics() {
    }

    public void calculateAll(ArrayList<Dataset> datasetArrayList) {
        allUsers = allUsers(datasetArrayList);

        calculateNumOfDatasets(datasetArrayList);
        calculateDatasetCompletenessPercentage(datasetArrayList);
        calculateLabeledInstances(datasetArrayList);
        calculateUnqLabeledInstances(datasetArrayList);
        calculateConsistencyPercentage(datasetArrayList);
        calculateAvgTime(datasetArrayList);
        calculateStd(datasetArrayList);
    }

    public void calculateNumOfDatasets(ArrayList<Dataset> datasetArrayList) {//Number of datasets assigne
        for (User user : this.allUsers) {
            int numOfDatasets = 0;
            for (Dataset dataset : datasetArrayList) {
                ArrayList<User> assignedUsers = dataset.getAssignedUsersArrayList();
                if (assignedUsers.contains(user))
                    numOfDatasets++;
            }
            user.getEvaluationMatrix().setNumOfDatasets(numOfDatasets);
        }
    }

    public void calculateDatasetCompletenessPercentage(ArrayList<Dataset> datasetArrayList) {//List of all datasets with their completeness percentage
        for (User user : this.allUsers) {
            Map<Dataset, Double> datasetCompletenessPercentage = new LinkedHashMap<>();
            for (Dataset dataset : datasetArrayList) {
                ArrayList<User> assignedUsers = dataset.getAssignedUsersArrayList();
                if (assignedUsers.contains(user))
                    datasetCompletenessPercentage.put(dataset, dataset.getEvaluationMatrix().getCompletenessPercentage());
            }
            user.getEvaluationMatrix().setDatasetCompletenessPercentage(datasetCompletenessPercentage);
        }

    }

    public void calculateLabeledInstances(ArrayList<Dataset> datasetArrayList) {//Total number of instances labeled
        for (User user : this.allUsers) {
            int labeledInstances = 0;
            for (Dataset dataset : datasetArrayList) {
                ArrayList<Labelling> labellingArrayList = dataset.getLabellingArrayList();
                for (Labelling labelling : labellingArrayList)
                    if (labelling.getUser() == user)
                        labeledInstances++;
            }
            user.getEvaluationMatrix().setLabeledInstances(labeledInstances);

        }
    }

    public void calculateUnqLabeledInstances(ArrayList<Dataset> datasetArrayList) {//Total number of unique instances labeled
        for (User user : this.allUsers) {
            ArrayList<Integer> labellingIds = new ArrayList<>();
            for (Dataset dataset : datasetArrayList) {
                ArrayList<Labelling> labellingArrayList = dataset.getLabellingArrayList();
                for (Labelling labelling : labellingArrayList) {
                    if (!labellingIds.contains(labelling.getInstance().getInstanceID()))
                        labellingIds.add(labelling.getInstance().getInstanceID());
                }
            }
            user.getEvaluationMatrix().setUnqLabeledInstances(labellingIds.size());
        }
    }

    public void calculateConsistencyPercentage(ArrayList<Dataset> datasetArrayList) {//Consistency percentage/////anlamadım-----------------soram takım arkilerime
        double consistencyPercentage = 0;
        int total = 0;
        int same = 0;
        for (User user : this.allUsers) {
            for (Dataset dataset : datasetArrayList) {
                ArrayList<Labelling> labellingArrayList = dataset.getLabellingArrayList();
                for (Labelling labelling : labellingArrayList) {
                    if (user == labelling.getUser()) {
                        for (Labelling labelling1 : labellingArrayList)
                            if (labelling.getInstance() == labelling1.getInstance()) {
                                total++;
                                if (labelling.getLabelArrayList().equals(labelling1.getLabelArrayList()))
                                    same++;
                            }
                    }

                }
            }
            consistencyPercentage = (double) same / (double) total;
            user.getEvaluationMatrix().setConsistencyPercentage(consistencyPercentage);
        }

    }

    public void calculateAvgTime(ArrayList<Dataset> datasetArrayList) {//Average time spent in labeling an instance in seconds
        double counter = 0;
        double total = 0;
        double avgTime;
        for (User user : this.allUsers) {
            for (Dataset dataset : datasetArrayList) {
                ArrayList<Labelling> labellingArrayList = dataset.getLabellingArrayList();
                for (Labelling labelling : labellingArrayList) {
                    if (user == labelling.getUser()) {
                        counter++;
                        total += labelling.getTimeSpent();
                    }
                }
            }
            avgTime = total / counter;
            user.getEvaluationMatrix().setAvgTime(avgTime);
        }
    }

    public void calculateStd(ArrayList<Dataset> datasetArrayList) {//Std. dev. of  time spent in labeling an instance in seconds--- takes mean from field6 and field3
        double total = 0;
        for (User user : this.allUsers) {
            for (Dataset dataset : datasetArrayList) {
                ArrayList<Labelling> labellingArrayList = dataset.getLabellingArrayList();
                for (Labelling labelling : labellingArrayList) {
                    total += Math.pow(user.getEvaluationMatrix().getAvgTime() - labelling.getTimeSpent(), 2);
                }
            }
            user.getEvaluationMatrix().setStd(Math.sqrt(total));
        }
    }

    private ArrayList<User> allUsers(ArrayList<Dataset> datasetArrayList) {
        ArrayList<User> users = new ArrayList<>();
        for (Dataset dataset : datasetArrayList) {
            ArrayList<User> assignedUsers = dataset.getAssignedUsersArrayList();
            for (User user : assignedUsers) {
                if (!users.contains(user))
                    users.add(user);
            }
        }
        return users;
    }

    public int getNumOfDatasets() {
        return numOfDatasets;
    }

    public void setNumOfDatasets(int numOfDatasets) {
        this.numOfDatasets = numOfDatasets;
    }

    public Map<Dataset, Double> getDatasetCompletenessPercentage() {
        return datasetCompletenessPercentage;
    }

    public void setDatasetCompletenessPercentage(Map<Dataset, Double> datasetCompletenessPercentage) {
        this.datasetCompletenessPercentage = datasetCompletenessPercentage;
    }

    public int getLabeledInstances() {
        return labeledInstances;
    }

    public void setLabeledInstances(int labeledInstances) {
        this.labeledInstances = labeledInstances;
    }

    public int getUnqLabeledInstances() {
        return unqLabeledInstances;
    }

    public void setUnqLabeledInstances(int unqLabeledInstances) {
        this.unqLabeledInstances = unqLabeledInstances;
    }

    public double getConsistencyPercentage() {
        return consistencyPercentage;
    }

    public void setConsistencyPercentage(double consistencyPercentage) {
        this.consistencyPercentage = consistencyPercentage;
    }

    public double getAvgTime() {
        return avgTime;
    }

    public void setAvgTime(double avgTime) {
        this.avgTime = avgTime;
    }

    public double getStd() {
        return std;
    }

    public void setStd(double std) {
        this.std = std;
    }

}

