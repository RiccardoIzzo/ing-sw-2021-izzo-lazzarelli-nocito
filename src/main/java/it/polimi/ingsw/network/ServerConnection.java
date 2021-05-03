package it.polimi.ingsw.network;

import it.polimi.ingsw.events.servermessages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * ServerConnection class manages inbound connection with the server.
 *
 * @author Riccardo Izzo
 */
public class ServerConnection implements Runnable{
    private ObjectInputStream input;
    private boolean status;
    //private ActionHandler actionHandler;

    /**
     * Constructor ServerConnection creates a new instance of ServerConnection.
     * @param socket instance of socket.
     */
    public ServerConnection(Socket socket){
        try {
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
     */
    public void receiveFromServer(){
        try {
            ServerMessage message = (ServerMessage) input.readObject();
            //actionHandler.handle(message);
        } catch (IOException | ClassNotFoundException e) {
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
