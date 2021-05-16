package it.polimi.ingsw.listeners;

import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.constants.GameConstants.MARKET_CHANGE;
import static it.polimi.ingsw.constants.GameConstants.SLIDE_MARBLE;

/**
 * Class MarketListener notifies the player(s) when the market changes its configuration.
 * @author Gabriele Lazzarelli
 */
public class MarketListener extends PropertyListener {
    public MarketListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(MARKET_CHANGE)) {
            //new message
            //send message
        } else if (evt.getPropertyName().equals(SLIDE_MARBLE)) {
            //new message
            //send message
        }
    }
}
