package it.polimi.ingsw.listeners;

import it.polimi.ingsw.events.servermessages.ServerMessage;
import it.polimi.ingsw.events.servermessages.UpdateView;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.constants.PlayerConstants.SHELF_CHANGE;
import static it.polimi.ingsw.constants.PlayerConstants.TEMPORARY_SHELF;

public class WarehouseListener extends PropertyListener{
    public WarehouseListener(VirtualView virtualView) {
        super(virtualView);
    }

    //Both SHELF_CHANGE and TEMPORARY_SHELF:
    //evt OldValue is a ResourceMap, the Shelf resources before the change
    //    NewValue is a ResourceMap, the Shelf resources after the change

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ServerMessage serverMessage;
        if (evt.getPropertyName().equals(SHELF_CHANGE)) {
            serverMessage = new UpdateView((String) evt.getSource(), evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            virtualView.sendToPlayer((String) evt.getSource(), serverMessage);
        } else if (evt.getPropertyName().equals(TEMPORARY_SHELF)) {
            serverMessage = new UpdateView((String) evt.getSource(), evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            virtualView.sendToPlayer((String) evt.getSource(), serverMessage);
        }
    }
}
