package src;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;

public class Input {

    private JSONObject jsonObject;

    Input(JSONObject jsonObject){
        this.jsonObject=jsonObject;
    }


    public void getInputs(){
        try{

            JSONParser jsonParser=new JSONParser();
            FileReader reader=new FileReader("/book.json");
            Object obj=jsonParser.parse(reader);
            JSONObject jsonObject=(JSONObject)obj;

            int datasetId=(int)(long)jsonObject.get("dataset id");
            System.out.println("dataset id = " + datasetId);
            String datasetName=(String)jsonObject.get("dataset name");
            System.out.println("dataset name = " + datasetName);
            int lblPerIns=(int)(long)jsonObject.get("maximum number of labels per instance");
            System.out.println("label per instance = " + lblPerIns);

            JSONArray classLabel=(JSONArray)jsonObject.get("class labels");
            for (int i=0;i<classLabel.size();i++){
                JSONObject address=(JSONObject)classLabel.get(i);

                int labelId=(int)(long)address.get("label id");
                String labelText=(String)address.get("label text");

                System.out.println("Label Id -> " + labelId);
                System.out.println("Label Text -> " + labelText);
            }

            JSONArray instance=(JSONArray)jsonObject.get("instances");
            for (int i=0;i<instance.size();i++){
                JSONObject address=(JSONObject)instance.get(i);

                int labelId=(int)(long)address.get("id");
                String labelText=(String)address.get("instance");

                System.out.println("Id -> " + labelId);
                System.out.println("Instance -> " + labelText);
            }



        } catch (Exception e){
            e.printStackTrace();
        }
    }



}
