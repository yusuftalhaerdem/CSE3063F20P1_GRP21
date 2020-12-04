package edu.marmara.annotator;


import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    static FileHandler fileHandler;
    public static void main(String[] args) throws IOException, ParseException {
        fileHandler = new FileHandler("app.log",true);
        LinkedList<Label> labelLinkedList = new LinkedList<Label>();
        LinkedList<Instance> instanceLinkedList = new LinkedList<Instance>();
        User user = new User();
        String fileName = askInputFileName();
        Input input = new Input(fileName, labelLinkedList, instanceLinkedList);
        input.getInputs();

        System.out.print("");

        RandomLabeling labeling = new RandomLabeling(labelLinkedList, instanceLinkedList, user);
        labeling.labelRandomly();

        Output out = new Output(labeling, user);
        out.writeToFile();
    }

    private static String askInputFileName(){
        System.out.println("Enter file name : ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public static void log(Logger logger,String log){

        logger.addHandler(fileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
        logger.info(log);
    }
}
