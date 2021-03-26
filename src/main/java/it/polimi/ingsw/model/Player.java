package it.polimi.ingsw.model;

import java.util.Set;

public class Player {
    private String nickname;
    private int victoryPoints;
    //private Dashboard myDashboard;
    private Set<Card> developments;
    private Set<Card> leaders;
    private Set<ProductionPower> availableProduction;
    private Set<MarbleColor> availableExchange;
    private Set<Resource> availableDiscount;

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
