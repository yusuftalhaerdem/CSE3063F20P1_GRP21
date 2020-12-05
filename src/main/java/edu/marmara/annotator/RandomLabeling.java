package edu.marmara.annotator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Logger;

public class RandomLabeling extends LabelAssignment {
    private static final Logger logger = Logger.getLogger( RandomLabeling.class.getName());

    LinkedList<User> userLinkedList;
    LinkedList<Label> labelLinkedList;
    LinkedList<Instance> instanceLinkedList;
    int instanceCount, labelCount, labelID;
    int labelPerIns;

    RandomLabeling(LinkedList<Label> labelLinkedList, LinkedList<Instance> instanceLinkedList, LinkedList<User> userLinkedList) {
        this.labelLinkedList = labelLinkedList;
        this.instanceLinkedList = instanceLinkedList;
        this.userLinkedList = userLinkedList;
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
                int userId = this.userLinkedList.get(rd.nextInt(this.userLinkedList.size())).getUserID();
                instanceLinkedList.get(i).setUserId(userId);
                instanceLinkedList.get(i).setLabel(labelID);
                instanceLinkedList.get(i).setDateTime(formattedDate);
            }
            Collections.sort(instanceLinkedList.get(i).getLabels());
        }
    }

}

