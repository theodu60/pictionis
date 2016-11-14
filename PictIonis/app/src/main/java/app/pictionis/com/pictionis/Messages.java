package app.pictionis.com.pictionis;

/**
 * Created by theophilehemachandra on 14/11/2016.
 */

public class Messages {
    private String email;
    private String message;

    public Messages(){

    }
    public Messages(String email, String message){
        this.email = email;
        this.message = message;
    }
    public String getEmail(){
        return this.email;
    }
    public String getMessage(){
        return this.message;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public void setMessage(String message){
        this.message = message;
    }
}
