package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;

import java.util.Collections;

import static it.polimi.ingsw.constants.GameConstants.END_TURN;

/**
 * MultiplayerGame class extends Game class and implements the logic of a multiplayer match up to four players.
 *
 * @author Riccardo Izzo
 */
public class MultiplayerGame extends Game {
    private Player currPlayer;
    private Player firstPlayer;

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
        Collections.shuffle(getPlayers());
        currPlayer = getPlayers().get(0);
        firstPlayer = currPlayer;
        pcs.firePropertyChange(END_TURN, null, currPlayer.getNickname());
    }

    /**
     * Method nextPlayer updates the current player after the turn.
     */
    public void nextPlayer(){
        int indexCurrPlayer = getPlayers().indexOf(currPlayer);
        currPlayer = getPlayers().get((indexCurrPlayer + 1) % getPlayers().size());
        pcs.firePropertyChange(END_TURN, null, currPlayer.getNickname());
    }
}
