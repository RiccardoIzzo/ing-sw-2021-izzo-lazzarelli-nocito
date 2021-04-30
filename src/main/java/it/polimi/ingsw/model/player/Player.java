package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.card.*;

import java.util.*;

/**
 * Player class represents a player in the game.
 *
 * @author Gabriele Lazzarelli, Riccardo Izzo
 */
public class Player {
    private String nickname;
    private Game game;
    private Dashboard myDashboard;
    private Set<DevelopmentCard> developments;
    private Set<LeaderCard> leaders;
    private ResourceMap totalResources;
    private CardMap numberOfCard;
    private CardMap levelOfCard;
    private ArrayList<Card> availableProduction;
    private Set<MarbleColor> availableExchange;
    private ResourceMap availableDiscount;

    /**
     * Constructor Player creates a new Player instance.
     * @param name player nickname.
     */
    public Player(String name, boolean singlePlayer, Game game){
        nickname = name;
        this.game = game;
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

    public Set<DevelopmentCard> getDevelopments(){
        return developments;
    }

    public Set<LeaderCard> getLeaders(){
        return leaders;
    }

    public CardMap getNumberOfCard() {
        return numberOfCard;
    }

    public CardMap getLevelOfCard() {
        return levelOfCard;
    }

    /**
     * Method getAvailableProduction gets all the ProductionPower in availableProduction list.
     * @return List<ProductionPower>, list of the productionPower this Player has.
     */
    public List<ProductionPower> getAvailableProduction() {
        List<ProductionPower> productionPowers = new ArrayList<>();
        for (Card card: availableProduction) {
            if (card instanceof DevelopmentCard) {
                productionPowers.add(((DevelopmentCard) card).getProduction());
            } else if (card instanceof ProductionLeaderCard) {
                productionPowers.add(((ProductionLeaderCard) card).getProduction());
            }
        }
        return productionPowers;
    }

    public Dashboard getDashboard() {
        return myDashboard;
    }

    /**
     * Method buyCard is called when the player buys a DevelopmentCard.
     * The new DevelopmentCard will be added to availableProduction and placed in:
     * <ul>
     * <li>a free slot if the DevelopmentCard's level is I</li>
     * <li>the slot of a DevelopmentCard of the same type but lower level</li>
     * </ul>
     * @param row is the row line in the grid of developmentCard
     * @param column is the column in the grid of developmentCard
     */
    public void buyCard(int row, int column){
        DevelopmentCard developmentCard = (DevelopmentCard) game.getGrid()[row][column].draw();
        int indexPosition = availableProduction.size();

        for(Card card: availableProduction){
            if (card instanceof DevelopmentCard){
                if (((DevelopmentCard) card).getType() == developmentCard.getType() && ((DevelopmentCard) card).getLevel()+1 == developmentCard.getLevel()){
                    indexPosition = availableProduction.indexOf(card);
                    availableProduction.remove(card);
                }
            }
        }
        availableProduction.add(indexPosition, developmentCard);
        developments.add(developmentCard);
        numberOfCard.addCard(developmentCard.getType(), 1);
        if (levelOfCard.getCard(developmentCard.getType()) < developmentCard.getLevel()) {
            levelOfCard.put(developmentCard.getType(), developmentCard.getLevel());
        }
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
    public void discardLeaderCard(LeaderCard card){
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
        for(Card card : developments){
            victoryPoints += card.getVictoryPoints();
        }
        // VPs for each Leader Card
        for(LeaderCard card : leaders){
            if(card.isActive()) victoryPoints += card.getVictoryPoints();
        }
        for(Resource resource : Resource.values()){
            numResources += totalResources.getResource(resource);
        }
        // VPs for every set of 5 resources of any type + VPs depending on the final position on the faith track + VPs based on the Pope's favor tiles
        victoryPoints += (numResources % 5) + myDashboard.getPath().getPosVictoryPoints() + myDashboard.getPath().getPointsForTiles();
        return victoryPoints;
    }

}
