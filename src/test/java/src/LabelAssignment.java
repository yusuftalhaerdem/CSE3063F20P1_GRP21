package src;
public class LabelAssignment {
    private int instanceID;
    private int[] labelID;

    public LabelAssignment(){}

    public LabelAssignment(int instanceID, int[] labelID){
        this.instanceID=instanceID;
        this.labelID=labelID;
    }
    //set methods for attributes
    public void setInstanceID(int id){
        instanceID = id;
    }
    public void setLabelID(int id){
        labelID[instanceID] = id;
    }
    //get methods for attributes
    public int getInstanceID(){
        return instanceID;
    }
    public int getLabelID(){
        return labelID[instanceID];
    }

}
