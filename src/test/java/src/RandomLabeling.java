package src;

import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class RandomLabeling extends LabelAssignment {//needs to extend LabelAssignment

    LinkedList<Label> labelLinkedList;
    LinkedList<Instance> instanceLinkedList;
    int instanceCount, labelCount, labelID;
    InputCreator inputCreator;
    int labelPerIns;


    public RandomLabeling() throws IOException, ParseException {
        Random rd = new Random();
        inputCreator = new InputCreator();
        labelLinkedList = inputCreator.getLabelLinkedList();
        instanceLinkedList = inputCreator.getInstanceLinkedList();
        instanceCount = instanceLinkedList.size();
        labelCount = labelLinkedList.size();


        this.labelPerIns = labelLinkedList.get(0).getLblPerIns();


        for (int i = 0; i < instanceCount; i++) {
            int assignLabelCount = rd.nextInt(2 * labelPerIns - 1);
            assignLabelCount = (assignLabelCount % labelPerIns) + 1;
            for (int j = 0; j < assignLabelCount; j++) {
                labelID = rd.nextInt((2 * labelCount) - 1);
                labelID = (labelID % labelCount) + 1;
                instanceLinkedList.get(i).setLabel(labelID);
            }
        }
        for(int i = 0; i < instanceCount ; i++){
            System.out.println(instanceLinkedList.get(i).getInstanceText() + " is labeled as " + instanceLinkedList.get(i).getLabels());
        }
    }
}
