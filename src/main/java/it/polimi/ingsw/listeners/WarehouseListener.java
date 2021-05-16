package it.polimi.ingsw.listeners;

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
        if (evt.getPropertyName().equals(SHELF_CHANGE)) { //see shelf listener
            //new message
            //send message
        } else if (evt.getPropertyName().equals(TEMPORARY_SHELF)) {
            //new message
            //send message
        }
    }
}
