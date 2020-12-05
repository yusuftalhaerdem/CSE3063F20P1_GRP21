package edu.marmara.annotator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Logger;

public class RandomLabeling extends LabelAssignment {
    private static final Logger logger = Logger.getLogger(RandomLabeling.class.getName());

    LinkedList<Product> productLinkedList;
    LinkedList<User> userLinkedList;
    LinkedList<Label> labelLinkedList;
    LinkedList<Instance> instanceLinkedList;
    int instanceCount, labelCount, labelID;
    int labelPerIns;

    RandomLabeling(LinkedList<Label> labelLinkedList, LinkedList<Instance> instanceLinkedList, LinkedList<User> userLinkedList, LinkedList<Product> productLinkedList) {
        this.labelLinkedList = labelLinkedList;
        this.instanceLinkedList = instanceLinkedList;
        this.userLinkedList = userLinkedList;
        this.productLinkedList = productLinkedList;
    }

    public void labelRandomly() {
        Random rd = new Random();

        this.instanceCount = this.instanceLinkedList.size();
        this.labelCount = this.labelLinkedList.size();
        this.labelPerIns = this.labelLinkedList.get(0).getLblPerIns();


        for (int i = 0; i < instanceCount; i++) {
            Product product = new Product();
            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedDate = myDateObj.format(myFormatObj);

            int maxPossibleLabelCount = 0;
            if (labelPerIns > labelLinkedList.size()) {
                maxPossibleLabelCount = labelLinkedList.size();
            } else {
                maxPossibleLabelCount = labelPerIns;
            }
            int assignLabelCount = (int) (Math.ceil(Math.random()) * maxPossibleLabelCount);

            //int assignLabelCount = rd.nextInt(10 * labelPerIns - 1);
            //assignLabelCount = (assignLabelCount % labelPerIns) + 1;

            LinkedList<Integer> LabelCheckList;
            //LinkedList<Integer> UserCheckList;
            LabelCheckList = new LinkedList<>();
            //UserCheckList = new LinkedList<>();

            int userId = this.userLinkedList.get(rd.nextInt(this.userLinkedList.size())).getUserID();

            for (int k = 0; k < userLinkedList.size(); k++) {


                for (int j = 0; j < assignLabelCount; j++) {
                    do {
                        do {
                            labelID = (int) (Math.floor(Math.random() * labelCount));
                            //labelID = rd.nextInt((maxPossibleLabelCount * labelCount) - 1);
                            //labelID = (labelID % labelCount) + 1;
                            System.out.println("");
                        } while (labelID > (labelCount - 1));
                    }while (LabelCheckList.contains(labelID));
                    LabelCheckList.add(labelID);

                    System.out.println(k);
                    product.createProduct(labelLinkedList.get(labelID), userLinkedList.get(labelID), formattedDate, instanceLinkedList.get(i).getDatasetName(),
                            instanceLinkedList.get(i).getDatasetID(), instanceLinkedList.get(i).getMaxPerLabel(), instanceLinkedList.get(i).getInstanceText(),
                            instanceLinkedList.get(i).getInstanceID(), labelLinkedList, productLinkedList);
                    //product.getLabelList().get(j).setLabelID(labelID);
                    //product.setUser(userLinkedList.get(k));
                    //product.setDateTime(formattedDate);
                    // productLinkedList.add(product);
                    Product product1 = productLinkedList.get(j);
                    System.out.println(product1.getUserID());

                }

                //Collections.sort(productLinkedList.get(k).getLabels());
            }
        }
    }

}

