package it.polimi.ingsw.view;

import it.polimi.ingsw.events.clientmessages.CloseLobby;
import it.polimi.ingsw.events.clientmessages.GetLobbies;
import it.polimi.ingsw.events.clientmessages.SetFinalTurn;
import it.polimi.ingsw.events.servermessages.*;
import javafx.scene.control.Alert;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

import static it.polimi.ingsw.constants.GameConstants.*;
import static it.polimi.ingsw.constants.PlayerConstants.*;

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
     * Based on the message received, it performs the corresponding action on the view.
     */
    public void handle(ServerMessage message){
        if(message instanceof ValidNickname || message instanceof InvalidNickname){
            view.handleNickname(message);
        }
        else if(message instanceof SendLobbies){
            view.handleLobbies(((SendLobbies) message).getLobbies());
        }
        else if(message instanceof LobbyJoined){
            view.printText("Lobby joined! Waiting for other players...\n");
            if (view instanceof GUI) {
                ((GUI) view).handleLobbyJoined();
            }
        }
        else if(message instanceof LobbyFull){
            if(view instanceof CLI){
                view.printText("This lobby is full! Try again.\n");
                view.send(new GetLobbies());
            }

            if (view instanceof GUI) {
                ((GUI) view).enableLobbies();
                ((GUI) view).showAlert("This lobby is full! Try again.", Alert.AlertType.ERROR);
            }
        }
        else if(message instanceof LobbyError){
            if(view instanceof CLI){
                view.printText("An error occurred while creating the lobby! Try again.\n");
                view.send(new GetLobbies());
            }

            if (view instanceof GUI) {
                ((GUI) view).enableLobbies();
                ((GUI) view).showAlert("An error occurred while creating the lobby! Try again.", Alert.AlertType.ERROR);
            }
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

            if (view instanceof GUI) {
                ((GUI) view).handleEndGame();
            }
        }
        else if(message instanceof GameStats){
            view.showStats(((GameStats) message).getPlayerPoints());
            if(view instanceof CLI){
                System.exit(0);
            }
        }
        else if(message instanceof TokenDrawn){
            view.printText("\nToken drawn:\n" + ((TokenDrawn) message).getToken().toString());
            if (view instanceof GUI) {
                ((GUI) view).handleToken(((TokenDrawn) message).getToken());
            }
            view.startTurn();
            view.handleTurn();
        }
        else if(message instanceof Defeat){
            view.send(new CloseLobby());
            if(view instanceof CLI) {
                view.printText("You lost!");
                System.exit(0);
            }
            else if(view instanceof GUI) {
                ((GUI) view).handleDefeat();
            }
        }
        else if(message instanceof Reconnection) {
            if(view instanceof GUI)
                ((GUI) view).setReconnected(0);
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
                if(view instanceof GUI && ((GUI) view).dashboardController != null) {
                    ((GUI) view).dashboardController.updatePlayerDashboard();
                    ((GUI) view).dashboardController.handleWaitingText();
                }
            }
            else if (TEMPORARY_SHELF_CHANGE.equals(propertyName)){
                view.handleTemporaryShelf();
            }
            else {
                if(view instanceof GUI && ((GUI) view).dashboardController != null) {
                    switch(propertyName) {
                        case STRONGBOX_CHANGE:
                            ((GUI) view).dashboardController.updatePlayerStrongbox();
                            break;
                        case FAITH_MARKER_POSITION:
                        case BLACK_MARKER_POSITION:
                        case POPES_TILES_CHANGE:
                        case TILES_UNCOVERED_CHANGE:
                            ((GUI) view).dashboardController.updatePlayerFaithTrack();
                            break;
                        case ACTIVE_DEVELOPMENTS_CHANGE:
                            ((GUI) view).dashboardController.updatePlayerActiveDevelopments();
                            ((GUI) view).dashboardController.updatePlayerWarehouse();
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    /**
     * Runnable class method that handle a single ServerMessage message.
     */
    @Override
    public void run() {
        while (true) {
            ServerMessage serverMessage = null;
            try {
                serverMessage = messages.take();
            } catch (InterruptedException e) {
                Logger.getLogger("ActionHandler error");
                Thread.currentThread().interrupt();
            }
            handle(serverMessage);
        }
    }
}
