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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {

        JSONParser jsonParser=new JSONParser();
        FileReader reader=new FileReader("/book.json");
        Object obj=jsonParser.parse(reader);
        JSONObject jsonobj=(JSONObject)obj;
        Input input = new Input(jsonobj);
        input.getInputs();


    }


}
