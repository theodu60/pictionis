package app.pictionis.com.pictionis;

/**
 * Created by theophilehemachandra on 12/10/2016.
 */

public class Users {
    private String email;
    private String id;


    public  Users(){

    }
    public  Users(String email, String id){
        this.email = email;
        this.id = id;
    }
    public String getEmail(){
        return this.email;
    }
    public String getId(){
        return this.id;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public void setId(String id){
        this.id = id;
    }
}
