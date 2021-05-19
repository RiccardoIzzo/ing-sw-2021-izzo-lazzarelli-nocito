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
        switch (evt.getPropertyName()) {
            case SHELF_CHANGE, TEMPORARY_SHELF_CHANGE -> {
                serverMessage = new UpdateView((String) evt.getSource(), evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
                virtualView.sendToPlayer((String) evt.getSource(), serverMessage);
            }
        }
    }
}
