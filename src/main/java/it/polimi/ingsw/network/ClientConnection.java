package it.polimi.ingsw.network;

import it.polimi.ingsw.events.clientmessages.*;
import it.polimi.ingsw.events.servermessages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class ClientConnection manages server-client communication.
 *
 * @author Riccardo Izzo
 */
public class ClientConnection implements Runnable{
    private final Server server;
    private final Socket socket;
    private String nickname;
    /**
     * Input/Output data streams.
     */
    private ObjectInputStream input;
    private ObjectOutputStream output;
    /**
     * Server-side connection status.
     */
    private boolean status;
    /**
     * Socket timeout, if it expires it means that a disconnection has occurred.
     */
    private static final int TIMEOUT = 20000;

    /**
     * Constructor ClientConnection creates a new instance of ClientConnection and enables inbound and outbound transmission channels.
     * It also sets a socket timeout used in receiveFromClient method to check if connection is still alive.
     * @param server instance of Server.
     * @param socket server socket that accepts connections.
     */
    public ClientConnection(Server server, Socket socket){
        this.server = server;
        this.socket = socket;
        status = false;
        try{
            socket.setSoTimeout(TIMEOUT);
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e){
            System.err.println("Something went wrong during the client connection initialization.");
        }
    }

    /**
     * Method setStatus sets the server-side connection status.
     * @param status connection status, true if the server is active.
     */
    public void setStatus(boolean status){
        this.status = status;
    }

    /**
     * Method getNickname returns the name of the player associated with the current connection.
     * @return the player's name.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Method setNickname sets the name of the player.
     * @param nickname the player's nickname.
     */
    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    /**
     * Method sentToClient allows communication from server to client.
     * It sends a ServerMessage (Serializable Object) to the client.
     * @param message ServerMessage to be sent to the client.
     */
    public void sendToClient(ServerMessage message){
        try {
            output.reset();
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method receiveFromClient allows communication from client to server.
     * It receives a ClientMessage (Serializable Object) from the client.
     */
    public synchronized void receiveFromClient(){
        try {
            ClientMessage message = (ClientMessage) input.readObject();
            if (!(message instanceof Heartbeat)) {
                System.out.println(message.getClass().getSimpleName() + " from " + socket.getRemoteSocketAddress().toString());
                if(message instanceof SetNickname) setNickname(((SetNickname) message).getNickname());
                server.handleMessage(this, message);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Disconnected! Connection lost with " + socket.getRemoteSocketAddress().toString());
            server.handleMessage(this, new Disconnection());
            close();
        }
    }

    /**
     * Method close terminates the server connection by closing the socket and the stream of data.
     */
    private void close(){
        try {
            setStatus(false);
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Runnable class method that waits for incoming messages from the client.
     */
    @Override
    public void run() {
        while(status){
            receiveFromClient();
        }
    }
}
