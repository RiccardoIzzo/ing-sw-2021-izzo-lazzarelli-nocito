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
    public Player(String name, boolean singlePlayer){
        nickname = name;
        myDashboard = new Dashboard(singlePlayer);
        developments = new HashSet<>();
        leaders = new HashSet<>();
        availableProduction = new ArrayList<>();
        availableExchange = new HashSet<>();
        availableDiscount = new ResourceMap();
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

    public Set<Card> getDevelopments(){
        return developments;
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

    /**
     * Method addProduction takes as parameter a Production power and adds it to the ArrayList of availableProduction.
     * Example: when a player buys a DevelopmentCard then its ProductionPower is available to the player.
     * @param production is the ProductionPower to add the list of availableProductions.
     */
    public void addProduction(ProductionPower production){
        availableProduction.add(production);
    }

    /**
     * Method removeProduction takes as parameter a ProductionPower and removes it from the ArrayList of availableProduction.
     * Example: the ProductionPower belongs to a DevelopmentCard, if this Card is topped by another of the same type but of the
     * next level then the old ProductionPower is no more available.
     * @param production is ProductionPower to remove from the list of availableProductions.
     */
    public void removeProduction(ProductionPower production){
        availableProduction.remove(production);
    }

    /**
     * Method addExchange takes as parameter a Set<MarbleColor> and adds its values to the Set of availableExchanges.
     * @param exchange is a Set of MarbleColor, it contains the MarbleColor(s) which can be exchanged for the white marble.
     */
    public void addExchange(Set<MarbleColor> exchange){
        availableExchange.addAll(exchange);
    }

    /**
     * Method addDiscount takes as parameter a ResourceMap and adds its values to the ResourceMap of availableDiscounts.
     * @param discount is a ResourceMap, the values represent the total discount for each Resource
     */
    public void addDiscount(ResourceMap discount){
        availableDiscount.addResources(discount);
    }

    /**
     * Method getTotalResources returns the sum of resources in both the warehouse and the strongbox
     * @return the available resources of this Player
     */
    public ResourceMap getTotalResources() {
        return totalResources;
    }

    /**
     * Method getResources takes resources from the market by letting slide the marble in the selected index.
     * If by performing this action a red marble is found, the player moves forward on the faith track.
     * The resources obtained are finally added to the temporary shelf where they can be managed by the player.
     * @param pos row/column index.
     * @param type represent the user choice: 1 = row, 2 = column.
     * @param market instance of Market.
     */
    public void getResources(int pos, int type, Market market){
        market.insertMarble(pos, type);
        if(market.findFaith()) myDashboard.incrementFaith(1);
        myDashboard.getWarehouse().addResourcesIntoTemporaryShelf(market.resourceOutput());
    }


    /**
     * activateProduction calls the method activatePower of the chosen production
     * @param index : index of the production to activate
     */
    public void activateProduction(int index){
       if ( index < availableProduction.size() ) {
           availableProduction.get(index).activatePower(this);
        }
    }

    /**
     * Method selectLeaderCard selects two leader cards among the four available at the start of the game and deletes the others.
     * @param firstCard first leader card chosen by the player.
     * @param secondCard second leader card chosen by the player.
     */
    public void selectLeaderCard(Card firstCard, Card secondCard){
        leaders.removeIf(leader -> !(leader.equals(firstCard)) && !(leader.equals(secondCard)));
    }

    /**
     * Method activateLeaderCard activates a leader card.
     * @param leaderCard the leader card that must be activated.
     */
    public void activateLeaderCard(Card leaderCard){
        for (Card card : leaders) {
            if (card.equals(leaderCard)) {
                ((LeaderCard) card).setActive(true);
                ((LeaderCard) card).setAbility(this);
            }
        }
    }

    /**
     * Method discardLeaderCard discards the selected leader card and increments the player position on the faith track.
     * @param card the leader card that must be discarded.
     */
    public void discardLeaderCard(Card card){
        leaders.remove(card);
        myDashboard.incrementFaith(1);
    }

    /**
     * Method getVictoryPoints returns the number of victory points achieved by a player.
     * @return the victory points.
     */
    public int getVictoryPoints(){
        int victoryPoints = 0;
        int numResources = 0;
        // VPs for each Development Card
        for(Card card : getDevelopments()){
            victoryPoints += card.getVictoryPoints();
        }
        // VPs for each Leader Card
        for(Card card : getLeaders()){
            if(((LeaderCard) card).isActive()) victoryPoints += card.getVictoryPoints();
        }
        for(Resource resource : Resource.values()){
            numResources += totalResources.getResource(resource);
        }
        // VPs for every set of 5 resources of any type + VPs depending on the final position on the faith track + VPs based on the Pope's favor tiles
        victoryPoints += (numResources % 5) + myDashboard.getPath().getPosVictoryPoints() + myDashboard.getPath().getPointsForTiles();
        return victoryPoints;
    }

}
