package it.polimi.ingsw.listeners;

import it.polimi.ingsw.events.servermessages.ServerMessage;
import it.polimi.ingsw.events.servermessages.UpdateView;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeEvent;
import java.util.HashSet;

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
        String playerSource = (String) evt.getSource();
        String propertyName = evt.getPropertyName();
        Object oldValue = evt.getOldValue();
        Object newValue = evt.getNewValue();

        switch (propertyName) {
            case SET_LEADERS -> {
                serverMessage = new UpdateView(playerSource, propertyName, oldValue, translateLeadersToMap((HashSet<LeaderCard>) newValue));
                virtualView.sendToPlayer(playerSource, serverMessage);
            }
            case SELECT_LEADERS, DISCARD_LEADER, LEADER_ACTIVATION -> {
                serverMessage = new UpdateView(playerSource, propertyName, oldValue, newValue);
                virtualView.sendToEveryone(serverMessage);
            }
            case GRID_CHANGE -> {
                serverMessage = new UpdateView(null, propertyName, oldValue, newValue);
                virtualView.sendToEveryone(serverMessage);
            }
        }
    }
}
