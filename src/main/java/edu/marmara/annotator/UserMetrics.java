package edu.marmara.annotator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class UserMetrics{

    private int userID;/////create setter and getters in Matrix class maybe
    private int numOfDatasets;
    private Map<Integer ,String> datasetCompletenessPercentage;
    private int labeledInstances;
    private int unqLabeledInstances;
    private String consistencyPercentage;
    private double avgTime;
    private double std;

    UserMetrics(){}

    public void calculateAll(ArrayList<Dataset> datasetArrayList){
        calculateNumOfDatasets(datasetArrayList);
        calculateDatasetCompletenessPercentage(datasetArrayList);
        calculateLabeledInstances(datasetArrayList);
        calculateUnqLabeledInstances(datasetArrayList);
        calculateConsistencyPercentage(datasetArrayList);//eksik hala sanırım
        calculateAvgTime(datasetArrayList);
        calculateStd(datasetArrayList);
    }

    public void calculateNumOfDatasets(ArrayList<Dataset> datasetArrayList){//Number of datasets assigned
        int numOfDatasets=0;
        for(int i=0;i<datasetArrayList.size();i++){
            ArrayList<User> idArrayList=datasetArrayList.get(i).getAssignedUsersArrayList();
            for(int j=0;j<idArrayList.size();j++)
                if(idArrayList.get(j).getUserID()==this.userID)
                    numOfDatasets++;
        }
        this.setNumOfDatasets(numOfDatasets);
    }

    public void calculateDatasetCompletenessPercentage(ArrayList<Dataset> datasetArrayList){//List of all datasets with their completeness percentage
        Map<Integer ,String> datasetCompletenessPercentage = new LinkedHashMap<>();
        for(int i=0;i<datasetArrayList.size();i++){
            ArrayList<User> idArrayList=datasetArrayList.get(i).getAssignedUsersArrayList();
            for(int j=0;j<idArrayList.size();j++)
                if(idArrayList.get(j).getUserID()==userID){
                    int percentage = (int) ((1 - datasetArrayList.get(i).getEvaluationMatrix().getCompletenessPercentage()) * 100);
                    datasetCompletenessPercentage.put(datasetArrayList.get(i).getDatasetID(),(" %" + percentage));
                }
        }
        this.setDatasetCompletenessPercentage(datasetCompletenessPercentage);
    }

    public void calculateLabeledInstances(ArrayList<Dataset> datasetArrayList){//Total number of instances labeled
        int labeledInstances=0;
        for(int i=0;i<datasetArrayList.size();i++){
            ArrayList<Labelling> labellingArrayList =datasetArrayList.get(i).getLabellingArrayList();
            for(int j = 0; j< labellingArrayList.size(); j++){
                if(labellingArrayList.get(j).getUser().getUserID()==userID)
                    labeledInstances++;
            }
        }
        this.setLabeledInstances(labeledInstances);
    }
    public void calculateUnqLabeledInstances(ArrayList<Dataset> datasetArrayList){//Total number of unique instances labeled
        int unqLabeledInstances=0;
        for(int i=0;i<datasetArrayList.size();i++){
            ArrayList<Labelling> labellingArrayList =datasetArrayList.get(i).getLabellingArrayList();
            ArrayList<Instance> instanceArrayList=datasetArrayList.get(i).getInstanceArrayList();
            for(int j=0;j<instanceArrayList.size();j++){//aramak üzere bir instance seçiyor
                Instance instanceToSearch= instanceArrayList.get(j);
                boolean found=false;
                for(int k = 0; k< labellingArrayList.size(); k++){//labelinglere bakacak ki var mı
                    if(labellingArrayList.get(k).getInstance()==instanceToSearch){//instancea bakıyor aradığımız mı
                        if(labellingArrayList.get(k).getUser().getUserID()==userID){
                            found=true;
                        }
                        else{
                            break;
                        }
                    }
                    if(found&&k== labellingArrayList.size()-1)
                        unqLabeledInstances++;
                }
            }
        }
        this.setUnqLabeledInstances(unqLabeledInstances);
    }
    public void calculateConsistencyPercentage(ArrayList<Dataset> datasetArrayList){//Consistency percentage/////anlamadım-----------------soram takım arkilerime
        int totalFound=0;
        int totalLabeledInstances=0;
        for(int i=0;i<datasetArrayList.size();i++){ //datasetlerin içine bakıyor sırasıyla
            Dataset dataset=datasetArrayList.get(i);

            //instanceları bulacağız ve aynı user buna ne aynı etiketi atamış mi bulacağız

            for (int k = 0; k < dataset.getInstanceArrayList().size(); k++) {//bir instance seçiyor araştırmak üzere
                for(int j=0;j<dataset.getLabellingArrayList().size();j++) {//labellanmalara bakıyor bulmak için
                    Labelling labelling = dataset.getLabellingArrayList().get(j);
                    if(labelling.getUser().getUserID()==userID){//bizim userın atayıp atamadığını buluyor
                        totalLabeledInstances++;
                        Label previousLabel= labelling.getLabelArrayList().get(0);
                        for(int l = 1; l< labelling.getLabelArrayList().size(); l++){
                            if(previousLabel== labelling.getLabelArrayList().get(l)){

                            }else if(l== labelling.getLabelArrayList().size()-1){
                                totalFound++;
                            }else{
                                continue;
                            }
                        }
                    }
                }
            }

        }
        double consistencyPercentage=(double)totalFound/(double)totalLabeledInstances;
        this.consistencyPercentage="%"+consistencyPercentage*100;
    }
    public void calculateAvgTime(ArrayList<Dataset> datasetArrayList){//Average time spent in labeling an instance in seconds
        double counter=0;
        double total=0;
        for(int i=0;i<datasetArrayList.size();i++){
            for(int j=0;j<datasetArrayList.get(i).getLabellingArrayList().size();j++){
                if(datasetArrayList.get(i).getLabellingArrayList().get(j).getUser().getUserID()==userID){
                    counter++;
                    total+=datasetArrayList.get(i).getLabellingArrayList().get(j).getTimeSpent();
                }
            }
        }
        double avgTime=total/counter;
        this.setAvgTime(avgTime);
    }
    public void calculateStd(ArrayList<Dataset> datasetArrayList){//Std. dev. of  time spent in labeling an instance in seconds--- takes mean from field6 and field3
        double total=0;
        for(int i=0;i<datasetArrayList.size();i++){
            for(int j=0;j<datasetArrayList.get(i).getLabellingArrayList().size();j++){
                if(datasetArrayList.get(i).getLabellingArrayList().get(j).getUser().getUserID()==userID){
                    double timeSpent=datasetArrayList.get(i).getLabellingArrayList().get(j).getTimeSpent();
                    total+= Math.pow(avgTime-timeSpent, 2);
                }
            }
        }
        total=total/labeledInstances;
        double std=Math.sqrt(total);
        this.setStd(std);
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getNumOfDatasets() {
        return numOfDatasets;
    }

    public void setNumOfDatasets(int numOfDatasets) {
        this.numOfDatasets = numOfDatasets;
    }

    public Map<Integer ,String> getDatasetCompletenessPercentage() {
        return datasetCompletenessPercentage;
    }

    public void setDatasetCompletenessPercentage(Map<Integer ,String> datasetCompletenessPercentage) {
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

    public String getConsistencyPercentage() {
        return consistencyPercentage;
    }

    public void setConsistencyPercentage(String consistencyPercentage) {
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

