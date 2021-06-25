package it.polimi.ingsw.model.player;

import it.polimi.ingsw.listeners.PlayerListener;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.constants.PlayerConstants.*;

/**
 * Player class represents a player in the game.
 *
 * @author Gabriele Lazzarelli, Riccardo Izzo
 */
public class Player {
    private String nickname;
    private final Game game;
    private final Dashboard myDashboard;
    private final Set<DevelopmentCard> developments;
    private final Set<LeaderCard> leaders;
    private final CardMap numberOfCard;
    private final CardMap levelOfCard;
    private final ArrayList<DevelopmentCard> activeDevelopments;
    private final PropertyChangeSupport pcs;

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
        activeDevelopments = new ArrayList<>(Collections.nCopies(3,null));
        pcs = new PropertyChangeSupport(name);
        setPropertyChangeSupport();
    }

    /**
     * Method getNickname returns the player's nickname.
     * @return the player's nickname.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Method setNickname sets the player's nickname.
     * @param nickname the player's nickname.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Method getLeaders returns the set of leader cards.
     * @return the set of leader cards
     */
    public Set<LeaderCard> getLeaders(){
        return leaders;
    }

    /**
     * Method setLeaders adds a list of LeaderCard to this Player, it is called at the start of the game.
     * @param cards a list of leader cards.
     */
    public void setLeaders(List<LeaderCard> cards){
        leaders.addAll(cards);
        pcs.firePropertyChange(SET_LEADERS, null, this.leaders);
    }

    /**
     * Method getDevelopments returns the set of development cards.
     * @return the set of development cards.
     */
    public Set<DevelopmentCard> getDevelopments(){
        return developments;
    }

    /**
     * Method getNumberOfCard returns a CardMap that associates the card color with his occurrences.
     * @return the CardMap.
     */
    public CardMap getNumberOfCard() {
        return numberOfCard;
    }

    /**
     * Method getLevelOfCard returns a CardMap that associates the card level with his occurrences.
     * @return the CardMap.
     */
    public CardMap getLevelOfCard() {
        return levelOfCard;
    }

    /**
     * Method getActiveDevelopments gets all the Card(s) in activeDevelopments list.
     * @return ArrayList<DevelopmentCard>, list of the Cards with ProductionPower this Player has.
     */
    public ArrayList<DevelopmentCard> getActiveDevelopments() {
        return activeDevelopments;
    }

    /**
     * Method getAvailableProductions gets all the Card(s) with an active ProductionPower.
     * @return ArrayList<Card>, list of the Cards (Development and Leader) with an active ProductionPower this Player has.
     */
    public ArrayList<Card> getAvailableProduction() {
        ArrayList<Card> availableProduction = new ArrayList<>();
        availableProduction.addAll(activeDevelopments.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        availableProduction.addAll(leaders.stream()
                .filter(leaderCard -> leaderCard instanceof ProductionLeaderCard)
                .filter(LeaderCard::isActive)
                .collect(Collectors.toList()));
        return availableProduction;
    }

    /**
     * Method getDashboard returns the dashboard associated to this player.
     * @return the dashboard.
     */
    public Dashboard getDashboard() {
        return myDashboard;
    }

    /**
     * Method buyCard is called when the player buys a DevelopmentCard.
     * The new DevelopmentCard will be added to activeDevelopments and placed in:
     * <ul>
     *     <li>a free slot if the DevelopmentCard's level is I</li>
     *     <li>the slot of a DevelopmentCard of the same type but lower level</li>
     * </ul>
     * @param row is the row line in the grid of developmentCard.
     * @param column is the column in the grid of developmentCard.
     * @param index slot index in the dashboard.
     */
    public void buyCard(int row, int column, int index){
        DevelopmentCard developmentCard = game.getGrid()[row][column].draw();
        ResourceMap resourcesToRemove = ((ResourceRequirement) developmentCard.getRequirement()).getResources();
        List<DiscountLeaderCard> activeDiscount = leaders.stream()
                .filter(leader -> leader instanceof DiscountLeaderCard)
                .filter(LeaderCard::isActive)
                .map(leader -> (DiscountLeaderCard) leader)
                .collect(Collectors.toList());
        //Apply available discount
        for (DiscountLeaderCard leader : activeDiscount){
            resourcesToRemove.removeResources(leader.getDiscount());
        }
        myDashboard.removeResourcesFromDashboard(resourcesToRemove);
        activeDevelopments.set(index - 1, developmentCard);
        pcs.firePropertyChange(ACTIVE_DEVELOPMENTS_CHANGE, null, activeDevelopments);
        pcs.firePropertyChange(GRID_CHANGE, null, game.getGrid());
        addDevelopmentCard(developmentCard);
    }

    /**
     * Method addDevelopmentCard adds a DevelopmentCard to this Player and updates its attributes, in particular:
     * <ul>
     *     <li>activeDevelopments: places the given DevelopmentCard in the correct slot</li>
     *     <li>developments: adds the given DevelopmentCard to the list</li>
     *     <li>numberOfCard: updates the CardMap</li>
     *     <li>levelOfCard: updates the CardMap</li>
     * </ul>
     * @param developmentCard the DevelopmentCard to add.
     */
    public void addDevelopmentCard(DevelopmentCard developmentCard){
        developments.add(developmentCard);
        numberOfCard.addCard(developmentCard.getType(), 1);
        if (levelOfCard.getCard(developmentCard.getType()) < developmentCard.getLevel()) {
            levelOfCard.put(developmentCard.getType(), developmentCard.getLevel());
        }
        pcs.firePropertyChange(DEVELOPMENTS_CHANGE, null, developments);
    }

    /**
     * Method getTotalResources returns the sum of resources in both the warehouse and the strongbox.
     * @return the available resources of this Player.
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
    public void takeResourcesFromMarket(int pos, int type, MarbleColor whiteMarbleExchange){
        game.getMarket().setSpecialMarble(whiteMarbleExchange);
        game.getMarket().insertMarble(pos, type);
        if(game.getMarket().findFaith()){
            if(myDashboard.incrementFaith(1)) game.vaticanReport();
        }
        myDashboard.getWarehouse().addResourcesToShelf(TEMPORARY_SHELF, game.getMarket().resourceOutput());
        game.getMarket().reset();
    }

    /**
     * Method activateProduction gets the ProductionPower of the chosen Card and increment the resources
     * in the strongbox and the faith in the FaithTrack by the output of the ProductionPower.
     * @param productionCard the Card which productionPower is activated.
     */
    public void activateProduction(Card productionCard){
        if (productionCard instanceof DevelopmentCard) {
            myDashboard.removeResourcesFromDashboard(((DevelopmentCard) productionCard).getProduction().getInputResource());
            myDashboard.addResourcesToStrongbox(((DevelopmentCard) productionCard).getProduction().getOutputResource());
            if(myDashboard.incrementFaith(((DevelopmentCard) productionCard).getProduction().getOutputFaith())) game.vaticanReport();
        } else if (productionCard instanceof ProductionLeaderCard) {
            myDashboard.removeResourcesFromDashboard(((ProductionLeaderCard) productionCard).getProduction().getInputResource());
            myDashboard.addResourcesToStrongbox(((ProductionLeaderCard) productionCard).getProduction().getOutputResource());
            if(myDashboard.incrementFaith(((ProductionLeaderCard) productionCard).getProduction().getOutputFaith())) game.vaticanReport();
        }
    }

    /**
     * Method activateProduction gets two parameters the input and output resources of the production.
     * @param inputResources the production's input resources.
     * @param outputResource the production's output resources     .
     */
    public void activateProduction(ResourceMap inputResources, ResourceMap outputResource){
        myDashboard.removeResourcesFromDashboard(inputResources);
        myDashboard.addResourcesToStrongbox(outputResource);
    }


    /**
     * Method activateLeaderCard activates a leader card.
     * @param leaderCard the leader card that must be activated.
     */
    public void activateLeaderCard(LeaderCard leaderCard){
        leaderCard.setActive(true);
        if (leaderCard instanceof ExtraShelfLeaderCard){
            myDashboard.getWarehouse().addExtraShelfResource(((ExtraShelfLeaderCard) leaderCard).getResource());
        }
        pcs.firePropertyChange(LEADER_ACTIVATION, null, leaders);
    }

    /**
     * Method discardLeaderCard discards the selected leader card and increments the player position on the faith track.
     * @param cardID the leader card that must be discarded.
     */
    public void discardLeaderCard(int cardID){
        leaders.removeIf(leaderCard -> leaderCard.getCardID() == cardID);
        if(getDashboard().incrementFaith(1)) game.vaticanReport();
        pcs.firePropertyChange(DISCARD_LEADER, null, this.leaders);
    }

    /**
     * Method removeLeaderCard removes the selected leader cards.
     * @param cardIDs the leaders to remove.
     */
    public void removeLeaders(ArrayList<Integer> cardIDs){
        for (int cardID : cardIDs) {
            leaders.removeIf(leaderCard -> leaderCard.getCardID() == cardID);
        }
        pcs.firePropertyChange(DISCARD_LEADER, null, this.leaders);
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
        numResources += getTotalResources().getAmount();
        // VPs for every set of 5 resources of any type + VPs depending on the final position on the faith track + VPs based on the Pope's favor tiles
        victoryPoints += (numResources / 5) + myDashboard.getFaithTrack().getPosVictoryPoints() + myDashboard.getFaithTrack().getPointsForTiles();
        return victoryPoints;
    }

    /**
     * Method setPropertyListener calls the setPropertyChangeSupport method of myDashboard.
     */
    public void setPropertyChangeSupport() {
        myDashboard.setPropertyChangeSupport(this.pcs);
    }

    /**
     * Method addPropertyListener register a PlayerListener to the PropertyChangeSupport of this class.
     * @param virtualView the VirtualView used to forward messages to the players.
     */
    public void addPropertyListener(VirtualView virtualView) {
        PlayerListener playerListener = new PlayerListener(virtualView, game);
        pcs.addPropertyChangeListener(SET_LEADERS, playerListener);
        pcs.addPropertyChangeListener(DISCARD_LEADER, playerListener);
        pcs.addPropertyChangeListener(LEADER_ACTIVATION, playerListener);
        pcs.addPropertyChangeListener(GRID_CHANGE, playerListener);
        pcs.addPropertyChangeListener(DEVELOPMENTS_CHANGE, playerListener);
        pcs.addPropertyChangeListener(ACTIVE_DEVELOPMENTS_CHANGE, playerListener);
        myDashboard.addPropertyListener(virtualView);
    }
}
