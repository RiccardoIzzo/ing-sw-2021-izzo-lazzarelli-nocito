package it.polimi.ingsw.view;

import it.polimi.ingsw.events.clientmessages.ClientMessage;
import it.polimi.ingsw.events.servermessages.ServerMessage;

import java.util.Map;

/**
 * Interface View has to be implemented by the two types of view, CLI and GUI.
 * It generalizes the concept of view.
 *
 * @author Riccardo Izzo, Gabriele Lazzarelli, Andrea Nocito
 */
public interface View {
    String getNickname();
    void setNickname();
    void setModelView(ModelView modelView);
    String getInput(String check);
    void handleNickname(ServerMessage message);
    void handleLobbies(Map<String, Integer> lobbies);
    void handleLeaders();
    void handleBonusResource(int amount);
    void handleTurn();
    boolean handleTakeResource();
    boolean handleBuyCard();
    boolean handleActivateProduction();
    boolean handleActivateLeader();
    boolean handleDiscardLeader();
    boolean handleEndTurn();
    void handleTemporaryShelf();
    void printText(String text);
    void send(ClientMessage message);
}
