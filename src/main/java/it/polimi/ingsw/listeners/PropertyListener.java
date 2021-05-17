package it.polimi.ingsw.listeners;

import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeListener;

/**
 * Class Listener is implemented by all the listeners which observe bound properties changes.
 * @author Gabriele Lazzarelli
 */
public abstract class PropertyListener implements PropertyChangeListener {
    final VirtualView virtualView;

    protected PropertyListener(VirtualView virtualView) {
        this.virtualView = virtualView;
    }
}
