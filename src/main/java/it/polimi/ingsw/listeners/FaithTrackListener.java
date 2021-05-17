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
        if (evt.getPropertyName().equals(FAITH_MARKER_POSITION)) {
            ServerMessage serverMessage = new UpdateView((String) evt.getSource(), evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            virtualView.sendToEveryone(serverMessage);
            if (((Integer) evt.getNewValue()) == END_TILE) {
                ServerMessage endGame = new EndGame();
                virtualView.sendToEveryone(endGame);
            }
        } else if (evt.getPropertyName().equals(BLACK_MARKER_POSITION)){
            ServerMessage serverMessage = new UpdateView((String) evt.getSource(), evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            virtualView.sendToEveryone(serverMessage);
            if (((Integer) evt.getNewValue()) == END_TILE) {
                ServerMessage endGame = new UpdateView((String) evt.getSource(), evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
                virtualView.sendToEveryone(endGame);
            }
        }
    }
}
