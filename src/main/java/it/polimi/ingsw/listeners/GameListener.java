package it.polimi.ingsw.listeners;

import it.polimi.ingsw.events.servermessages.ServerMessage;
import it.polimi.ingsw.events.servermessages.TokenDrawn;
import it.polimi.ingsw.events.servermessages.UpdateView;
import it.polimi.ingsw.model.card.Deck;
import it.polimi.ingsw.model.token.SoloActionToken;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.constants.GameConstants.END_TURN;
import static it.polimi.ingsw.constants.GameConstants.TOKEN_DRAWN;
import static it.polimi.ingsw.constants.PlayerConstants.GRID_CHANGE;

/**
 * Class GameListener handles the updates regarding the attributes' changes in Game.
 * @author Gabriele Lazzarelli
 */
public class GameListener extends PropertyListener{
    /**
     * Constructor GameListener takes a VirtualView as a parameter.
     * @param virtualView the VirtualView used to forward messages to the players.
     */
    public GameListener(VirtualView virtualView) {
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
        Object oldValue = evt.getOldValue();
        Object newValue = evt.getNewValue();

        if (END_TURN.equals(propertyName)) {
            serverMessage = new UpdateView(null, propertyName, oldValue, newValue);
            virtualView.sendToEveryone(serverMessage);
        } else if (TOKEN_DRAWN.equals(propertyName)) {
            serverMessage = new TokenDrawn((SoloActionToken) newValue);
            virtualView.sendToEveryone(serverMessage);
        } else if (GRID_CHANGE.equals(propertyName)) {
            serverMessage = new UpdateView(null, propertyName, oldValue, translateGrid((Deck[][]) newValue));
            virtualView.sendToEveryone(serverMessage);
        }
    }
}
