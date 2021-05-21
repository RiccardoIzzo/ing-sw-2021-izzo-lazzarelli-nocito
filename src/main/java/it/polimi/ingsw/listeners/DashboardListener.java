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
        ServerMessage serverMessage;
        String playerSource = (String) evt.getSource();
        String propertyName = evt.getPropertyName();
        Object oldValue = evt.getOldValue();
        Object newValue = evt.getNewValue();

        if (propertyName.equals(STRONGBOX_CHANGE)) {
            serverMessage = new UpdateView(playerSource, propertyName, oldValue, newValue);
            virtualView.sendToEveryone(serverMessage);
        }
    }
}