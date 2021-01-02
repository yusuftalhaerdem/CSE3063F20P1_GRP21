package edu.marmara.annotator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class RandomLabeling{

    RandomLabeling() {    }

    public void retrieveData(ArrayList<Labelling> labellingArrayList,ArrayList<Dataset> datasetArrayList) {  //this will do some things in randomlabeling
        for(int i=0;i<datasetArrayList.size();i++){
            for(int j=0;j<labellingArrayList.size();j++){
                if(labellingArrayList.get(j).getDataset()==datasetArrayList.get(i).getDatasetID()) {  //burada neden getDataset int döndürüyo ya
                    datasetArrayList.get(i).getLabellingArrayList().add(labellingArrayList.get(j));
                    for(int k=0;k<datasetArrayList.get(i).getInstanceArrayList().size();k++) {
                        Instance instance= datasetArrayList.get(i).getInstanceArrayList().get(k);
                        if(instance==labellingArrayList.get(j).getInstance()){//instance'a labelları ekliyor
                            instance.getLabels().addAll(labellingArrayList.get(j).getLabelArrayList());
                        }
                    }
                }
            }
        }

    }

    public void labelByUser(Dataset currentDataset,ArrayList<User> userArrayList1,ArrayList<Labelling> labellingArrayList,ArrayList<Dataset> datasetArrayList) throws InterruptedException {

        //labellingArrayList input is probably wrong so im gonna do that in here

        ArrayList<User> userArrayList=getUserArrayList(userArrayList1,currentDataset);
        ArrayList<Label> labelArrayList= currentDataset.getLabelArrayList();
        ArrayList<Instance> instanceArrayList= currentDataset.getInstanceArrayList();
        //etiketleri ata ve labellinge oluştur productı, user-label-instance olarak

        ArrayList<Instance> instancesWithoutLabel = new ArrayList<>(instanceArrayList);
        ArrayList<Instance> instancesWithLabel = new ArrayList<>();//!!!!bu arraylist böyle olmayabilir önceki iterationları retrieve ettiğimizde bu da değişecek ciddi manada
        Output out = new Output();
        Log log = Log.getInstance();

        int userSize = userArrayList.size();

        while (instancesWithoutLabel.size() != 0) {    //sırasıyla olmayacak random olacak

            long start = System.currentTimeMillis();

            int userRandom = (int) (userSize * Math.random());
            User chosenUser = userArrayList.get(userRandom);

            //user varmış gibi label ve instance seçiyor, yukarda user seç
            int randomNumber = (int) (Math.random() * 100);
            if (randomNumber > chosenUser.getConsistencyCheckProbability() * 100) {  //usera bakıyor random olarak consistencye göre assigned or unassigned label listten seçiyor

                Instance instanceToLabel = getRandomInstance(instancesWithoutLabel);


                //Label labelsToAssign= getRandomLabel(labelArrayList);
                ArrayList<Label> labelsToAssign = new ArrayList<>();
                labelsToAssign.add(getRandomLabel(labelArrayList)); //

                //kaç label ekleneceğini random olarak seçiyor gerek yok muhtemelen
                for (int howManyLabel = (int) (Math.random() * labelArrayList.size()); howManyLabel > 0; howManyLabel--) {
                    labelsToAssign.add(getRandomLabel(labelArrayList));
                }

                //instance labellanmamışlar listesinden kaldırıyor
                instanceToLabel.setLabels(labelsToAssign);
                instancesWithoutLabel.remove(instanceToLabel);
                instancesWithLabel.add(instanceToLabel);


                //create labeling with user-label-instance remove instance from instancesWithoutLabel
                //fix it with more than one labelling and same users shouldnt label same instance again
                Thread.sleep(500);

                //bunların sırası değişecek ve bişitik hiza ne ya

                double timeSpentInLabeling = (System.currentTimeMillis() - start) / 1000F; //calculates the time elapsed from start of labeling-----not sure-----
                String timeString= String.valueOf(LocalTime.now());
                timeString= timeString.substring(0,8);
                Labelling labelling = new Labelling(currentDataset, instanceToLabel, labelsToAssign, chosenUser, "", 0, findFinalLabel(labelsToAssign));
                labelling.setDateTime(LocalDate.now()+", "+timeString);
                labellingArrayList.add(labelling);
                currentDataset.getLabellingArrayList().add(labelling);

                currentDataset.getEvaluationMatrix().calculateAll(datasetArrayList);
                labelling.getInstance().getEvaluationMatrix().calculateAll(datasetArrayList);
                chosenUser.getEvaluationMatrix().calculateAll(datasetArrayList);
                /*
                out.outputDataset("output.json", datasetArrayList);
                out.outputMetrics("metrics.json", datasetArrayList, userArrayList);
                log.log(String.format("user id:%s %s tagged instance id:%s with class label %s instance:\"%s\"",
                        chosenUser.getUserID(), chosenUser.getUserType(), instanceToLabel.getInstanceID(),
                        labelsToAssign, instanceToLabel.getInstanceText()));
*/
            } else { //if instance is already labeled by already, make sure same user does not label it again!!

                if (instancesWithLabel.size() != 0) {
                    Instance instanceToLabel = getRandomInstance(instancesWithLabel);

                    //Label labelsToAssign= getRandomLabel(labelArrayList);
                    ArrayList<Label> labelsToAssign = new ArrayList<>();                        //bu da mı long aq şuna birisi düzen çeksin.
                    labelsToAssign.add(getRandomLabel(labelArrayList)); //bu da mı long aq şuna birisi düzen çeksin.

                    //kaç label ekleneceğini random olarak seçiyor gerek yok muhtemelen
                    for (int howManyLabel = (int) (Math.random() * labelArrayList.size()); howManyLabel > 0; howManyLabel--) {
                        labelsToAssign.add(getRandomLabel(labelArrayList));
                    }

                    //bakıyor label atamak için yeterli yer var mı diye
                    int labelSize = instanceToLabel.getLabels().size();   //label atanacak örneğin mevcutta kaç etiketi olduğuna bakıyor
                    int maxSize = instanceToLabel.getMaxLabelPerInstance();       //instanceın max kaç label olabileceğine bakıyor
                    int sizeOfLabelsToAdd = labelsToAssign.size();       //mevcut iterationda kaç label atanacağını tutuyor
                    if (labelSize + sizeOfLabelsToAdd <= maxSize) {

                        Thread.sleep(500);
                        // labelling it
                        double timeSpentInLabeling = (System.currentTimeMillis() - start) / 1000F; //calculates the time elapsed from start of labeling---- not sure2----
                        //fix it with more than one labelling and same users shouldnt label same instance again
                        String timeString= String.valueOf(LocalTime.now());
                        timeString= timeString.substring(0,8);
                        Labelling labelling = new Labelling(currentDataset, instanceToLabel, labelsToAssign, chosenUser, "", timeSpentInLabeling, findFinalLabel(labelsToAssign));
                        labelling.setDateTime(LocalDate.now()+", "+timeString);
                        labellingArrayList.add(labelling);
                        currentDataset.getLabellingArrayList().add(labelling);

                        currentDataset.getEvaluationMatrix().calculateAll(datasetArrayList);
                        labelling.getInstance().getEvaluationMatrix().calculateAll(datasetArrayList);
                        chosenUser.getEvaluationMatrix().calculateAll(datasetArrayList);
                        /*
                        out.outputDataset("output.json", datasetArrayList);
                        out.outputMetrics("metrics.json", datasetArrayList, userArrayList);
                        log.log(String.format("user id:%s %s tagged instance id:%s with class label %s instance:\"%s\"",
                                chosenUser.getUserID(), chosenUser.getUserType(), instanceToLabel.getInstanceID(),
                                labelsToAssign, instanceToLabel.getInstanceText()));
                        //sendItToOutput here, labellingArrayList, updateMatrix*/
                    } else {
                        System.err.println("there is no suitable place to assign another label to instance");
                    }
                }
                System.out.println("end of a while loop, one user is labelled");
            }
        }

        System.out.println("end of userLabeling");

    }

    private ArrayList<User> getUserArrayList(ArrayList<User> userArrayList1,Dataset dataset) {
        ArrayList<User> userAssignedToDataset = new ArrayList<>();

        for (int i = 0; i < userArrayList1.size(); i++) {
            for (int j = 0; j < userArrayList1.get(i).getAssignedDataset().size(); j++) {
                if (userArrayList1.get(i).getAssignedDataset().get(j) == dataset.getDatasetID())
                    userAssignedToDataset.add(userArrayList1.get(i));
            }
        }
        return userAssignedToDataset;
    }

    private Instance getRandomInstance(ArrayList<Instance> instanceArrayList) {
        int randomInstanceNO = (int) (instanceArrayList.size() * Math.random());//choosing instance for labeling
        return instanceArrayList.get(randomInstanceNO);
    }

    private Label getRandomLabel(ArrayList<Label> labelArrayList) {
        int randomLabelID = (int) (labelArrayList.size() * Math.random());
        //choosing label for labeling
        Label labelItem = labelArrayList.get(randomLabelID);
        labelItem.setNumberOfTimes(labelItem.getNumberOfTimes() + 1);
        return labelItem;

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

