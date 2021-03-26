package it.polimi.ingsw.model;

public class Player {
    private String nickname;
    private int victoryPoints;

    public Player(String name){
        nickname = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
