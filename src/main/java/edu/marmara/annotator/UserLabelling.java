package edu.marmara.annotator;

import com.fasterxml.jackson.databind.type.ClassStack;

import java.io.Console;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class UserLabelling {
    Scanner scanner = new Scanner(System.in);

    UserLabelling() {
    }

    public boolean userLabellingOrRandomLabelling(ArrayList<User> userArrayList, ArrayList<Dataset> datasetArrayList, int currentDataset) {

        //boolean isUserLabelling=false;
        System.out.println("\n\n\n\n\n\n");
        System.out.println("----------------------Welcome to the system--------------------------\n" +
                "----------you can press \"q\" to quit in string parts-------------\n\n\n\n\n");

        int startingInstance;
        while (true) {
            //asks for user input and output
            System.out.print("enter the user name: ");
            String userName = scanner.nextLine();
            if (userName.equals("q"))
                break;
            System.out.print("enter the user password: ");
            String password = scanner.nextLine();
            //  int userID=Integer.parseInt(password);

            //checks if that user exist in the database
            for (int i = 0; i < userArrayList.size(); i++) {
                if (userArrayList.get(i).getUserPassword() != null && userArrayList.get(i).getUserPassword().equals(password)) {
                    if (userArrayList.get(i).getUserName().equals(userName)) {
                        //isUserLabelling = true; //we run the program for the user to label
                        System.out.println("-----user succesfully entered to system.------\n\n");

                        userLabelling(userArrayList.get(i), findDataset(currentDataset, datasetArrayList), userArrayList, datasetArrayList);
                        break;
                    }
                    System.out.println("wrong user name/id. please try again. also you can press \"q\" to quit");
                    break;
                }
                if (i == userArrayList.size() - 1)
                    System.out.println("wrong user name/id. please try again. also you can press \"q\" to quit");
            }

         /*   if(isUserLabelling)
                break;
*/
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println("Do you want random Labelling?(y/n): ");
        String answer = scanner.nextLine();
        scanner.close();
        if (answer.equalsIgnoreCase("y"))
            return true;
        else if (answer.equalsIgnoreCase("n"))
            return false;
        else {
            System.out.println("you entered invalid input, we will assume no, and terminate the program");
            return false;   //as default.
        }
    }

    private Dataset findDataset(int currentDataset, ArrayList<Dataset> datasetArrayList) {
        for (int i = 0; i < datasetArrayList.size(); i++) {
            if (currentDataset == datasetArrayList.get(i).getDatasetID())
                return datasetArrayList.get(i);
        }
        System.err.println("the given dataset id cannot be found. please check your values.");
        return new Dataset();   //this is a not.!
    }


    private void userLabelling(User user, Dataset dataset, ArrayList<User> userArrayList, ArrayList<Dataset> datasetArrayList) {

        //bir şekilde userın son etiketlediği databasei ve instance getirmen lazım ki peki
        //bunu şey yapacam datasetin içine girecek hangi elemana kadar etiketlemiş bakacak ve oradan devam edecek
        Output out = new Output();
        ArrayList<Instance> unlabeledInstances = new ArrayList<>();
        ArrayList<Instance> labeledInstances = new ArrayList<>();
        Log log = Log.getInstance();


        if(!dataset.getAssignedUsersArrayList().contains(user)){
            System.out.println("This user is not assigned to this dataset. Please log in with another user or select another dataset.");
            return;
        }

        if(user.getUserType().equals("RandomBot")){
            System.out.println("User type is not suitable for manual labelling.");
            return;
        }
        //retrieving data of where we left
        for (int i = 0; i < dataset.getInstanceArrayList().size(); i++) {
            if (dataset.getInstanceArrayList().get(i).getLabels().size() > 0) {
                labeledInstances.add(dataset.getInstanceArrayList().get(i));
            } else {
                unlabeledInstances.add(dataset.getInstanceArrayList().get(i));
            }
        }

        int lastLabeledInstanceID = user.getLastLabeled().getInstanceID()>0 ? user.getLastLabeled().getInstanceID() : 0;
        //labelling part
        while (unlabeledInstances.size() != 0) {    //array bitene kadar labellamaya devam edecek

            long start = System.currentTimeMillis();    //starts the clock

            ///şimdi aşağıdakileri consistency check proya göre ayıracağız

            boolean chosedNew = false;//if its true, we will label new instance
            Instance instanceToLabel;
            if (Math.random() > user.getConsistencyCheckProbability()) {
                //mevcut part
                chosedNew = true;
                instanceToLabel = unlabeledInstances.get(lastLabeledInstanceID);

                labeledInstances.add(instanceToLabel);//listeleri düzenliyor şimdiden
                unlabeledInstances.remove(instanceToLabel);
            } else {

                //instance listin içine bakacak teker teker eğer yeni label atanabilecek bir şey bulamazsa  bass geçecek
                ArrayList<Instance> listToFindRandomInstance = new ArrayList<>(labeledInstances);

                //şunu methoda al lütfen zel
                if (listToFindRandomInstance.size() == 0) {
                    chosedNew = true;
                    instanceToLabel = unlabeledInstances.get(lastLabeledInstanceID);

                    labeledInstances.add(instanceToLabel);//listeleri düzenliyor şimdiden
                    unlabeledInstances.remove(instanceToLabel);
                } else {
                    do {
                        instanceToLabel = listToFindRandomInstance.get((int) (Math.random() * listToFindRandomInstance.size()));   //önceden labellanmış random bir instance seçiyor tekrar labellamak için
                        listToFindRandomInstance.remove(instanceToLabel);
                    } while (instanceToLabel.getMaxLabelPerInstance() < instanceToLabel.getLabels().size() && listToFindRandomInstance.size() != 0);

                    // eğer bulamazsa etiketlenmemişlerden seçecek
                    if (listToFindRandomInstance.size() == 0) {
                        chosedNew = true;
                        instanceToLabel = unlabeledInstances.get(lastLabeledInstanceID);

                        labeledInstances.add(instanceToLabel);//listeleri düzenliyor şimdiden
                        unlabeledInstances.remove(instanceToLabel);
                    }
                }
            }

            System.out.println("Instance: " + instanceToLabel.getInstanceText());
            printLabels(dataset.getLabelArrayList());
            System.out.println("if you wanna assign more than one label, please divide them by \",\"");
            System.out.print("please write the labels you want to assign for instance: ");

            //gets the labels that are supposed to assigned.
            ArrayList<Label> labelsToAssign = new ArrayList<>();

            if (readLabels(labelsToAssign, dataset)) {//reads the labels user entered
                if (chosedNew) {
                    unlabeledInstances.add(instanceToLabel);
                    labeledInstances.remove(instanceToLabel);
                }
                return;//if user denys to enter any label we will quit from user labelling part
            }

            //hali hazırda eklenmişler labellar dışında ne kadar yer kaldığına bakıyor
            if (labelsToAssign.size() > instanceToLabel.getMaxLabelPerInstance() - instanceToLabel.getLabels().size()) {
                if (chosedNew == true) {
                    unlabeledInstances.add(instanceToLabel);
                    labeledInstances.remove(instanceToLabel);
                    System.err.println("you tried to label an instance with more than max limit.");
                    continue;
                }
            }

            //moladan sonra baktım buna hata olması muhtemel
            String timeString = String.valueOf(LocalTime.now());
            timeString = timeString.substring(0, 8);
            instanceToLabel.getLabels().addAll(labelsToAssign);

            Labelling labelling = new Labelling(dataset, instanceToLabel, labelsToAssign, user, "", 0);
            labelling.setDateTime(LocalDate.now() + ", " + timeString);
            dataset.getLabellingArrayList().add(labelling);
            instanceToLabel.getLabels().addAll(labelsToAssign);
            System.out.println("given labels are assigned to instance succesfully.\n\n");

            double timeSpentInLabeling = (System.currentTimeMillis() - start) / 1000F; //calculates the time elapsed from start of labeling-----not sure-----
            labelling.setTimeSpent(timeSpentInLabeling);

            dataset.getEvaluationMatrix().calculateAll(datasetArrayList);
            labelling.getInstance().getEvaluationMatrix().calculateAll(datasetArrayList);
            user.getEvaluationMatrix().calculateAll(datasetArrayList);
            user.setLastLabeled(instanceToLabel);
            out.outputDataset("output.json", datasetArrayList);
            out.outputMetrics("metrics.json", datasetArrayList, userArrayList);

            ArrayList<String> labels = new ArrayList<>();
            for(Label label : labelsToAssign)
                labels.add(label.getLabelText());
            log.log(String.format("user id:%s %s tagged instance id:%s with class label %s instance:\"%s\"",
                    user.getUserID(), user.getUserType(), instanceToLabel.getInstanceID(),
                    labels, instanceToLabel.getInstanceText()));


        }

        System.out.println("\n\n\n-----FINISHED LABELING ALL INSTANCES.------\n\n\n");

    }

    private boolean readLabels(ArrayList<Label> labelsToAssign, Dataset dataset) {//reads the labels user entered,
        String line = scanner.nextLine();
        if (line.equals("q")) //if user denys to enter any label we will quit from user labelling part
            return true;

        String[] labels = line.split(",");
        for (int i = 0; i < labels.length; i++) {
            labels[i] = labels[i].trim();//boşluklarını alıyor
            for (int j = 0; j < dataset.getLabelArrayList().size(); j++) {
                if (labels[i].equals(dataset.getLabelArrayList().get(j).getLabelText())) {
                    labelsToAssign.add(dataset.getLabelArrayList().get(j));
                    break;
                }
                if (j == dataset.getLabelArrayList().size() - 1) {
                    System.out.println("we couldt find \"" + labels[i] + "\" among assignable labels!!!!!!!!!!!!!1");
                    return true;
                }
            }
        }
        return false;
    }

    private void printLabels(ArrayList<Label> labelArrayList) {  //prints the all available labels.
        System.out.print("Labels: ");
        int size = labelArrayList.size();
        for (int i = 0; i < size - 1; i++) {
            System.out.print(labelArrayList.get(i).getLabelText() + ", ");
        }
        if (size == 0)
            return;
        else    //prints the last element
            System.out.println(labelArrayList.get(size - 1).getLabelText());

    }

}