public class User {
    private int userID;
    private String userName;
    private String userType;





    // set Methods to attributes
    public void setUserID(int id){
        userID = id;
    }
    public void setUserName(String name){
        userName = name;
    }
    public void setUserType(String type){
        userType = type;
    }
    // get methods for attributes
    public int getUserID(){
        return userID;
    }
    public String  getUserName() {
        return userName;
    }
    public String getUserType(){
        return userType;
    }


}
