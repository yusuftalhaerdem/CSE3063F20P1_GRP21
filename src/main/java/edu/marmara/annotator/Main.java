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

        ArrayList<Labeling> labelingArrayList = new ArrayList<>();
        ArrayList<User> lastUsers = new ArrayList<>();
        ArrayList<Instance> lastInstance = new ArrayList<>();

        //hasancan input alıyor ama yanlış biraz bakacaz şuna konuşcaz mümkünse bugün
        Input input = new Input(datasetArrayList, userArrayList, labelArrayList, instanceArrayList, labelingArrayList);
        int currentDataset = input.getInputs();

        //bunun böyle çıkmaması gerekiyor
        System.out.println("---printing the labeling objects------------------------------------------------------------");
        for (int i = 0; i < labelingArrayList.size(); i++) {
            System.out.println(labelingArrayList.get(i).toString());
        }

        Dataset dataset = datasetArrayList.get(currentDataset - 1);//hata alıyon mu bak hasancan öyle bişeyler diyordu
        RandomLabeling rl = new RandomLabeling(dataset, userArrayList, labelingArrayList, datasetArrayList);
        rl.retriveData();
        rl.labelByUser();
    }


}
