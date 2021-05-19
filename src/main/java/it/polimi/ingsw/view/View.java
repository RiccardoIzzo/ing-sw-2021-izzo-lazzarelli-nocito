package it.polimi.ingsw.view;

import it.polimi.ingsw.events.clientmessages.ClientMessage;
import it.polimi.ingsw.events.servermessages.ServerMessage;

import java.util.Map;

/**
 * Interface View has to be implemented by the two types of view, CLI and GUI.
 * It generalizes the concept of view.
 */
public interface View {
    void setNickname();
    void handleNickname(ServerMessage message);
    void handleLobbies(Map<String, Integer> lobbies);
    void printText(String text);
    void send(ClientMessage message);
    void selectBonusResource(int amount);
}
