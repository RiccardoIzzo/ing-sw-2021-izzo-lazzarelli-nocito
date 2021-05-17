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
        if (evt.getPropertyName().equals(END_TURN)) {
            ServerMessage serverMessage = new UpdateView(null, evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            virtualView.sendToEveryone(serverMessage);
        } else if (evt.getPropertyName().equals(TOKEN_DRAWN)) {
            ServerMessage serverMessage = new UpdateView(null, evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            virtualView.sendToEveryone(serverMessage);
        }
    }
}
