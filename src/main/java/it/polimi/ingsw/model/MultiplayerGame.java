package it.polimi.ingsw.model;

import it.polimi.ingsw.listeners.LeaderCardListener;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeSupport;
import java.util.Random;

/**
 * MultiplayerGame class extends Game class and implements the logic of a multiplayer match up to four players.
 *
 * @author Riccardo Izzo
 */
public class MultiplayerGame extends Game {
    private Player currPlayer;
    private Player firstPlayer;
    private int playerIndex;

    private String END_TURN = "endTurn"; //property name
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Constructor MultiplayerGame creates a new MultiplayerGame instance.
     */
    public MultiplayerGame(){
        super();
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
        setCurrPlayer(super.getPlayers().get(playerIndex));
        firstPlayer = currPlayer;
    }

    /**
     * Method nextPlayer updates the current player after the turn.
     */
    public void nextPlayer(){
        String previousPlayer = currPlayer.getNickname();
        if(playerIndex == super.getNumPlayers() - 1){
            playerIndex = 0;
            setCurrPlayer(super.getPlayers().get(0));
        }
        else{
            playerIndex++;
            setCurrPlayer(super.getPlayers().get(playerIndex));
        }
//        pcs.firePropertyChange(END_TURN, previousPlayer, currPlayer.getNickname());

    }

    public void addListener(VirtualView virtualView){
        pcs.addPropertyChangeListener(END_TURN, new LeaderCardListener(virtualView));
    }
}
