package edu.marmara.annotator;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class InputCreator{
    private static final Logger logger = Logger.getLogger( InputCreator.class.getName());
    FileHandler fileHandler;

    LinkedList<Label> labelLinkedList;
    LinkedList<Instance> instanceLinkedList;
    Dataset datasetInfo;

    public InputCreator(FileHandler fileHandler) throws IOException{
        this.fileHandler = fileHandler;
        logger.addHandler(fileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);

        instanceLinkedList = new LinkedList<>();
        labelLinkedList = new LinkedList<>();
        datasetInfo = new Dataset(this.fileHandler);
        System.out.println("Enter input file name : ");
        Scanner scanner = new Scanner(System.in);
        String inputFileName = scanner.next();
        Input input = new Input(fileHandler,inputFileName, labelLinkedList, instanceLinkedList, datasetInfo);

        input.getInputs();

        for (Label label : labelLinkedList){
            System.out.println(label.getLabelText());
        }

        for (Instance instance : instanceLinkedList){
            System.out.println(instance.getInstanceText());
        }
        System.out.println();
    }

    public LinkedList<Label> getLabelLinkedList(){
        return this.labelLinkedList;
    }

    public LinkedList<Instance> getInstanceLinkedList(){
        return this.instanceLinkedList;
    }

    public Dataset getDatasetInfo() {
        return this.datasetInfo;
    }
}
