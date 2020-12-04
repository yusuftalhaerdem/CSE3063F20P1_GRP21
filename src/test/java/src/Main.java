package src;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        OutputCreator outputCreator;
        String username;
        User user = new User();
        Random rd = new Random();
        LinkedList<Label> labelLinkedList;
        LinkedList<Instance> instanceLinkedList;

       // System.out.println("If you want to assign random labels, pls type Random");
        System.out.println("Please enter Username: ");

        Scanner scanner = new Scanner(System.in);
        username = scanner.next();
        user.setUserName(username);
        user.setUserID(rd.nextInt());


        outputCreator = new OutputCreator(user,user.getFileHandler());
        System.out.println(outputCreator.instanceCount);
    }
}
