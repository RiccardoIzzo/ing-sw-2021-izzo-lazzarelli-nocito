package it.polimi.ingsw.listeners;

import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.constants.PlayerConstants.SHELF_CHANGE;

public class ShelfListener extends PropertyListener {
    public ShelfListener(VirtualView virtualView) {
        super(virtualView);
    }

    //evt OldValue is a ResourceMap, the Shelf resources before the change
    //    NewValue is a ResourceMap, the Shelf resources after the change

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(SHELF_CHANGE)) {
            //new message
            //send message
        }
    }
}
