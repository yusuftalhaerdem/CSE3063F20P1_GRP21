package src;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
//import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RandomLabeling extends LabelAssignment {
    private static final Logger logger = Logger.getLogger( LabelAssignment.class.getName());
    FileHandler fileHandler;

    LinkedList<Label> labelLinkedList;
    LinkedList<Instance> instanceLinkedList;
    Dataset datasetInfo;
    int instanceCount, labelCount, labelID;
    InputCreator inputCreator;
    int labelPerIns;


    public RandomLabeling(FileHandler fileHandler) throws IOException, ParseException {
        this.fileHandler = fileHandler;

        logger.addHandler(fileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);

        Random rd = new Random();
        inputCreator = new InputCreator(fileHandler);
        this.labelLinkedList = inputCreator.getLabelLinkedList();
        this.instanceLinkedList = inputCreator.getInstanceLinkedList();
        this.datasetInfo = inputCreator.getDatasetInfo();
        this.instanceCount = instanceLinkedList.size();
        this.labelCount = labelLinkedList.size();

        this.labelPerIns = labelLinkedList.get(0).getLblPerIns();



        for (int i = 0; i < instanceCount; i++) {
            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedDate = myDateObj.format(myFormatObj);

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
                instanceLinkedList.get(i).setDateTime(formattedDate);
            }
            Collections.sort(instanceLinkedList.get(i).getLabels());
        }

        for(int i = 0; i < instanceCount ; i++){
            System.out.println(instanceLinkedList.get(i).getInstanceText() + " is labeled with labels " + instanceLinkedList.get(i).getLabels());
        }
    }

    public LinkedList<Label> getLabelLinkedList() {
        return labelLinkedList;
    }

    public LinkedList<Instance> getInstanceLinkedList() {
        return instanceLinkedList;
    }

    public Dataset getDatasetInfo() {
        return datasetInfo;
    }

    public int getInstanceCount() {
        return instanceCount;
    }

    public int getLabelCount() {
        return labelCount;
    }
}
