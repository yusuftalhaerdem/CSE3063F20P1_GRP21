public class Dataset {
    private int datasetID;
    private String datasetName;
    private int maxNoOfLabelsPerInstance;


    void setDatasetID(int value){
        this.datasetID = value;
    }
    int getDatasetID(){
        return this.datasetID;
    }
    void setDatasetName(String value){
        this.datasetName = value;
    }
    String getDatasetName(){
        return this.datasetName;
    }
    void setMaxNoOfLabelsPerInstance(int value){
        this.maxNoOfLabelsPerInstance = value;
    }
    int getMaxNoOfLabelsPerInstance(){
        return this.maxNoOfLabelsPerInstance;
    }

}
