package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;

/**
 * MultiplayerGame class extends Game class and implements the logic of a multiplayer match up to four players.
 *
 * @author Riccardo Izzo
 */
public class MultiplayerGame extends Game{
    private ArrayList<Player> players;
    private Player currPlayer;
    private Player firstPlayer;
    private int playerIndex;

    /**
     * Constructor MultiplayerGame creates a new MultiplayerGame instance.
     */
    public MultiplayerGame(){
        super();
        players = new ArrayList<>();
    }

    /**
     * Method getCurrPlayer returns the current player.
     * @return the current player.
     */
    public Player getCurrPlayer(){
        return currPlayer;
    }

    /**
     * Method setCurrPlayer sets the current player.
     * @param player the new current player to be set.
     */
    public void setCurrPlayer(Player player){
        this.currPlayer = player;
    }

    /**
     * Method getFirstPlayer return the first player, the one with the inkwell.
     * @return the first player.
     */
    public Player getFirstPlayer(){
        return firstPlayer;
    }

    /**
     * Method setFirstPlayer sets the first player at the beginning of the game.
     * The first player is randomly chosen from the list of players.
     */
    public void setFirstPlayer(){
        playerIndex = new Random().nextInt(getNumPlayers());
        setCurrPlayer(players.get(playerIndex));
        firstPlayer = currPlayer;
    }

    /**
     * Method getNumPlayers returns the number of players.
     * @return the number of players.
     */
    public int getNumPlayers(){
        return players.size();
    }

    /**
     * Method getPlayers return the list of players.
     * @return a list of players.
     */
    public ArrayList<Player> getPlayers(){
        return players;
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
     * Method nextPlayer updates the current player after the turn.
     */
    public void nextPlayer(){
        if(playerIndex == players.size() - 1){
            playerIndex = 0;
            setCurrPlayer(players.get(0));
        }
        else{
            playerIndex++;
            setCurrPlayer(players.get(playerIndex));
        }
    }
}
