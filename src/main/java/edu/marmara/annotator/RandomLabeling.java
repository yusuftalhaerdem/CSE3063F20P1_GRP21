package edu.marmara.annotator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
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
            int assignLabelCount;
            LinkedList<Integer> LabelCheckList;
            LabelCheckList = new LinkedList<>();

            assignLabelCount = (int) (Math.ceil(Math.random() * maxPossibleLabelCount));

            int userIndex = (int) (Math.floor(Math.random() * userLinkedList.size()));
            for (int j = 0; j < assignLabelCount; j++) {
                do {
                    do {
                        labelID = (int) (Math.floor(Math.random() * labelCount));
                    } while (labelID > (labelCount - 1));
                } while (LabelCheckList.contains(labelID));
                LabelCheckList.add(labelID);


                product.createProduct(labelLinkedList.get(labelID), userLinkedList.get(userIndex), formattedDate, instanceLinkedList.get(i).getDatasetName(),
                        instanceLinkedList.get(i).getDatasetID(), instanceLinkedList.get(i).getMaxPerLabel(), instanceLinkedList.get(i).getInstanceText(),
                        instanceLinkedList.get(i).getInstanceID(), labelLinkedList, instanceLinkedList);

                productLinkedList.add(product);
            }
        }
    }

}
