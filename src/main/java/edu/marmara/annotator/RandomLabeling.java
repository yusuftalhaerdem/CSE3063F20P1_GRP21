package edu.marmara.annotator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RandomLabeling {

    RandomLabeling() {
    }

    public void retrieveData(ArrayList<Labelling> labellingArrayList, ArrayList<Dataset> datasetArrayList) {  //this will do some things in randomlabeling
        for (int i = 0; i < datasetArrayList.size(); i++) {
            for (int j = 0; j < labellingArrayList.size(); j++) {
                if (labellingArrayList.get(j).getDataset() == datasetArrayList.get(i).getDatasetID()) {  //burada neden getDataset int döndürüyo ya
                    datasetArrayList.get(i).getLabellingArrayList().add(labellingArrayList.get(j));
                    for (int k = 0; k < datasetArrayList.get(i).getInstanceArrayList().size(); k++) {
                        Instance instance = datasetArrayList.get(i).getInstanceArrayList().get(k);
                        if (instance == labellingArrayList.get(j).getInstance()) {//instance'a labelları ekliyor
                            instance.getLabels().addAll(labellingArrayList.get(j).getLabelArrayList());
                            break;  //i think it works but no need actually especially at last day.
                        }
                    }
                }
            }
        }
    }

    public void labelByUser(Dataset currentDataset, ArrayList<User> userArrayList1, ArrayList<Labelling> labellingArrayList, ArrayList<Dataset> datasetArrayList) throws InterruptedException {

        //labellingArrayList input is probably wrong so im gonna do that in here

        ArrayList<User> userArrayList = getUserArrayList(userArrayList1, currentDataset);
        ArrayList<Label> labelArrayList = currentDataset.getLabelArrayList();
        ArrayList<Instance> instanceArrayList = currentDataset.getInstanceArrayList();
        //etiketleri ata ve labellinge oluştur productı, user-label-instance olarak

        ArrayList<Instance> instancesWithoutLabel = new ArrayList<>(instanceArrayList);
        ArrayList<Instance> instancesWithLabel = new ArrayList<>();//!!!!bu arraylist böyle olmayabilir önceki iterationları retrieve ettiğimizde bu da değişecek ciddi manada
        Output out = new Output();
        Log log = Log.getInstance();

        int lastUserChosen = 0;
        List<User> users = currentDataset.getAssignedUsersArrayList().stream().filter(i -> i.getUserType().equals("RandomBot")).collect(Collectors.toList());
        while (instancesWithoutLabel.size() != 0) {
            if (lastUserChosen > users.size() - 1)
                lastUserChosen = 0;
            User chosenUser = users.get(lastUserChosen);
            lastUserChosen++;

            long start = System.currentTimeMillis();

            int randomNumber = (int) (Math.random() * 100);
            Instance instanceToLabel;
            if (randomNumber > chosenUser.getConsistencyCheckProbability() * 100) {  //usera bakıyor random olarak consistencye göre assigned or unassigned label listten seçiyor

                instanceToLabel = instancesWithoutLabel.get(0);
                instancesWithLabel.add(instanceToLabel);
                instancesWithoutLabel.remove(instanceToLabel);

            } else { //if instance is already labeled by already, make sure same user does not label it again!!
                if (instancesWithLabel.size() == 0) {
                    instanceToLabel = instancesWithoutLabel.get(0);
                    instancesWithLabel.add(instanceToLabel);
                    instancesWithoutLabel.remove(instanceToLabel);
                } else {
                    ArrayList<Instance> checkList = new ArrayList<>(instancesWithLabel);
                    do {
                        instanceToLabel = getRandomInstance(checkList);
                        checkList.remove(instanceToLabel);
                    } while (instanceToLabel.getLabels().size() >= instanceToLabel.getMaxLabelPerInstance() && checkList.size() != 0);
                    if (instanceToLabel.getLabels().size() < instanceToLabel.getMaxLabelPerInstance()) {
                        instanceToLabel = instancesWithoutLabel.get(0);
                        instancesWithLabel.add(instanceToLabel);
                        instancesWithLabel.remove(instanceToLabel);
                    }

                }
            }


            //Label labelsToAssign= getRandomLabel(labelArrayList);
            ArrayList<Label> labelsToAssign = new ArrayList<>();
            labelsToAssign.add(getRandomLabel(labelArrayList)); //

            //kaç label ekleneceğini random olarak seçiyor gerek yok muhtemelen
            for (int howManyLabel = (int) (Math.random() * instanceToLabel.getMaxLabelPerInstance() - instanceToLabel.getLabels().size()); howManyLabel > 0; howManyLabel--) {
                ArrayList<Label> labels2 = new ArrayList<>(labelArrayList);
                Label labelToAdd = getRandomLabel(labels2);
                labelsToAssign.add(labelToAdd);
                labels2.remove(labelToAdd);
            }

            //instance labellanmamışlar listesinden kaldırıyor
            instanceToLabel.setLabels(labelsToAssign);
            //create labeling with user-label-instance remove instance from instancesWithoutLabel
            //fix it with more than one labelling and same users shouldnt label same instance again
            Thread.sleep(500);

            String timeString = String.valueOf(LocalTime.now());
            timeString = timeString.substring(0, 8);
            Labelling labelling = new Labelling(currentDataset, instanceToLabel, labelsToAssign, chosenUser, "", 0);
            labelling.setDateTime(LocalDate.now() + ", " + timeString);
            labellingArrayList.add(labelling);
            currentDataset.getLabellingArrayList().add(labelling);

            double timeSpentInLabeling = (System.currentTimeMillis() - start) / 1000F; //calculates the time elapsed from start of labeling-----not sure-----
            labelling.setTimeSpent(timeSpentInLabeling);
            currentDataset.getEvaluationMatrix().calculateAll(datasetArrayList);
            labelling.getInstance().getEvaluationMatrix().calculateAll(datasetArrayList);
            chosenUser.getEvaluationMatrix().calculateAll(datasetArrayList);

            out.outputDataset("output.json", datasetArrayList);
            out.outputMetrics("metrics.json", datasetArrayList, userArrayList);

            ArrayList<String> labels = new ArrayList<>();
            for(Label label : labelsToAssign)
                labels.add(label.getLabelText());
            log.log(String.format("user id:%s %s tagged instance id:%s with class label %s instance:\"%s\"",
                    chosenUser.getUserID(), chosenUser.getUserType(), instanceToLabel.getInstanceID(),
                    labels, instanceToLabel.getInstanceText()));

            System.out.println("end of userLabeling");
        }

    }

    private ArrayList<User> getUserArrayList(ArrayList<User> userArrayList1, Dataset dataset) {
        ArrayList<User> userAssignedToDataset = new ArrayList<>();

        for (int i = 0; i < userArrayList1.size(); i++) {
            for (int j = 0; j < dataset.getAssignedUsersArrayList().size(); j++) {
                if (dataset.getAssignedUsersArrayList().get(j) == userArrayList1.get(i)) {
                    userAssignedToDataset.add(userArrayList1.get(i));
                    break;
                }
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

}

