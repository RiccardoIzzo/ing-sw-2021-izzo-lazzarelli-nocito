package it.polimi.ingsw.listeners;

import it.polimi.ingsw.events.servermessages.GameStarted;
import it.polimi.ingsw.events.servermessages.Reconnection;
import it.polimi.ingsw.events.servermessages.UpdateView;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SinglePlayerGame;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.SinglePlayerFaithTrack;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

import static it.polimi.ingsw.constants.GameConstants.*;
import static it.polimi.ingsw.constants.PlayerConstants.*;

/**
 * Class DisconnectionListener handles the retrieval of a player data who just reconnected to the game.
 * @author Gabriele Lazzarelli
 */
public class DisconnectionListener extends PropertyListener{
    private final Game game;

    /**
     * Constructor DisconnectionListener takes a VirtualView as a parameter.
     * @param virtualView the VirtualView used to forward messages to the players.
     */
    public DisconnectionListener(VirtualView virtualView, Game game) {
        super(virtualView);
        this.game = game;
    }

    /**
     * Method propertyChange takes an event as parameter and send various message of update using the event property name as .
     * @param evt the PropertyChangeEvent to handle.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String playerSource = evt.getPropertyName();
        virtualView.sendToPlayer(playerSource, new Reconnection());
        virtualView.sendToPlayer(playerSource, new GameStarted((ArrayList<String>) game.getPlayers().stream().map(Player::getNickname).collect(Collectors.toList())));
        virtualView.sendToPlayer(playerSource, new UpdateView(null, GRID_CHANGE, null, translateGrid(game.getGrid())));
        virtualView.sendToPlayer(playerSource, new UpdateView(null, TILES_UNCOVERED_CHANGE, null, game.getPlayerByName(playerSource).getDashboard().getFaithTrack().getTilesUncovered()));
        virtualView.sendToPlayer(playerSource, new UpdateView(null, SLIDE_MARBLE, null, game.getMarket().getSlideMarble()));
        virtualView.sendToPlayer(playerSource, new UpdateView(null, MARKET_CHANGE, null, game.getMarket().getMarketTray()));

        for(Player user : game.getPlayers()){
            String name = user.getNickname();
            virtualView.sendToPlayer(playerSource, new UpdateView(name, DISCARD_LEADER, null, translateLeadersToMap((HashSet<LeaderCard>) game.getPlayerByName(name).getLeaders())));
            virtualView.sendToPlayer(playerSource, new UpdateView(name, DEVELOPMENTS_CHANGE, null, translateCards(user.getDevelopments())));
            virtualView.sendToPlayer(playerSource, new UpdateView(name, ACTIVE_DEVELOPMENTS_CHANGE, null, translateCards(user.getActiveDevelopments())));
            virtualView.sendToPlayer(playerSource, new UpdateView(name, STRONGBOX_CHANGE, null, game.getPlayerByName(name).getDashboard().getStrongbox()));
            virtualView.sendToPlayer(playerSource, new UpdateView(name, SHELF_CHANGE, null, game.getPlayerByName(name).getDashboard().getWarehouse().getShelves()));
            virtualView.sendToPlayer(playerSource, new UpdateView(name, FAITH_MARKER_POSITION, null, game.getPlayerByName(name).getDashboard().getFaithTrack().getPlayerPosition()));
            if(game instanceof SinglePlayerGame) {
                virtualView.sendToPlayer(playerSource, new UpdateView(name, BLACK_MARKER_POSITION, null,  ((SinglePlayerFaithTrack) game.getPlayerByName(name).getDashboard().getFaithTrack()).getBlackFaithMarker()));
            }
            virtualView.sendToPlayer(playerSource, new UpdateView(name, POPES_TILES_CHANGE, null, game.getPlayerByName(name).getDashboard().getFaithTrack().getPopesFavorTiles()));
        }
    }
}
