package it.polimi.ingsw.listeners;

import it.polimi.ingsw.events.servermessages.ServerMessage;
import it.polimi.ingsw.events.servermessages.UpdateView;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.constants.PlayerConstants.SHELF_CHANGE;
import static it.polimi.ingsw.constants.PlayerConstants.TEMPORARY_SHELF_CHANGE;

public class WarehouseListener extends PropertyListener{
    public WarehouseListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ServerMessage serverMessage;
        String playerSource = (String) evt.getSource();
        String propertyName = evt.getPropertyName();
        Object oldValue = evt.getOldValue();
        Object newValue = evt.getNewValue();

        if (SHELF_CHANGE.equals(propertyName) || TEMPORARY_SHELF_CHANGE.equals(propertyName)) {
            serverMessage = new UpdateView(playerSource, propertyName, oldValue, newValue);
            virtualView.sendToPlayer(playerSource, serverMessage);
        }
    }
}
