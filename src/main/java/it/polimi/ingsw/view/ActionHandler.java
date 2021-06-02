package it.polimi.ingsw.view;

import it.polimi.ingsw.events.servermessages.*;

import java.util.concurrent.ArrayBlockingQueue;

import static it.polimi.ingsw.constants.GameConstants.END_TURN;
import static it.polimi.ingsw.constants.GameConstants.TOKEN_DRAWN;
import static it.polimi.ingsw.constants.PlayerConstants.SET_LEADERS;
import static it.polimi.ingsw.constants.PlayerConstants.TEMPORARY_SHELF_CHANGE;

/**
 * ActionHandler class manages the ServerMessage from the server and updates the view.
 *
 * @author Riccardo Izzo, Gabriele Lazzarelli
 */
public class ActionHandler extends Thread{
    private final View view;
    private final ArrayBlockingQueue<ServerMessage> messages;

    /**
     * Constructor ActionHandler creates a new ActionHandler instance.
     * @param view interface view, represents the CLI or the GUI.
     *
     */
    public ActionHandler(View view, ArrayBlockingQueue<ServerMessage> messages){
        this.view = view;
        this.messages = messages;
    }

    /**
     * Method handle updates the view.
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
            view.setModelView(new ModelView(((GameStarted) message).getPlayers(), view.getNickname()));
        }
        else if(message instanceof GetBonusResources){
            view.handleBonusResource(((GetBonusResources) message).getAmount());
        }
        else if(message instanceof TextMessage){
            view.printText(((TextMessage) message).getText());
        }
        else if(message instanceof StartTurn){
            view.startTurn();
            view.handleTurn();
        }
        else if(message instanceof CheckRequirementResult){
            view.handleCheckRequirement(((CheckRequirementResult) message).isRequirementMet(), ((CheckRequirementResult) message).getId());
        }
        else if(message instanceof UpdateView){
            UpdateView updateView = (UpdateView) message;
            String propertyName = updateView.getPropertyName();
            Object newValue = updateView.getNewValue();
            if (SET_LEADERS.equals(propertyName)) {
                view.handleLeaders();
            }
            else if (END_TURN.equals(propertyName)){
                view.printText(newValue.toString() + ", ready to play.");
            }
            else if (TEMPORARY_SHELF_CHANGE.equals(propertyName)){
                view.handleTemporaryShelf();
            }
            else if (TOKEN_DRAWN.equals(propertyName)){
                view.handleSoloActionToken();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            ServerMessage serverMessage = null;
            try {
                serverMessage = messages.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handle(serverMessage);
        }
    }
}
