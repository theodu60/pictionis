package app.pictionis.com.pictionis;

import java.util.ArrayList;

/**
 * Created by theophilehemachandra on 17/10/2016.
 */

public class Games {
    private Users master;
    private ArrayList<Users> players = new ArrayList<Users>();
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
}
