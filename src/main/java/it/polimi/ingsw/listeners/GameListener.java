package it.polimi.ingsw.listeners;

import it.polimi.ingsw.events.servermessages.ServerMessage;
import it.polimi.ingsw.events.servermessages.UpdateView;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.constants.GameConstants.END_TURN;
import static it.polimi.ingsw.constants.GameConstants.TOKEN_DRAWN;

public class GameListener extends PropertyListener{
    public GameListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ServerMessage serverMessage;
//        String playerSource = (String) evt.getSource();
        String propertyName = evt.getPropertyName();
        Object oldValue = evt.getOldValue();
        Object newValue = evt.getNewValue();

        if (END_TURN.equals(propertyName)) {
            serverMessage = new UpdateView(null, propertyName, oldValue, newValue);
            virtualView.sendToEveryone(serverMessage);
        } else if (TOKEN_DRAWN.equals(propertyName)) {
            serverMessage = new UpdateView(null, propertyName, oldValue, newValue);
            virtualView.sendToEveryone(serverMessage);
        }
    }
}
