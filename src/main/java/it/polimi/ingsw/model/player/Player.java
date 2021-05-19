package it.polimi.ingsw.model.player;

import it.polimi.ingsw.listeners.PlayerListener;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeSupport;
import java.util.*;

import static it.polimi.ingsw.constants.PlayerConstants.*;

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
    private CardMap numberOfCard;
    private CardMap levelOfCard;
    private ArrayList<Card> availableProduction;
    private Set<MarbleColor> availableExchange;
    private ResourceMap availableDiscount;
    private PropertyChangeSupport pcs;

    /**
     * Constructor Player creates a new Player instance.
     * @param name player nickname.
     */
    public Player(String name, Game game){
        nickname = name;
        this.game = game;
        myDashboard = new Dashboard(game);
        developments = new HashSet<>();
        leaders = new HashSet<>();
        numberOfCard = new CardMap();
        levelOfCard = new CardMap();
        availableProduction = new ArrayList<>();
        availableExchange = new HashSet<>();
        availableDiscount = new ResourceMap();
        pcs = new PropertyChangeSupport(name);
        setPropertyChangeSupport();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Method setLeaders adds a list of LeaderCard to this Player, it is called at the start of the game.
     * @param cards the cards
     */
    public void setLeaders(List<LeaderCard> cards){
        leaders.addAll(cards);
        pcs.firePropertyChange(SET_LEADERS, null, cards);
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
        Deck[][] OldGrid = game.getGrid();
        DevelopmentCard developmentCard = game.getGrid()[row][column].draw();
        pcs.firePropertyChange(GRID_CHANGE, OldGrid, game.getGrid());
        addDevelopmentCard(developmentCard);
    }

    /**
     * Method addDevelopmentCard adds a DevelopmentCard to this Player and updates its attributes, in particular:
     * <ul>
     *     <li>availableProduction: places the given DevelopmentCard in the correct slot</li>
     *     <li>developments: adds the given DevelopmentCard to the list</li>
     *     <li>numberOfCard: updates the CardMap</li>
     *     <li>levelOfCard: updates the CardMap</li>
     * </ul>
     * @param developmentCard the DevelopmentCard to add
     */
    public void addDevelopmentCard(DevelopmentCard developmentCard){
        int indexSlotPlace = availableProduction.size();

        for(Card card: availableProduction){
            if (card instanceof DevelopmentCard){
                if (((DevelopmentCard) card).getType() == developmentCard.getType() && ((DevelopmentCard) card).getLevel()+1 == developmentCard.getLevel()){
                    indexSlotPlace = availableProduction.indexOf(card);
                    availableProduction.remove(card);
                    break;
                }
            }
        }
        availableProduction.add(indexSlotPlace, developmentCard);
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
        ResourceMap totalResources = new ResourceMap();
        totalResources.addResources(myDashboard.getStrongbox());
        totalResources.addResources(myDashboard.getWarehouse().getResourcesFromWarehouse());
        return totalResources;
    }

    /**
     * Method takeResourcesFromMarket takes resources from the market by letting slide the marble in the selected index.
     * If by performing this action a red marble is found, the player moves forward on the faith track.
     * The resources obtained are finally added to the temporary shelf where they can be managed by the player.
     * @param pos row/column index.
     * @param type represent the user choice: 1 = row, 2 = column.
     */
    public void takeResourcesFromMarket(int pos, int type){
        game.getMarket().insertMarble(pos, type);
        if(game.getMarket().findFaith()) myDashboard.incrementFaith(1);
        myDashboard.getWarehouse().addResourcesToShelf(TEMPORARY_SHELF, game.getMarket().resourceOutput());
    }

    /**
     * Method activateProduction gets the ProductionPower of the chosen DevelopmentCard and increment the resources
     * in the strongbox and the faith in the FaithTrack by the output of the ProductionPower
     * @param developmentCard : the Card which productionPower is to activate
     */
    public void activateProduction(DevelopmentCard developmentCard){
        myDashboard.removeResourcesFromDashboard(developmentCard.getProduction().getInputResource());
        myDashboard.addResourcesToStrongbox(developmentCard.getProduction().getOutputResource());
        myDashboard.incrementFaith(developmentCard.getProduction().getOutputFaith());
    }

    /**
     * Method selectLeaderCard selects two leader cards among the four available at the start of the game and deletes the others.
     * @param firstCard first leader card chosen by the player.
     * @param secondCard second leader card chosen by the player.
     */
    public void selectLeaderCard(Card firstCard, Card secondCard){
        Set<LeaderCard> OldLeaders = leaders;
        leaders.removeIf(leader -> !(leader.equals(firstCard)) && !(leader.equals(secondCard)));
        pcs.firePropertyChange(SELECT_LEADERS, OldLeaders, this.leaders);
    }

    /**
     * Method activateLeaderCard activates a leader card.
     * @param leaderCard the leader card that must be activated.
     */
    public void activateLeaderCard(LeaderCard leaderCard){
        leaderCard.setActive(true);
        if (leaderCard instanceof ProductionLeaderCard){
            availableProduction.add(leaderCard);
        } else if (leaderCard instanceof WhiteMarbleLeaderCard){
            availableExchange.addAll(((WhiteMarbleLeaderCard) leaderCard).getExchange());
        } else if (leaderCard instanceof DiscountLeaderCard){
            availableDiscount.addResources(((DiscountLeaderCard) leaderCard).getDiscount());
        } else if (leaderCard instanceof ExtraShelfLeaderCard){
            myDashboard.getWarehouse().addExtraShelfResource(((ExtraShelfLeaderCard) leaderCard).getResource());
        }
        pcs.firePropertyChange(LEADER_ACTIVATION, leaderCard.getCardID(), leaderCard.getCardID());
    }

    /**
     * Method discardLeaderCard discards the selected leader card and increments the player position on the faith track.
     * @param card the leader card that must be discarded.
     */
    public void discardLeaderCard(LeaderCard card){
        Set<LeaderCard> oldLeaders = this.leaders;
        leaders.remove(card);
        myDashboard.incrementFaith(1);
        pcs.firePropertyChange(DISCARD_LEADER, oldLeaders, this.leaders);
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
            numResources += getTotalResources().getResource(resource);
        }
        // VPs for every set of 5 resources of any type + VPs depending on the final position on the faith track + VPs based on the Pope's favor tiles
        victoryPoints += (numResources % 5) + myDashboard.getFaithTrack().getPosVictoryPoints() + myDashboard.getFaithTrack().getPointsForTiles();
        return victoryPoints;
    }

    public void setPropertyChangeSupport() {
        myDashboard.setPropertyChangeSupport(this.pcs);
    }

    public void addPropertyListener(VirtualView virtualView) {
        pcs.addPropertyChangeListener(new PlayerListener(virtualView));
        myDashboard.addPropertyListener(virtualView);
    }
}
