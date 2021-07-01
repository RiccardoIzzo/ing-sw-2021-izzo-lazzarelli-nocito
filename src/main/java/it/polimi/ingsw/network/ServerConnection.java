package it.polimi.ingsw.network;

import it.polimi.ingsw.events.servermessages.EndGame;
import it.polimi.ingsw.events.servermessages.GameStarted;
import it.polimi.ingsw.events.servermessages.ServerMessage;
import it.polimi.ingsw.events.servermessages.UpdateView;
import it.polimi.ingsw.view.ActionHandler;
import it.polimi.ingsw.view.ModelView;
import it.polimi.ingsw.view.ModelViewUpdater;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

import static it.polimi.ingsw.constants.GameConstants.END_TURN;
import static it.polimi.ingsw.constants.GameConstants.TOKEN_DRAWN;
import static it.polimi.ingsw.constants.PlayerConstants.*;

/**
 * ServerConnection class manages inbound connection with the server.
 *
 * @author Riccardo Izzo, Gabriele Lazzarelli
 */
public class ServerConnection implements Runnable{
    private ObjectInputStream input;
    private boolean status;
    private final ActionHandler actionHandler;
    private final ArrayBlockingQueue<ServerMessage> messages;
    private ModelView modelView;

    /**
     * Constructor ServerConnection creates a new instance of ServerConnection.
     * @param socket instance of socket.
     * @param view view interface.
     */
    public ServerConnection(Socket socket, View view){
        this.messages = new ArrayBlockingQueue<>(10000);
        this.actionHandler = new ActionHandler(view, messages);
        actionHandler.start();
        try {
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            Logger.getLogger("ServerConnection error");
        }
    }

    /**
     * Method setModelView sets the model view.
     * @param modelView the model view.
     */
    public void setModelView(ModelView modelView) {
        this.modelView = modelView;
    }

    /**
     * Method setStatus sets the client-side connection status.
     * @param status connection status, true if the client is active.
     */
    public void setStatus(boolean status){
        this.status = status;
    }

    /**
     * Method receiveFromServer allows communication from server to client.
     * It receives a ServerMessage (Serializable Object) from the server.
     * In case of an UpdateView message it starts a new ModelViewUpdater thread that handle the update of the model view.
     * Otherwise it executes a new ActionHandler thread that handle the view based on the message received.
     */
    public void receiveFromServer(){
        ModelViewUpdater modelViewUpdater;
        try {
            ServerMessage message = (ServerMessage) input.readObject();
            if (message instanceof UpdateView) {
                modelViewUpdater = new ModelViewUpdater(modelView, (UpdateView) message);
                modelViewUpdater.start();
                if ((SET_LEADERS.equals(((UpdateView) message).getPropertyName()) || TEMPORARY_SHELF_CHANGE.equals(((UpdateView) message).getPropertyName()) || TOKEN_DRAWN.equals(((UpdateView) message).getPropertyName()) || END_TURN.equals(((UpdateView) message).getPropertyName()))){
                    modelViewUpdater.join();
                }
                messages.add(message);
            }
            else {
                if(message instanceof GameStarted) {
                    actionHandler.handle(message);
                } else if (message instanceof EndGame) {
                    actionHandler.handle(message);
                }
                else messages.add(message);
            }
        }
        catch (IOException | ClassNotFoundException | InterruptedException e) {
            System.out.println("Can't find server! Quitting...");
            Thread.currentThread().interrupt();
            System.exit(0);
        }
    }

    /**
     * Runnable class method that waits for incoming messages from the server.
     */
    @Override
    public void run() {
        while(status){
            receiveFromServer();
        }
    }
}
