package it.polimi.ingsw.listeners;

import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.constants.PlayerConstants.*;


public class FaithTrackListener extends PropertyListener {
    public FaithTrackListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(FAITH_MARKER_POSITION)) {
            //new message
            //send message
            if (((Integer) evt.getNewValue()) == END_TILE) {
                //new message
                //send message
            }
        } else if (evt.getPropertyName().equals(BLACK_MARKER_POSITION)){
            //new message
            //send message
            if (((Integer) evt.getNewValue()) == END_TILE) {
                //new message
                //send message
            }
        }
    }
}
