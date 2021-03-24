package it.polimi.ingsw.model;

public class Player {
    String nickname;

    public Player(String name){
        nickname = name;
    }

    public String getName(){
        return nickname;
    }
}
