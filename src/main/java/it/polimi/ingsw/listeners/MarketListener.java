package it.polimi.ingsw.listeners;

import it.polimi.ingsw.events.servermessages.ServerMessage;
import it.polimi.ingsw.events.servermessages.UpdateView;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.constants.GameConstants.MARKET_CHANGE;
import static it.polimi.ingsw.constants.GameConstants.SLIDE_MARBLE;

/**
 * Class MarketListener handles the updates regarding the attributes' changes in the Market.
 * @author Gabriele Lazzarelli
 */
public class MarketListener extends PropertyListener {
    /**
     * Constructor MarketListener takes a VirtualView as a parameter.
     * @param virtualView the VirtualView used to forward messages to the players.
     */
    public MarketListener(VirtualView virtualView) {
        super(virtualView);
    }

    /**
     * Method propertyChange takes an event as parameter and send a message of update using the event values.
     * @param evt the PropertyChangeEvent to handle.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ServerMessage serverMessage;
        String propertyName = evt.getPropertyName();
        Object newValue = evt.getNewValue();
        if (MARKET_CHANGE.equals(propertyName)) {
            serverMessage = new UpdateView(null, propertyName, null, newValue);
            virtualView.sendToEveryone(serverMessage);
        } else if (SLIDE_MARBLE.equals(propertyName)) {
            serverMessage = new UpdateView(null, propertyName, null, newValue);
            virtualView.sendToEveryone(serverMessage);
        }
    }
}
