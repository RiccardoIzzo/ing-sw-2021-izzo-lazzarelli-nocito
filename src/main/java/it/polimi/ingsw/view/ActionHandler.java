package it.polimi.ingsw.view;

import it.polimi.ingsw.events.servermessages.*;

/**
 * ActionHandler class manages the ServerMessage from the server and updates the view.
 *
 * @author Riccardo Izzo, Gabriele Lazzarelli
 */
public class ActionHandler extends Thread{
    private final View view;
    private final ServerMessage message;

    /**
     * Constructor ActionHandler creates a new ActionHandler instance.
     * @param view interface view, represents the CLI or the GUI.
     * @param message ServerMessage to handle.
     *
     */
    public ActionHandler(View view, ServerMessage message){
        this.view = view;
        this.message = message;
    }

    /**
     * Method handle updates the view.
     */
    public void handle(){
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
    }

    /**
     * Runnable class method that handle a single ServerMessage.
     */
    @Override
    public void run() {
        handle();
    }
}
