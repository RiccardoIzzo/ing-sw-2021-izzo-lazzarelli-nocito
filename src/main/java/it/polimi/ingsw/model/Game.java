package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 * Game class contains the main logic of "Master of Renaissance".
 *
 * @author Riccardo Izzo
 */
public class Game {
    private ArrayList<Player> players;
    private Market market;
    private Deck[][] grid;

    /**
     * Game constructor creates a new Game instance.
     */
    public Game(){
        players = new ArrayList<>();
        market = new Market();
        grid = new Deck[3][4];
    }

    /**
     * Method getPlayers return the list of players.
     * @return a list of players.
     */
    public ArrayList<Player> getPlayers(){
        return players;
    }

    /**
     * Method getNumPlayers returns the number of players.
     * @return the number of players.
     */
    public int getNumPlayers(){
        return players.size();
    }

    /**
     * Method addPlayer create a nem player and adds it to the list of player.
     * @param name name of the player to be added.
     */
    public void addPlayer(String name){
        players.add(new Player(name));
    }

    /**
     * Method removePlayer remove a player from the list.
     * @param name name of the player to be removed.
     */
    public void removePlayer(String name){
        players.remove(getPlayerByName(name));
    }

    /**
     * Method getPlayerByName return a player instance relying on his name.
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

    public void startGame(){

    }

    public void generateGrid(){

    }

    public void generateLeaders(){

    }
}
