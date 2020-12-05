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
        fileHandler = new FileHandler("app.log", true);
        LinkedList<Label> labelLinkedList = new LinkedList<Label>();
        LinkedList<Instance> instanceLinkedList = new LinkedList<Instance>();
        LinkedList<User> userLinkedList = new LinkedList<User>();
        LinkedList<Product> productLinkedList = new LinkedList<Product>();
        String fileName = askInputFileName();

        do {
            User user = new User();
            userLinkedList.add(user);
        } while (askUserToContinue());

        Input input = new Input(fileName, labelLinkedList, instanceLinkedList);
        input.getInputs();

        System.out.print("");

        RandomLabeling labeling = new RandomLabeling(labelLinkedList, instanceLinkedList, userLinkedList, productLinkedList);
        labeling.labelRandomly();

        System.out.print("");

      /*      Output out = new Output(labeling, userLinkedList);
            out.writeToFile();
            System.out.print("");
*/
    }

    private static boolean askUserToContinue() {
        System.out.println("If you want to exit press 0,if not press number other than 0");
        Scanner scan = new Scanner(System.in);
        int num = scan.nextInt();
        if (num == 0) {
            return false;
        } else {
            return true;
        }

    }

    private static String askInputFileName() {
        System.out.println("Enter file name : ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public static void log(Logger logger, String log) {

        logger.addHandler(fileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
        logger.info(log);
    }
}
