package it.polimi.ingsw.network;

import it.polimi.ingsw.events.clientmessages.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * NetworkHandler class represents the client side of the game, it manages connection with server.
 *
 * @author Riccardo Izzo
 */
public class NetworkHandler {
    private final String ip;
    private final int port;
    private Socket socket;
    private ServerConnection serverConnection;
    private ObjectOutputStream output;

    /**
     * Constructor NetworkHandler creates a new instance of NetworkHandler.
     * @param ip server address.
     * @param port server port.
     */
    public NetworkHandler(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    /**
     * Method setConnection creates a new socket connection, enables outbound communication and starts a thread that manages inbound communication.
     */
    public void setConnection(){
        try {
            socket = new Socket(ip, port);
            output = new ObjectOutputStream(socket.getOutputStream());
            serverConnection = new ServerConnection(socket);
            serverConnection.setStatus(true);
            (new Thread(serverConnection)).start();
            keepAlive();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method keepAlive verifies that the connection between client and server is alive and responsive.
     * The server has a TIMEOUT set on the socket that throws an exception if it doesn't receive anything during this period of time.
     * This method keep the connection alive by sending an Heartbeat message every TIMEOUT/2.
     */
    private void keepAlive(){
        new Thread(() -> {
            while(true){
                try {
                    TimeUnit.MILLISECONDS.sleep(10000);
                    sendToServer(new Heartbeat());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Method close terminates the client connection by closing the socket and the stream of data.
     */
    private void close(){
        try {
            serverConnection.setStatus(false);
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method sendToServer allows communication from client to server.
     * It sends a ClientMessage (Serializable Object) to the server.
     * @param message ClientMessage to be sent to the server.
     */
    public void sendToServer(ClientMessage message){
        try {
            output.reset();
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            System.err.println("Can't find server! Quitting...");
            close();
            //notify every player and quit
            System.exit(0);
        }
    }
}
