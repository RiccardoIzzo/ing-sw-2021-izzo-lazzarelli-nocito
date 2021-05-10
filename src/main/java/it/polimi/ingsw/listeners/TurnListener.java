package it.polimi.ingsw.listeners;

import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeEvent;

public class TurnListener extends Listener {
    public TurnListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }
}

