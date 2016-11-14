package app.pictionis.com.pictionis;

import java.util.ArrayList;

/**
 * Created by theophilehemachandra on 17/10/2016.
 */

public class Games {
    private Users master;
    private ArrayList<Users> players = new ArrayList<Users>();
    private ArrayList<Messages> messages = new ArrayList<Messages>();
    private String imgBase64;

    public Games(){

    }
    public Games(Users master, ArrayList<Users> players, ArrayList<Messages> messages){
        this.master = master;
        this.players = players;
        this.messages = messages;
    }
    public Users getMaster(){
        return this.master;
    }
    public void setMaster(Users master){
        this.master = master;
    }
    public ArrayList<Users> getPlayers(){
        return this.players;
    }
    public void setPlayers(Users player){
        this.players.add(player);
    }
    public ArrayList<Messages> getMessages(){
        return this.messages;
    }
    public void setMessages(Messages message){
        this.messages.add(message);
    }
    public String getImgBase64(){
        return this.imgBase64;
    }
    public void setImgBase64(String imgBase64){
        this.imgBase64 = imgBase64;
    }

}
