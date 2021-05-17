package it.polimi.ingsw.listeners;

import it.polimi.ingsw.events.servermessages.ServerMessage;
import it.polimi.ingsw.events.servermessages.UpdateView;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.constants.PlayerConstants.STRONGBOX_CHANGE;

public class DashboardListener extends PropertyListener {
    public DashboardListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(STRONGBOX_CHANGE)) {
            ServerMessage serverMessage = new UpdateView((String) evt.getSource(), evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            virtualView.sendToEveryone(serverMessage);
        }
    }
}