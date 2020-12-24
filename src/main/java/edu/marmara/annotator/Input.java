package edu.marmara.annotator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;

public class Input {

    private ArrayList<Dataset> datasetArrayList;
    private ArrayList<User> userArrayList;
    private ArrayList<Label> labelArrayList;
    private ArrayList<Instance> instanceArrayList;
    private ArrayList<Labelling> labellingArrayList;
    private String filePath;
    private String datasetPath;
    private String datasetName;
    private int datasetId;

    public Input(ArrayList<Dataset> datasetArrayList, ArrayList<User> userArrayList, ArrayList<Label> labelArrayList, ArrayList<Instance> instanceArrayList, ArrayList<Labelling> labellingArrayList) {
        this.datasetArrayList = datasetArrayList;
        this.userArrayList = userArrayList;
        this.filePath = "config.json";
        this.instanceArrayList = instanceArrayList;
        this.labelArrayList = labelArrayList;
        this.labellingArrayList = labellingArrayList;
    }

    public Input() {
    }

    public int getInputs() {
        Log log = Log.getInstance();

        int current_dataset_id = -1;
        try {
            JSONParser parser = new JSONParser();
            filePath = "config.json";
            FileReader fileReader = new FileReader(filePath);
            Object obj = parser.parse(fileReader);
            JSONObject jsonObject = (JSONObject) obj;

            current_dataset_id = ((Long)jsonObject.get("current dataset")).intValue();

            // config dosyasından user değerleri alınan for döngüsü
            JSONArray users = (JSONArray) jsonObject.get("users");
            for (int i = 0; i < users.size(); i++) {
                JSONObject address = (JSONObject) users.get(i);

                String userName = (String) address.get("user name");
                int userId = ((Long)address.get("user id")).intValue();
                String userType = (String) address.get("user type");
                double checkProbability = (double) address.get("consistency check probability");

                JSONArray assigned = (JSONArray) address.get("assigned databases");
                ArrayList<Integer> assignedArray = new ArrayList<>();
                for (int j = 0; j < assigned.size(); j++) {
                    assignedArray.add(((Long) assigned.get(j)).intValue());
                }

                User user = new User(userId, userName, userType, assignedArray, checkProbability);
                userArrayList.add(user);
            }

            // config dosyasından dataset değerleri alınan for döngüsü
            JSONArray datasets = (JSONArray) jsonObject.get("datasets");
            for (int i = 0; i < datasets.size(); i++) {
                JSONObject address = (JSONObject) datasets.get(i);

                datasetPath = (String) address.get("path");
                datasetId = ((Long) address.get("dataset id")).intValue();
                datasetName = (String) address.get("dataset name");
                ArrayList<User> assignedUsers = new ArrayList<>();

                JSONArray assigned = (JSONArray) address.get("assigned users");
                for (int j = 0; j < assigned.size(); j++) {
                    for(User user : userArrayList){
                        if(user.getUserID() == ((Long)assigned.get(j)).intValue()){
                            assignedUsers.add(user);
                        }
                    }
                }

                Dataset dataset = new Dataset(datasetId, datasetName, datasetPath, assignedUsers);
                datasetArrayList.add(dataset);


            }

            for (int i = 0; i < this.datasetArrayList.size(); i++) {
                try {
                    String path = datasetArrayList.get(i).getDatasetPath();
                    FileReader reader = new FileReader(path);
                    Object o = parser.parse(reader);
                    JSONObject jo = (JSONObject) o;
                    int labelPerIns = ((Long) jo.get("maximum number of labels per instance")).intValue();

                    System.out.println("Dataset Id = " + datasetArrayList.get(i).getDatasetID()
                            + "\nDataset Name = " + datasetArrayList.get(i).getDatasetName()
                            + "\nDataset Assign Users = " + datasetArrayList.get(i).getAssignedUsersArrayList());

                    JSONArray labels = (JSONArray) jo.get("class labels");
                    for (int p = 0; p < labels.size(); p++) {
                        JSONObject adress = (JSONObject) labels.get(p);
                        String labelText = (String) adress.get("label text");
                        int labelId = ((Long) adress.get("label id")).intValue();
                        Label label = new Label(labelId, labelText, datasetArrayList.get(i).getDatasetName(), datasetArrayList.get(i).getDatasetID(), labelPerIns);
                        labelArrayList.add(label);

                        System.out.println("label Id = " + labelId + " | " + labelText);
                    }

                    JSONArray instances = (JSONArray) jo.get("instances");
                    for (int j = 0; j < instances.size(); j++) {
                        JSONObject adress = (JSONObject) instances.get(j);
                        String instanceText = (String) adress.get("instance");
                        int instanceId = ((Long) adress.get("id")).intValue();
                        Instance instance = new Instance(instanceId, instanceText, datasetArrayList.get(i).getDatasetID(), datasetArrayList.get(i).getDatasetName(), labelPerIns);
                        instanceArrayList.add(instance);

                        System.out.println("Instance Id = " + instanceId + " | " + instanceText);
                    }
                    System.out.println("\n");

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            //output okuduğumuz try-catch
            try (FileReader reader = new FileReader("output.json")) {

                Object object = parser.parse(reader);
                JSONObject jo = (JSONObject) object;

                if (!jo.isEmpty()) {
                    JSONArray dataset_json_array = (JSONArray)jo.get("Datasets");
                    for (int i=0; i<dataset_json_array.size(); i++){
                        JSONObject adres = (JSONObject)dataset_json_array.get(i);
                        int dataset_id =  ((Long) adres.get("datasetId")).intValue();

                        JSONArray labelassigment = (JSONArray) adres.get("labelAssignments");
                        for (int j = 0; j < labelassigment.size(); j++) {
                            JSONObject adress = (JSONObject) labelassigment.get(j);

                            int instanceId = ((Long) adress.get("instance id")).intValue();

                            ArrayList<Integer> labelId = new ArrayList<>();
                            JSONArray label_Id = (JSONArray) adress.get("class label ids");
                            for (int k = 0; k < label_Id.size(); k++) {

                                Long temp = (Long) label_Id.get(k);
                                labelId.add(temp.intValue());
                            }
                            int userId = ((Long) adress.get("user id")).intValue();
                            String dateTime = (String) adress.get("datetime");

                            Labelling labelling = new Labelling(findDataset(dataset_id), findInstance(instanceId), translateLabelArray(labelId), findUser(userId), dateTime);

                            labellingArrayList.add(labelling);
                            log.log(String.format("user id:%s %s tagged instance id:%s with class label %s instance:\"%s\"",
                                    labelling.getUser().getUserID(), labelling.getUser().getUserType(), labelling.getInstance().getInstanceID(),
                                    labelling.getLabelArrayList(), labelling.getInstance().getInstanceText()));
                        }
                    }
                } else {

                    System.out.println("This output.json file is empty..");
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            System.out.println(e);
            //logger.warning("Please provide a valid filepath");
            System.exit(1);
        }


        assignInstanceAndLabelsIntoDatasets();

        return current_dataset_id;
    }
    private void assignInstanceAndLabelsIntoDatasets() {
        for (int di = 0; datasetArrayList.size() > di; di++) { //teker teker datasetleri geziyor

            for (int i = 0; labelArrayList.size() > i; i++) //teker teker labellerı geziyo
                if (labelArrayList.get(i).getDatasetID() == datasetArrayList.get(di).getDatasetID())
                    datasetArrayList.get(di).getLabelArrayList().add(labelArrayList.get(i));

            for (int i = 0; instanceArrayList.size() > i; i++) //teker teker instanceları geioyr
                if (instanceArrayList.get(i).getDatasetID() == datasetArrayList.get(di).getDatasetID())
                    datasetArrayList.get(di).getInstanceArrayList().add(instanceArrayList.get(i));

        }
    }

    private ArrayList<Label> translateLabelArray(ArrayList<Integer> labels) {
        ArrayList<Label> labelArrayList = new ArrayList<>();
        for (int i = 0; labels.size() > i; i++)
            labelArrayList.add(findLabel(labels.get(i)));

        return labelArrayList;
    }

    private User findUser(int userID) {
        for (int i = 0; i < userArrayList.size(); i++)
            if (userArrayList.get(i).getUserID() == userID)
                return userArrayList.get(i);

        return new User();
    }

    private Label findLabel(int labelID) {
        for (int i = 0; i < labelArrayList.size(); i++)
            if (labelArrayList.get(i).getLabelID() == labelID)
                return labelArrayList.get(i);

        return new Label();
    }

    private Instance findInstance(int instanceID) {
        for (int i = 0; i < instanceArrayList.size(); i++)
            if (instanceArrayList.get(i).getInstanceID() == instanceID)
                return instanceArrayList.get(i);

        return new Instance();
    }

    private Dataset findDataset(int datasetID) {
        for (int i = 0; i < datasetArrayList.size(); i++)
            if (datasetArrayList.get(i).getDatasetID() == datasetID)
                return datasetArrayList.get(i);
        return new Dataset();
    }

}
