package it.polimi.ingsw.listeners;

import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.constants.PlayerConstants.STRONGBOX_CHANGE;

public class DashboardListener extends PropertyListener {
    public DashboardListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(STRONGBOX_CHANGE)) {
            //new message
            //send message
        }
    }
}