package edu.marmara.annotator;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Log log = Log.getInstance();
        log.log("Program is starting");

        ArrayList<User> userArrayList = new ArrayList<>();
        ArrayList<Dataset> datasetArrayList = new ArrayList<>();

        ArrayList<Label> labelArrayList = new ArrayList<>();
        ArrayList<Instance> instanceArrayList = new ArrayList<>();

        ArrayList<Labelling> labellingArrayList = new ArrayList<>();

        //bazı sorunları inputta hallettsek iyi olur ama oraya da ben girmek istemiyorum ./
        Input input = new Input();
        int currentDataset = input.getInputs(datasetArrayList, userArrayList, labelArrayList, instanceArrayList, labellingArrayList);

        //bunun böyle çıkmaması gerekiyor
        System.out.println("---printing the labeling objects------------------------------------------------------------");
        for (int i = 0; i < labellingArrayList.size(); i++) {
            System.out.println(labellingArrayList.get(i).toString());
        }

        Dataset dataset = findDataset(datasetArrayList,currentDataset);//hata alıyon mu bak hasancan öyle bişeyler diyordu
        RandomLabeling rl = new RandomLabeling();
        rl.retrieveData(labellingArrayList,datasetArrayList);


        //GUIDE ME YOU ____ BOI
        //this part will have a user panel addition, if user panel is passed we will go to random labelling.
        UserLabelling uL = new UserLabelling();
        boolean isRandomLabelling=uL.userLabellingOrRandomLabelling(userArrayList,datasetArrayList,currentDataset);


        //usera bir şekilde sormuş olmamız lazım random labelling yapacak mıyız, bunu birileri okusun
        if(isRandomLabelling)
            rl.labelByUser(dataset,userArrayList,labellingArrayList,datasetArrayList);

        System.out.println(" "); //THE DEBUGGER
    }

    private static Dataset findDataset(ArrayList<Dataset> datasetArrayList, int currentDataset) {
        Dataset dataset=new Dataset();
        for(int i=0;i<datasetArrayList.size();i++){
            if(datasetArrayList.get(i).getDatasetID()==currentDataset){
                dataset=datasetArrayList.get(i);
                return dataset;
            }
        }
        return dataset;
    }


}
