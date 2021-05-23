package it.polimi.ingsw.view;

import it.polimi.ingsw.events.servermessages.*;

import static it.polimi.ingsw.constants.PlayerConstants.*;
import static it.polimi.ingsw.constants.GameConstants.*;

/**
 * ActionHandler class manages the ServerMessage from the server and updates the view.
 *
 * @author Riccardo Izzo
 */
public class ActionHandler {
    private final View view;
    private ModelView modelView;

    /**
     * Constructor ActionHandler creates a new ActionHandler instance.
     * @param view interface view, represents the CLI or the GUI.
     *
     */
    public ActionHandler(View view){
        this.view = view;
    }

    /**
     * Method handle updates the view.
     * @param message the ServerMessage to handle.
     */
    public void handle(ServerMessage message){
        if(message instanceof ValidNickname || message instanceof InvalidNickname){
            view.handleNickname(message);
        }
        else if(message instanceof SendLobbies){
            view.handleLobbies(((SendLobbies) message).getLobbies());
        }
        else if(message instanceof LobbyJoined){
            view.printText("Lobby joined! Waiting for other players...");
        }
        else if(message instanceof LobbyFull){
            view.printText("This lobby is full! Try again.");
        }
        else if(message instanceof GameStarted){
            view.printText("The game is about to start.");
            modelView = new ModelView(((GameStarted) message).getPlayers(), view.getNickname());
            view.setModelView(modelView);
        }
        else if(message instanceof GetBonusResources){
            view.handleBonusResource(((GetBonusResources) message).getAmount());
        }
        else if(message instanceof TextMessage){
            view.printText(((TextMessage) message).getText());
        }
        else if(message instanceof StartTurn){
            view.handleTurn();
        }
        else if(message instanceof UpdateView){
            UpdateView messageUpdate = (UpdateView) message;
            String sourcePlayer = messageUpdate.getSourcePlayer();
            String propertyName = messageUpdate.getPropertyName();
            Object newValue = messageUpdate.getNewValue();
            modelView.updateModelView(sourcePlayer, propertyName, newValue);
            if (SET_LEADERS.equals(propertyName)) {
                view.handleLeaders();
            }
        }
    }
}
