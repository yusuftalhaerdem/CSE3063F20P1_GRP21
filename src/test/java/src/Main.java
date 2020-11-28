package src;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        LinkedList<Label> labelLinkedList = new LinkedList<Label>();
        LinkedList<Instance> instanceLinkedList = new LinkedList<Instance>();

        System.out.println("Enter input file name : ");
        Scanner scanner = new Scanner(System.in);
        String inputFileName = scanner.next();
        Input input = new Input(inputFileName, labelLinkedList, instanceLinkedList);

        input.getInputs();

        for(int i=0;i<labelLinkedList.size();i++){
            System.out.println(labelLinkedList.get(i).getLabelText());
        }

        for(int i=0;i<instanceLinkedList.size();i++){
            System.out.println(instanceLinkedList.get(i).getInstanceText());
        }
        System.out.println("");
    }


}
