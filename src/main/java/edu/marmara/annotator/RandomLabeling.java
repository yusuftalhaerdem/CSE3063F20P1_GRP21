package edu.marmara.annotator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class RandomLabeling extends LabelAssignment {
    User user;
    LinkedList<Label> labelLinkedList = new LinkedList<Label>();
    LinkedList<Instance> instanceLinkedList = new LinkedList<Instance>();
    int instanceCount, labelCount, labelID;
    int labelPerIns;

    RandomLabeling(LinkedList<Label> labelLinkedList, LinkedList<Instance> instanceLinkedList, User user) {
        this.labelLinkedList = labelLinkedList;
        this.instanceLinkedList = instanceLinkedList;
        this.user = user;
    }

    public void labelRandomly() {
        Random rd = new Random();


        this.instanceCount = this.instanceLinkedList.size();
        this.labelCount = this.labelLinkedList.size();
        this.labelPerIns = this.labelLinkedList.get(0).getLblPerIns();


        for (int i = 0; i < instanceCount; i++) {
            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedDate = myDateObj.format(myFormatObj);

            int assignLabelCount = rd.nextInt(5 * labelPerIns - 1);
            assignLabelCount = (assignLabelCount % labelPerIns) + 1;
            LinkedList<Integer> RandomCheckList;
            RandomCheckList = new LinkedList<>();
            for (int j = 0; j < assignLabelCount; j++) {
                do {
                    labelID = rd.nextInt((5 * labelCount) - 1);
                    labelID = (labelID % labelCount) + 1;
                }
                while (RandomCheckList.contains(labelID));
                RandomCheckList.add(labelID);
                instanceLinkedList.get(i).setLabel(labelID);
                instanceLinkedList.get(i).setDateTime(formattedDate);
            }
            Collections.sort(instanceLinkedList.get(i).getLabels());
        }

        for (int i = 0; i < instanceCount; i++) {
            System.out.println(instanceLinkedList.get(i).getInstanceText() + " is labeled with labels " + instanceLinkedList.get(i).getLabels());
        }

    }

}

