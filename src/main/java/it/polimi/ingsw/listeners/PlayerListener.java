package it.polimi.ingsw.listeners;

import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.constants.PlayerConstants.*;

/**
 * Class LeaderCardListener notifies the VirtualView when a LeaderCard is activated.
 * @author Gabriele Lazzarelli
 */
public class PlayerListener extends PropertyListener {
    public PlayerListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case LEADER_ACTIVATION:
                //new message
                //send message
                break;
            case SET_LEADERS:
                //new message
                //send message
                break;
            case SELECT_LEADERS:
                //new message
                //send message
                break;
            case DISCARD_LEADER:
                //new message
                //send message
                break;
            case GRID_CHANGE:
                //new message
                //send message
                break;
        }
    }


}
