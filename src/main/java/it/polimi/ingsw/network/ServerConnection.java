package it.polimi.ingsw.network;

import it.polimi.ingsw.events.servermessages.*;
import it.polimi.ingsw.view.ActionHandler;
import it.polimi.ingsw.view.ModelView;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ServerConnection class manages inbound connection with the server.
 *
 * @author Riccardo Izzo
 */
public class ServerConnection implements Runnable{
    private ObjectInputStream input;
    private boolean status;
    private final View view;
    private ModelView modelView;
    private final ExecutorService executors;

    /**
     * Constructor ServerConnection creates a new instance of ServerConnection.
     * @param socket instance of socket.
     * @param view view interface.
     */
    public ServerConnection(Socket socket, View view){
        this.view = view;
        executors = Executors.newCachedThreadPool();
        try {
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
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
     * Note that, to avoid conflicts during the model view update, only one ModelViewUpdater thread can be active.
     */
    public void receiveFromServer(){
        try {
            ServerMessage message = (ServerMessage) input.readObject();
            if(message instanceof UpdateView) {
                ModelViewUpdater updater = new ModelViewUpdater(view, modelView, (UpdateView) message);
                updater.start();
                updater.join();
            }
            else executors.execute(new ActionHandler(view, message));
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            System.out.println("Can't find server! Quitting...");
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
