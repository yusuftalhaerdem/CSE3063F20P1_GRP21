package src;

import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.util.Collections;
//import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

public class RandomLabeling extends LabelAssignment {

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
            int assignLabelCount = rd.nextInt(5 * labelPerIns - 1);
            assignLabelCount = (assignLabelCount % labelPerIns) + 1;
            LinkedList<Integer> RandomCheckList;
            RandomCheckList = new LinkedList<>();
            for (int j = 0; j < assignLabelCount; j++) {
                do{
                    labelID = rd.nextInt((5 * labelCount) - 1);
                    labelID = (labelID % labelCount) + 1; }
                while(RandomCheckList.contains(labelID));
                RandomCheckList.add(labelID);
                instanceLinkedList.get(i).setLabel(labelID);
            }
            Collections.sort(instanceLinkedList.get(i).getLabels());
        }
        for(int i = 0; i < instanceCount ; i++){
            System.out.println(instanceLinkedList.get(i).getInstanceText() + " is labeled with labels " + instanceLinkedList.get(i).getLabels());
        }
    }
}
