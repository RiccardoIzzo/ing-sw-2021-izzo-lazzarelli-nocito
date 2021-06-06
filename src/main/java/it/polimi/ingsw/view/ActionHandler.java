package it.polimi.ingsw.view;

import it.polimi.ingsw.events.clientmessages.SetFinalTurn;
import it.polimi.ingsw.events.servermessages.*;

import java.util.concurrent.ArrayBlockingQueue;

import static it.polimi.ingsw.constants.GameConstants.END_TURN;
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
        else if(message instanceof EndGame){
            view.printText("The game is about to finish.\nWaiting for the remaining players to play theirs last turn...");
            view.send(new SetFinalTurn());
        }
        else if(message instanceof GameStats){
            view.showStats(((GameStats) message).getPlayerPoints());
        }
        else if(message instanceof TokenDrawn){
            view.printText("\nToken drawn.\n" + ((TokenDrawn) message).getToken().toString() + "\n");
            view.startTurn();
            view.handleTurn();
        }
        else if(message instanceof Defeat){
            view.printText("You lost!");
            //finish game
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
