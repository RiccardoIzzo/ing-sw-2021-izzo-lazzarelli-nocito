package it.polimi.ingsw.listeners;

import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeListener;

public abstract class Listener implements PropertyChangeListener {
    private final VirtualView virtualView;

    protected Listener(VirtualView virtualView) {
        this.virtualView = virtualView;
    }
}
