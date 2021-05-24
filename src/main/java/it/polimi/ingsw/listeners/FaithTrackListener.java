package it.polimi.ingsw.listeners;

import it.polimi.ingsw.events.servermessages.EndGame;
import it.polimi.ingsw.events.servermessages.ServerMessage;
import it.polimi.ingsw.events.servermessages.UpdateView;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.constants.PlayerConstants.*;


public class FaithTrackListener extends PropertyListener {
    public FaithTrackListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ServerMessage serverMessage;
        String playerSource = (String) evt.getSource();
        String propertyName = evt.getPropertyName();
        Object oldValue = evt.getOldValue();
        Object newValue = evt.getNewValue();

        if (FAITH_MARKER_POSITION.equals(propertyName) || BLACK_MARKER_POSITION.equals(propertyName)) {
            serverMessage = new UpdateView(playerSource, propertyName, oldValue, newValue);
            virtualView.sendToEveryone(serverMessage);
            if (((Integer) evt.getNewValue()) == END_TILE) {
                ServerMessage endGame = new EndGame();
                virtualView.sendToEveryone(endGame);
            }
        } else if (POPES_TILES_CHANGE.equals(propertyName)) {
            serverMessage = new UpdateView(playerSource, propertyName, oldValue, newValue);
            virtualView.sendToEveryone(serverMessage);
        }
    }
}
