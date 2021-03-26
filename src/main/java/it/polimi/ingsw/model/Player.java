package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Set;

public class Player {
    private String nickname;
    private int victoryPoints;
    //private Dashboard myDashboard;
    private Set<Card> developments;
    private Set<Card> leaders;
    private ArrayList<ProductionPower> availableProduction;
    private Set<MarbleColor> availableExchange;
    private ResourceMap availableDiscount;

    public Player(String name){
        nickname = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void buyCard(){
        //code to choose DevelopmentCard
        //code to check resources
        //code to decrease resources
        /*Example
        DevelopmentCard c - instance of the bought card
        c.production.activatePower(this) - see ProductionPower
         */
        //activatePower needs to be inside ProductionPower (LeaderCard uses it too)
    }

    public void addProduction(ProductionPower production){
        availableProduction.add(production);
    }

    public void removeProduction(ProductionPower production){
        availableProduction.remove(production);
    }
}
