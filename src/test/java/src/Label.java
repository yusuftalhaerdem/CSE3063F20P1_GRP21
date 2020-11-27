package src;
public class Label {
    private String labelText;
    private int labelID;

    public Label(int labelID, String labelText) {
        this.labelID = labelID;
        this.labelText=labelText;
    }


    //set methods for attributes
    public void setLabelText(String text){
        labelText = text;
    }
    // get methods for attributes
    public String getLabelText(){
        return labelText;
    }
    public void setLabelID(int labelID) {
        this.labelID = labelID;
    }
    public int getLabelID() {
        return labelID;
    }
}
