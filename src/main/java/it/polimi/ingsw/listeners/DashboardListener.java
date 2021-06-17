package it.polimi.ingsw.listeners;

import it.polimi.ingsw.events.servermessages.ServerMessage;
import it.polimi.ingsw.events.servermessages.UpdateView;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.constants.PlayerConstants.STRONGBOX_CHANGE;

/**
 * Class DashboardListener handles the updates regarding the attributes' changes in the Dashboard.
 * @author Gabriele Lazzarelli
 */
public class DashboardListener extends PropertyListener {
    /**
     * Constructor DashboardListener takes a VirtualView as a parameter.
     * @param virtualView the VirtualView used to forward messages to the players.
     */
    public DashboardListener(VirtualView virtualView) {
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

        if (STRONGBOX_CHANGE.equals(propertyName)) {
            serverMessage = new UpdateView(playerSource, propertyName, oldValue, newValue);
            virtualView.sendToEveryone(serverMessage);
        }
    }
}