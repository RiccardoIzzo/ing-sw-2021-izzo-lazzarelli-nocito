package it.polimi.ingsw.view;

import it.polimi.ingsw.events.servermessages.*;

/**
 * ActionHandler class manages the ServerMessage from the server and updates the view.
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
        }
        else if(message instanceof GetBonusResources){
            //view.selectBonusResource(((BonusResources) message).getAmount());
        }
        else if(message instanceof TextMessage){
            view.printText(((TextMessage) message).getText());
        }
        else if (message instanceof UpdateView){
            UpdateView messageUpdate = (UpdateView) message;
            String sourcePlayer = messageUpdate.getSourcePlayer();
            String propertyName = messageUpdate.getPropertyName();
            modelView.updateModelView(sourcePlayer, (String) ((UpdateView) message).getOldValue(), ((UpdateView) message).getNewValue());
            switch(propertyName){
                case "SET_LEADERS" -> {
                    //view.handleLeaders();
                }
            }
        }
    }
}
