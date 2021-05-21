package it.polimi.ingsw.listeners;

import it.polimi.ingsw.events.servermessages.ServerMessage;
import it.polimi.ingsw.events.servermessages.UpdateView;
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
        ServerMessage serverMessage;
        String playerSource = (String) evt.getSource();
        String propertyName = evt.getPropertyName();
        Object oldValue = evt.getOldValue();
        Object newValue = evt.getNewValue();

        switch (propertyName) {
            case MARKET_CHANGE -> {
                serverMessage = new UpdateView(null, propertyName, oldValue, newValue);
                virtualView.sendToEveryone(serverMessage);
            }
            case SLIDE_MARBLE -> {
                serverMessage = new UpdateView(null, propertyName, oldValue, newValue);
                virtualView.sendToEveryone(serverMessage);
            }
        }
    }
}
