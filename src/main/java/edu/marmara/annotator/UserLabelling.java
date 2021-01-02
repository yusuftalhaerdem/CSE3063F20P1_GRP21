package edu.marmara.annotator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class UserLabelling {
    UserLabelling(){}

    public boolean userLabellingOrRandomLabelling(ArrayList<User> userArrayList, ArrayList<Dataset> datasetArrayList,int currentDataset){

        boolean isUserLabelling=false;
        System.out.println("\n\n\n\n\n\n");
        System.out.println("----------------------Welcome to the system--------------------------\n\n\n\n\n\n");   //where the heck that kindness came from
        Scanner in = new Scanner(System.in);
        boolean isItDone=false;

        while(true) {
            //asks for user input and output
            System.out.print("enter the user id: ");
            int userID = in.nextInt();
            System.out.print("enter the user name: ");
            String userName = in.next();
            if(userName.equals("")){    //a trick left from my sleeves
                return false;
            }

            //checks if that user exist in the database
            for (int i = 0; i < userArrayList.size(); i++) {
                if (userArrayList.get(i).getUserID() == userID) {
                    if (userArrayList.get(i).getUserName().equals(userName)) {
                        isUserLabelling = true; //we run the program for the user to label
                        System.out.println("-----user succesfully entered to system.------\n\n");
                        userLabelling(userArrayList.get(i),findDataset(currentDataset,datasetArrayList),userArrayList,datasetArrayList);
                        break;
                    }
                    break;
                }
            }
            System.err.println("wrong user name/id. please try again.");

            if(isUserLabelling)
                break;

            //şuraya wait koysun biri ben koyarım siz uyuyun
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

        return isUserLabelling;
    }

    private Dataset findDataset(int currentDataset, ArrayList<Dataset> datasetArrayList) {
        for(int i=0;i<datasetArrayList.size();i++){
            if(currentDataset==datasetArrayList.get(i).getDatasetID())
                return datasetArrayList.get(i);
        }
        System.err.println("the given dataset id cannot be found. please check your values.");
        return new Dataset();   //this is a not.!
    }


    private void userLabelling(User user,Dataset dataset,ArrayList<User> userArrayList, ArrayList<Dataset> datasetArrayList){

        //bir şekilde userın son etiketlediği databasei ve instance getirmen lazım ki wtf hoca yeter aq ya
        //bunu şey yapacam datasetin içine girecek hangi elemana kadar etiketlemiş bakacak ve oradan devam edecek

        ArrayList<Instance> unlabeledInstances=new ArrayList<>();
        ArrayList<Instance> labeledInstances=new ArrayList<>();

        //retrieving data of where we left
        for(int i=0;i<dataset.getInstanceArrayList().size();i++){
            if(dataset.getInstanceArrayList().get(i).getLabels().size()>0){
                labeledInstances.add(dataset.getInstanceArrayList().get(i));
            }else{
                unlabeledInstances.add(dataset.getInstanceArrayList().get(i));
            }
        }


        //labelling part
        while(unlabeledInstances.size()!=0){    //array bitene kadar labellamaya devam edecek

            long start = System.currentTimeMillis();    //starts the clock


            ///şimdi aşağıdakileri consistency check proya göre ayıracağız

            boolean chosedNew=false;//if its true, we will label new instance
            Instance instanceToLabel;
            if(Math.random()> user.getConsistencyCheckProbability()){
                //mevcut part
                chosedNew=true;
                instanceToLabel=unlabeledInstances.get(0);

                labeledInstances.add(instanceToLabel);//listeleri düzenliyor şimdiden
                unlabeledInstances.remove(instanceToLabel);
            }else{

                //instance listin içine bakacak teker teker eğer yeni label atanabilecek bir şey bulamazsa  bass geçecek
                ArrayList<Instance> listToFindRandomInstance=new ArrayList<>(labeledInstances);

                //şunu methoda al lütfen zel
                if(listToFindRandomInstance.size()==0){
                    chosedNew=true;
                    instanceToLabel=unlabeledInstances.get(0);

                    labeledInstances.add(instanceToLabel);//listeleri düzenliyor şimdiden
                    unlabeledInstances.remove(instanceToLabel);
                }
                else {
                    do {
                        instanceToLabel = listToFindRandomInstance.get((int) (Math.random() * listToFindRandomInstance.size()));   //önceden labellanmış random bir instance seçiyor tekrar labellamak için
                        listToFindRandomInstance.remove(instanceToLabel);
                    } while (instanceToLabel.getMaxLabelPerInstance() < instanceToLabel.getLabels().size() && listToFindRandomInstance.size() != 0);

                    // eğer bulamazsa etiketlenmemişlerden seçecek
                    if (listToFindRandomInstance.size() == 0) {
                        chosedNew = true;
                        instanceToLabel = unlabeledInstances.get(0);

                        labeledInstances.add(instanceToLabel);//listeleri düzenliyor şimdiden
                        unlabeledInstances.remove(instanceToLabel);
                    }
                }
            }


            System.out.println("Instance: "+instanceToLabel.getInstanceText());
            printLabels(dataset.getLabelArrayList());
            System.out.println("if you wanna assign more than one label, please divide them by \",\"");
            System.out.print("please write the labels you want to assign for instance: ");


            //gets the labels that are supposed to assigned.
            ArrayList<Label> labelsToAssign=new ArrayList<>();

            if(readLabels(labelsToAssign,dataset)){//reads the labels user entered
                return;//if user denys to enter any label we will quit from user labelling part
            }

            //hali hazırda eklenmişler labellar dışında ne kadar yer kaldığına bakıyor
            if(labelsToAssign.size()>instanceToLabel.getMaxLabelPerInstance()-instanceToLabel.getLabels().size()){
                if(chosedNew==true){
                    unlabeledInstances.add(instanceToLabel);
                    labeledInstances.remove(instanceToLabel);
                    System.err.println("you tried to label an instance with more than max limit.");
                    continue;
                }
            }

            //moladan sonra baktım buna hata olması muhtemel
            String timeString= String.valueOf(LocalTime.now());
            timeString= timeString.substring(0,8);
            Labelling labelling = new Labelling(dataset, instanceToLabel, labelsToAssign, user, "", 0, findFinalLabel(labelsToAssign));
            labelling.setDateTime(LocalDate.now()+", "+timeString);
            dataset.getLabellingArrayList().add(labelling);
            instanceToLabel.getLabels().addAll(labelsToAssign);
            System.out.println("given labels are assigned to instance succesfully.\n\n");

            double timeSpentInLabeling = (System.currentTimeMillis() - start) / 1000F; //calculates the time elapsed from start of labeling-----not sure-----
            labelling.setTimeSpent(timeSpentInLabeling);



            //call the metrics calculations and output printing here.


        }

        System.out.println("\n\n\n-----FINISHED LABELING ALL INSTANCES.------\n\n\n");

    }

    private boolean readLabels(ArrayList<Label> labelsToAssign, Dataset dataset) {//reads the labels user entered
        Scanner in=new Scanner(System.in);
        String line= in.nextLine();
        if(line.equals("")) //if user denys to enter any label we will quit from user labelling part
            return true;
        String labels[]=line.split(",");
        for(int i=0;i<labels.length;i++){
            labels[i]=labels[i].trim();//boşluklarını alıyor
            for(int j=0;j<dataset.getLabelArrayList().size();j++){
                if(labels[i].equals(dataset.getLabelArrayList().get(j).getLabelText())){
                    labelsToAssign.add(dataset.getLabelArrayList().get(j));
                    break;
                }
                if(j==dataset.getLabelArrayList().size()-1){
                    System.err.println("we couldt find \""+labels[i]+"\" among assignable labels.");
                }
            }
        }
        return false;
    }

    private void printLabels(ArrayList<Label> labelArrayList){  //prints the all available labels.
        System.out.println("Labels: ");
        int size=labelArrayList.size();
        for(int i=0;i<size-1;i++){
            System.out.print(labelArrayList.get(i).getLabelText()+", ");
        }
        if(size==0)
            return;
        else    //prints the last element
            System.out.println(labelArrayList.get(size-1).getLabelText());

    }

    //fatihin yaptığı, labellinge eklemiş bunu ama bu instanceda olacaktı silincek de idare etsin şuanlık yorgunum dostlar
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
