package it.polimi.ingsw.listeners;

import it.polimi.ingsw.events.servermessages.ServerMessage;
import it.polimi.ingsw.events.servermessages.UpdateView;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.constants.PlayerConstants.*;

/**
 * Class LeaderCardListener notifies the VirtualView when a LeaderCard is activated.
 * @author Gabriele Lazzarelli
 */
public class PlayerListener extends PropertyListener {
    public PlayerListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ServerMessage serverMessage;
        switch (evt.getPropertyName()) {
            case SET_LEADERS -> {
                serverMessage = new UpdateView((String) evt.getSource(), evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
                virtualView.sendToPlayer((String) evt.getSource(), serverMessage);
            }
            case SELECT_LEADERS -> {
                serverMessage = new UpdateView((String) evt.getSource(), evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
                virtualView.sendToPlayer((String) evt.getSource(), serverMessage);
                virtualView.sendToEveryone(serverMessage);
            }
            case DISCARD_LEADER, LEADER_ACTIVATION -> {
                serverMessage = new UpdateView((String) evt.getSource(), evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
                virtualView.sendToEveryone(serverMessage);
            }
            case GRID_CHANGE -> {
                serverMessage = new UpdateView(null, evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
                virtualView.sendToEveryone(serverMessage);
            }
        }
    }
}
