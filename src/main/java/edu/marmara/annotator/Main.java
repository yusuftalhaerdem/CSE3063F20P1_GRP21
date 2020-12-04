package edu.marmara.annotator;


import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        LinkedList<Label> labelLinkedList = new LinkedList<Label>();
        LinkedList<Instance> instanceLinkedList = new LinkedList<Instance>();
        User user = new User();
        String fileName = askInputFileName();
        Input input = new Input(fileName, labelLinkedList, instanceLinkedList);
        input.getInputs();

        System.out.print("");

        RandomLabeling labeling = new RandomLabeling(labelLinkedList, instanceLinkedList, user);
        labeling.labelRandomly();


    }

    private static String askInputFileName(){
        System.out.println("Enter file name : ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

}
