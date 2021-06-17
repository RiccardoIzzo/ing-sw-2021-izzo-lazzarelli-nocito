package it.polimi.ingsw.listeners;

import it.polimi.ingsw.events.servermessages.Defeat;
import it.polimi.ingsw.events.servermessages.EndGame;
import it.polimi.ingsw.events.servermessages.ServerMessage;
import it.polimi.ingsw.events.servermessages.UpdateView;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.constants.PlayerConstants.*;

/**
 * Class FaithTrackListener handles the updates regarding the attributes' changes in the FaithTrack.
 * @author Gabriele Lazzarelli
 */
public class FaithTrackListener extends PropertyListener {
    /**
     * Constructor FaithTrackListener takes a VirtualView as a parameter.
     * @param virtualView the VirtualView used to forward messages to the players.
     */
    public FaithTrackListener(VirtualView virtualView) {
        super(virtualView);
    }

    /**
     * Method propertyChange takes an event as parameter and send a message of update using the event values.
     * @param evt the PropertyChangeEvent to handle.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ServerMessage serverMessage;
        String playerSource = (String) evt.getSource();
        String propertyName = evt.getPropertyName();
        Object oldValue = evt.getOldValue();
        Object newValue = evt.getNewValue();

        if (BLACK_MARKER_POSITION.equals(propertyName)) {
            serverMessage = new UpdateView(playerSource, propertyName, oldValue, newValue);
            virtualView.sendToEveryone(serverMessage);
            if (((Integer) evt.getNewValue()) == END_TILE) {
                ServerMessage message = new Defeat();
                virtualView.sendToPlayer(playerSource, message);
            }
        } else if (FAITH_MARKER_POSITION.equals(propertyName)) {
            serverMessage = new UpdateView(playerSource, propertyName, oldValue, newValue);
            virtualView.sendToEveryone(serverMessage);
            if (((Integer) evt.getNewValue()) == END_TILE) {
                ServerMessage endGame = new EndGame();
                virtualView.sendToEveryone(endGame);
            }
        } else {
            if (TILES_UNCOVERED_CHANGE.equals(propertyName)) {
                serverMessage = new UpdateView(null, propertyName, oldValue, newValue);
                virtualView.sendToEveryone(serverMessage);
            } else if (POPES_TILES_CHANGE.equals(propertyName)) {
                serverMessage = new UpdateView(playerSource, propertyName, oldValue, newValue);
                virtualView.sendToEveryone(serverMessage);
            }
        }
    }
}
