package it.polimi.ingsw.listeners;

import it.polimi.ingsw.events.servermessages.Defeat;
import it.polimi.ingsw.events.servermessages.EndGame;
import it.polimi.ingsw.events.servermessages.ServerMessage;
import it.polimi.ingsw.events.servermessages.UpdateView;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.JsonCardsCreator;
import it.polimi.ingsw.model.SinglePlayerGame;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static it.polimi.ingsw.constants.PlayerConstants.*;

/**
 * Class LeaderCardListener notifies the VirtualView when a LeaderCard is activated.
 * @author Gabriele Lazzarelli
 */
public class PlayerListener extends PropertyListener {
    private final Game game;

    public PlayerListener(VirtualView virtualView, Game game) {
        super(virtualView);
        this.game = game;
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
            if (game instanceof SinglePlayerGame){
                ArrayList<Integer> developmentIDs = translateGrid((Deck[][]) newValue);
                for(CardColor cardColor : CardColor.values()){
                    if (developmentIDs.stream().map(JsonCardsCreator::generateDevelopmentCard).noneMatch(card -> card.getType() == cardColor)){
                        ServerMessage message = new Defeat();
                        virtualView.sendToEveryone(message);
                    }
                }
            }
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
