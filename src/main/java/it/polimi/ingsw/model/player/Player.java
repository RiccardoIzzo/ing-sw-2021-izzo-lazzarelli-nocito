package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.ProductionPower;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardMap;

import java.util.*;

/**
 * Player class represents a player in the game.
 *
 * @author Gabriele Lazzarelli, Riccardo Izzo
 */
public class Player {
    private String nickname;
    private int victoryPoints = 0;
    private Dashboard myDashboard;
    private Set<Card> developments;
    private Set<Card> leaders;
    private ResourceMap totalResources;
    private CardMap numberOfCard;
    private CardMap levelOfCard;
    private ArrayList<ProductionPower> availableProduction;
    private Set<MarbleColor> availableExchange;
    private ResourceMap availableDiscount;

    /**
     * Constructor Player creates a new Player instance.
     * @param name player nickname.
     */
    public Player(String name){
        nickname = name;
        myDashboard = new Dashboard();
        developments = new HashSet<>();
        leaders = new HashSet<>();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setLeaders(List<LeaderCard> cards){
        leaders.addAll(cards);
    }

    public Set<Card> getLeaders(){
        return leaders;
    }

    public CardMap getNumberOfCard() {
        return numberOfCard;
    }

    public CardMap getLevelOfCard() {
        return levelOfCard;
    }

    public Dashboard getDashboard() {
        return myDashboard;
    }


    public void buyCard(){
        //code to choose DevelopmentCard
        //code to check resources
        //code to decrease resources
        /*Example
        DevelopmentCard c - instance of the bought card
        c.getProduction().activatePower(this) - see ProductionPower
         */
        //activatePower needs to be inside ProductionPower (LeaderCard uses it too)
    }

    public void addProduction(ProductionPower production){
        availableProduction.add(production);
    }

    public void removeProduction(ProductionPower production){
        availableProduction.remove(production);
    }

    public void addExchange(Set<MarbleColor> exchange){
        availableExchange.addAll(exchange);
    }

    public void addDiscount(ResourceMap discount){
        availableDiscount.addResources(discount);
    }

    public ResourceMap getTotalResources() {
        return totalResources;
    }

    public void getResources(int pos, int type, Market market){
        market.insertMarble(pos, type);
        if(market.findFaith()) myDashboard.incrementFaith(1);
        myDashboard.getWarehouse().addResourcesIntoTemporaryShelf(market.resourceOutput());
    }

    public void activateProduction(){

    }

    /**
     * Method selectLeaderCard selects two leader cards among the four available at the start of the game and deletes the others.
     * @param card1 first leader card chosen by the player.
     * @param card2 second leader card chosen by the player.
     */
    public void selectLeaderCard(Card card1, Card card2){
        leaders.removeIf(leader -> !(leader.equals(card1)) && !(leader.equals(card2)));
    }

    /**
     * Method activateLeaderCard activates a leader card.
     * @param card the leader card that must be activated.
     */
    public void activateLeaderCard(LeaderCard card){
        Iterator<Card> itr = leaders.iterator();
        while(itr.hasNext()){
            if(itr.next().equals(card)){
                LeaderCard leader = (LeaderCard) itr.next();
                leader.setAbility(this);
            }
        }
    }

    /**
     * Method discardLeaderCard discards the selected leader card and moves forward all the other players on the faith track.
     * @param card the leader card that must be discarded.
     * @param game instance of Game class.
     */
    public void discardLeaderCard(Card card, Game game){
        leaders.remove(card);
        for(Player player : game.getPlayers()){
            if(!(player.equals(this))){
                player.getDashboard().incrementFaith(1);
            }
        }
    }

    /**
     * Method calcPoints calculates the victory points achieved by a player.
     */
    public void calcPoints(){
        Iterator<Card> itrCards = developments.iterator();
        Iterator<Card> itrLeaders = leaders.iterator();
        int numResources = 0;
        // VPs for each Development Card
        while(itrCards.hasNext()){
            victoryPoints += itrCards.next().getVictoryPoints();
        }
        // VPs for each Leader Card
        while(itrLeaders.hasNext()){
            victoryPoints += itrLeaders.next().getVictoryPoints();
        }

        for(Resource resource : Resource.values()){
            numResources += totalResources.getResource(resource);
        }
        // VPs for every set of 5 resources of any type + VPs depending on the final position on the faith track + VPs based on the Pope's favor tiles
        victoryPoints += (numResources % 5) + myDashboard.getPath().getPosVictoryPoints();
    }

}
