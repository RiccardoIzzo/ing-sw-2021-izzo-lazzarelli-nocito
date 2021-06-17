package it.polimi.ingsw.listeners;

import it.polimi.ingsw.events.servermessages.ServerMessage;
import it.polimi.ingsw.events.servermessages.UpdateView;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.constants.PlayerConstants.*;

/**
 * Class WarehouseListener handles the updates regarding the attributes' changes in Warehouse.
 * @author Gabriele Lazzarelli
 */
public class WarehouseListener extends PropertyListener{
    /**
     * Constructor WarehouseListener takes a VirtualView as a parameter.
     * @param virtualView the VirtualView used to forward messages to the players.
     */
    public WarehouseListener(VirtualView virtualView) {
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
        Object newValue = evt.getNewValue();

        if (SHELF_CHANGE.equals(propertyName) || EXTRA_SHELF_CHANGE.equals(propertyName)) {
            serverMessage = new UpdateView(playerSource, propertyName, null, newValue);
            virtualView.sendToEveryone(serverMessage);
        } else if (TEMPORARY_SHELF_CHANGE.equals(propertyName)) {
            serverMessage = new UpdateView(playerSource, propertyName, null, newValue);
            virtualView.sendToPlayer(playerSource, serverMessage);
        }
    }
}
