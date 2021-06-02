package it.polimi.ingsw.model;

import java.beans.PropertyChangeSupport;
import it.polimi.ingsw.listeners.GameListener;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.VirtualView;

import java.util.*;

import static it.polimi.ingsw.constants.PlayerConstants.GRID_CHANGE;

/**
 * Game class contains the main logic of "Master of Renaissance".
 *
 * @author Riccardo Izzo
 */
public abstract class Game {
    private final ArrayList<Player> players;
    private final Market market;
    private final Deck[][] grid;
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Game constructor creates a new Game instance.
     */
    public Game(){
        players = new ArrayList<>();
        market = new Market();
        grid = new Deck[3][4];
    }

    /**
     * Method getPlayers returns the list of players.
     * @return a list of players.
     */
    public ArrayList<Player> getPlayers(){
        return players;
    }

    /**
     * Method getGrid returns the grid of the development cards.
     * @return the grid.
     */
    public Deck[][] getGrid() {
        return grid;
    }

    /**
     * Method getNumPlayers returns the number of players.
     * @return the number of players.
     */
    public int getNumPlayers(){
        return players.size();
    }

    /**
     * Method addPlayer creates a player and adds it to the list of players.
     * @param name name of the player to be added.
     */
    public void addPlayer(String name){
        players.add(new Player(name, this));
    }

    /**
     * Method removePlayer removes a player from the list.
     * @param name name of the player to be removed.
     */
    public void removePlayer(String name){
        players.remove(getPlayerByName(name));
    }

    /**
     * Method getPlayerByName returns a player instance relying on his name.
     * @param name player name.
     * @return an instance of player.
     */
    public Player getPlayerByName(String name){
        for(Player player : players){
            if(player.getNickname().equals(name)){
                return player;
            }
        }
        return null;
    }
    public Market getMarket() {
        return market;
    }

    /**
     * Method getDeck returns the deck of development cards at the specified indexes.
     * @param row row index.
     * @param col column index.
     * @return the deck.
     */
    public Deck getDeck(int row, int col){
        return grid[row][col];
    }

    /**
     * Method generateGrid gets a list of development cards from JsonCardsCreator and creates the grid with the 48 development cards.
     */
    public void generateGrid(){
        ArrayList<DevelopmentCard> cards = JsonCardsCreator.generateDevelopmentCards();

        for(int j = 0; j < 4; j++){
            for(int i = 0; i < 3; i++){
                grid[i][j] = new Deck(cards.subList(0, 4));
                cards.removeAll(cards.subList(0, 4));
            }
        }
        pcs.firePropertyChange(GRID_CHANGE, null, grid);
    }

    /**
     * Method generateLeaders gets a list of leader cards from JsonCardsCreator, shuffles them and distributes them among the players.
     */
    public void generateLeaders(){
        List<LeaderCard> leaders = JsonCardsCreator.generateLeaderCards();

        Collections.shuffle(leaders);
        for(Player player : getPlayers()){
            player.setLeaders(leaders.subList(0, 4));
            leaders.removeAll(leaders.subList(0, 4));
        }
    }

    public void addPropertyListener(VirtualView virtualView){
        pcs.addPropertyChangeListener(new GameListener(virtualView));
    }
}
