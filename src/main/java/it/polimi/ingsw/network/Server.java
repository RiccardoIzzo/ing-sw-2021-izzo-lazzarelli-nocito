package it.polimi.ingsw.network;

import it.polimi.ingsw.events.servermessages.*;
import java.util.*;

/**
 * Server class represents the server side of the game, it manages connections with clients.
 *
 * @author Riccardo Izzo
 */
public class Server {
    /**
     * List of active connections.
     */
    private List<ClientConnection> connections;

    /**
     * Map that associates the player's name with the connection established with his client.
     */
    private Map<String, ClientConnection> connectionMap;
    private final ServerHandler serverHandler;
    //reference to controller (Controller/GameHandler?)

    /**
     * Constructor Server creates a new Server instance.
     * @param port server port that will be managed by the serverHandler.
     */
    public Server(int port){
        serverHandler = new ServerHandler(this, port);
        connections = new ArrayList<>();
        connectionMap = new HashMap<>();
    }

    /**
     * Method getServerHandler returns the serverHandler.
     * @return the serverHandler.
     */
    public ServerHandler getServerHandler(){
        return serverHandler;
    }

    /**
     * Method getConnectionByPlayerName returns the connection from connectionMap based on the player's name.
     * @param nickname the player's nickname.
     * @return the connection associated with that player nickname.
     */
    public ClientConnection getConnectionByPlayerName(String nickname){
        return connectionMap.get(nickname);
    }

    /**
     * Method registerPlayer adds a new connection to the map by associating it with the player's name.
     * If the player's name already exists then it means that a disconnection has occurred, in this case the connection associated with that player is updated.
     * @param name the player's nickname.
     * @param clientConnection the connection associated with that player nickname.
     */
    public synchronized void registerPlayer(String name, ClientConnection clientConnection){
        /*
          New connection, first time that the client connects to the server.
         */
        if(!(connectionMap.containsKey(name))) {connectionMap.put(name, clientConnection);}
        /*
          Already exists a connection associated to that player's name, a disconnection has occurred and the player is connecting again.
          Updates socket connection associated to that nickname.
         */
        else {connectionMap.replace(name, clientConnection);}
        clientConnection.sendToClient(new TextMessage("Connected!"));
    }

    /**
     * Method addConnection adds the selected connection to the list of active ones.
     * @param clientConnection the connection to be added.
     */
    public synchronized void addConnection(ClientConnection clientConnection){
        connections.add(clientConnection);
    }

    /**
     * Method removeConnection removes the selected connection from the list of active ones.
     * @param clientConnection the connection to be removed.
     */
    public synchronized void removeConnection(ClientConnection clientConnection){
        connections.remove(clientConnection);
        sendEveryone(new TextMessage(clientConnection.getNickname() + " has disconnected!"));
    }

    /**
     * Method sendEveryone sends a message to every active client connection.
     * @param message the message to send.
     */
    public void sendEveryone(ServerMessage message){
        for(ClientConnection connection : connections){
            connection.sendToClient(message);
        }
    }

    /**
     * Server main class.
     * It asks for the server port, creates a new instance of Server and starts the serverHandler.
     * @param args main args.
     */
    public static void main(String[] args) {
        System.out.println("Welcome to Master of Renaissance server...");
        System.out.println("Insert server port: ");
        int port = new Scanner(System.in).nextInt();
        while(port < 1023){
            System.out.println("Error, the port number must be greater than 1023! Insert again: ");
            port = new Scanner(System.in).nextInt();
        }
        Server server = new Server(port);
        System.out.println("Starting server...");
        (new Thread(server.serverHandler)).start();
    }
}