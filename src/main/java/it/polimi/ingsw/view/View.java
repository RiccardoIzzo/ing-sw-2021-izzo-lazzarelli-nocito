package it.polimi.ingsw.view;

import it.polimi.ingsw.events.clientmessages.ClientMessage;
import it.polimi.ingsw.events.servermessages.ServerMessage;
import it.polimi.ingsw.events.servermessages.TokenDrawn;

import java.util.ArrayList;
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
    void startTurn();
    ArrayList<Action> getValidActions();
    void basicActionPlayed();
    void handleTurn();
    void handleTakeResource();
    void handleBuyCard();
    void handleActivateProduction();
    void handleActivateLeader();
    void handleCheckRequirement(boolean result, int id);
    void handleDiscardLeader();
    void handleEndTurn();
    void handleTemporaryShelf();
    void printText(String text);
    void send(ClientMessage message);
    void showStats(Map<String, Integer> map);

}
