package it.polimi.ingsw.listeners;

import it.polimi.ingsw.events.servermessages.EndGame;
import it.polimi.ingsw.events.servermessages.ServerMessage;
import it.polimi.ingsw.events.servermessages.UpdateView;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Deck;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
        ServerMessage serverMessage;
        String playerSource = (String) evt.getSource();
        String propertyName = evt.getPropertyName();
        Object oldValue = evt.getOldValue();
        Object newValue = evt.getNewValue();

        if (SET_LEADERS.equals(propertyName)) {
            serverMessage = new UpdateView(playerSource, propertyName, oldValue, translateLeadersToMap((HashSet<LeaderCard>) newValue));
            virtualView.sendToPlayer(playerSource, serverMessage);
        } else if (DISCARD_LEADER.equals(propertyName) || LEADER_ACTIVATION.equals(propertyName)) {
            serverMessage = new UpdateView(playerSource, propertyName, oldValue, translateLeadersToMap((HashSet<LeaderCard>) newValue));
            virtualView.sendToEveryone(serverMessage);
        } else if (GRID_CHANGE.equals(propertyName)) {
            serverMessage = new UpdateView(null, propertyName, oldValue, translateGrid((Deck[][]) newValue));
            virtualView.sendToEveryone(serverMessage);
        } else if (DEVELOPMENTS_CHANGE.equals(propertyName)) {
            serverMessage = new UpdateView(playerSource, propertyName, oldValue, translateCards((Collection<Card>) newValue));
            virtualView.sendToEveryone(serverMessage);
            if(((Set<DevelopmentCard>) newValue).size() == 7) {
                ServerMessage endGame = new EndGame();
                virtualView.sendToEveryone(endGame);
            }
        } else if (ACTIVE_DEVELOPMENTS_CHANGE.equals(propertyName)) {
            serverMessage = new UpdateView(playerSource, propertyName, oldValue, translateCards((Collection<Card>) newValue));
            virtualView.sendToEveryone(serverMessage);
        }
    }
}
