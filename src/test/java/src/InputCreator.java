package src;


import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.util.*;

public class InputCreator {

    LinkedList<Label> labelLinkedList;
    LinkedList<Instance> instanceLinkedList;

    public InputCreator() throws IOException, ParseException {

        instanceLinkedList = new LinkedList<Instance>();
        labelLinkedList = new LinkedList<Label>();
        System.out.println("Enter input file name : ");
        Scanner scanner = new Scanner(System.in);
        String inputFileName = scanner.next();
        Input input = new Input(inputFileName, labelLinkedList, instanceLinkedList);

        input.getInputs();

        for (int i = 0; i < labelLinkedList.size(); i++) {
            System.out.println(labelLinkedList.get(i).getLabelText());
        }

        for (int i = 0; i < instanceLinkedList.size(); i++) {
            System.out.println(instanceLinkedList.get(i).getInstanceText());
        }
        System.out.println("");
    }

    public LinkedList<Label> getLabelLinkedList(){
        return this.labelLinkedList;
    }

    public LinkedList<Instance> getInstanceLinkedList(){
        return this.instanceLinkedList;
    }

}
