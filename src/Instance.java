package src;
public class Instance {
    private String instance;
    private int instanceID;

    public Instance(int instanceID, String instance) {
        this.instanceID = instanceID;
        this.instance= instance;
    }


    // set methods for attribute
    public void setInstance(String _instance){
        this.instance = instance;
    }
    //get methods for attributes
    public String getInstance(){
        return instance;
    }
    public int getInstanceID() {
        return instanceID;
    }
    public void setInstanceID(int instanceID) {
        this.instanceID = instanceID;
    }
}
