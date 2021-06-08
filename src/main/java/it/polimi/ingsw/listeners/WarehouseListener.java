package it.polimi.ingsw.listeners;

import it.polimi.ingsw.events.servermessages.ServerMessage;
import it.polimi.ingsw.events.servermessages.UpdateView;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.constants.PlayerConstants.*;

public class WarehouseListener extends PropertyListener{
    public WarehouseListener(VirtualView virtualView) {
        super(virtualView);
    }

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
