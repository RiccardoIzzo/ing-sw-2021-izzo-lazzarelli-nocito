package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.events.clientmessages.*;
import it.polimi.ingsw.events.servermessages.*;
import java.util.*;

/**
 * Server class represents the server side of the game, it manages connections with clients.
 *
 * @author Riccardo Izzo
 */
public class Server {
    private final ServerHandler serverHandler;
    /**
     * List of active connections.
     * It represents all clients actively connected to the server.
     */
    private List<ClientConnection> connections;

    /**
     * Map that associates the player's name with the connection established with his client.
     */
    private Map<String, ClientConnection> connectionMap;

    /**
     * Map that associates a client connection with the lobby id of the game to which it is registered.
     */
    private Map<ClientConnection, String> connectionToLobby;

    /**
     * Map that associates the lobby id with the GameHandler that manages the corresponding game.
     */
    private Map<String, GameHandler> lobbies;

    /**
     * Map that associates the lobby id with the maximum number of players for that lobby.
     */
    private Map<String, Integer> lobbyToNumPlayers;

    /**
     * Constructor Server creates a new Server instance.
     * @param port server port that will be managed by the serverHandler.
     */
    public Server(int port){
        serverHandler = new ServerHandler(this, port);
        connections = new ArrayList<>();
        connectionMap = new HashMap<>();
        connectionToLobby = new HashMap<>();
        lobbies = new HashMap<>();
        lobbyToNumPlayers = new HashMap<>();
    }

    /**
     * Method getServerHandler returns the serverHandler.
     * @return the serverHandler.
     */
    public ServerHandler getServerHandler(){
        return serverHandler;
    }

    /**
     * Method getConnections returns the list of active connections.
     * @return the list of active connections.
     */
    public List<ClientConnection> getConnections() {
        return connections;
    }

    /**
     * Method getGameHandler returns the GameHandler relying on the player's name.
     * @param nickname player's name.
     * @return the GameHandler.
     */
    public GameHandler getGameHandler(String nickname){
        return lobbies.get(connectionToLobby.get(connectionMap.get(nickname)));
    }

    /**
     * Method getLobbyID returns the lobby ID relying on the gameHandler.
     * @param gameHandler is the gameHandler of that lobby.
     * @return the lobby id or an error message.
     */
    public String getLobbyID(GameHandler gameHandler){
//        return lobbies.get(connectionToLobby.get(connectionMap.get(nickname)));
        for (Map.Entry<String, GameHandler> values : lobbies.entrySet()) {
            if(values.getValue().equals(gameHandler)) {
                return values.getKey();
            }
        }
        return "GameHandler not found";
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
     * Method getPlayersNameByLobby returns the list of players that have joined the selected lobby.
     * @param lobbyID the lobby id.
     * @return a list of player's nickname.
     */
    public ArrayList<String> getPlayersNameByLobby(String lobbyID){
        ArrayList<String> players = new ArrayList<>();
        for(String nickname : connectionMap.keySet()){
            if(connectionToLobby.get(getConnectionByPlayerName(nickname)).equals(lobbyID)){
                players.add(nickname);
            }
        }
        return players;
    }

    /**
     * Method createLobby creates a new lobby (managed by a GameHandler) and associates the lobby id with the new GameHandler instance.
     * @param lobbyID lobby id that represents a game.
     */
    public void createLobby(String lobbyID){
        lobbies.put(lobbyID, new GameHandler());
    }

    /**
     * Method isConnected checks if the connection associated with that nickname is currently active.
     * @param nickname player's nickname.
     * @return true if the player's connection is active, false otherwise.
     */
    public boolean isConnected(String nickname){
        return connections.contains(connectionMap.get(nickname));
    }

    /**
     * Method isFull checks if the lobby associated to that id is full.
     * @param lobbyID the lobby id that represents the lobby to check.
     * @return true if the lobby is full, false otherwise.
     */
    public boolean isFull(String lobbyID){
        int numPlayers = 0;
        for(ClientConnection connection : connectionToLobby.keySet()){
            if(connectionToLobby.get(connection).equals(lobbyID)) numPlayers++;
        }
        return numPlayers == lobbyToNumPlayers.get(lobbyID);
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
    }

    /**
     * Method handleMessage handles messages coming from the client.
     * @param connection connection associated to that player.
     * @param message ClientMessage received from the client that needs to be processed.
     */
    public void handleMessage(ClientConnection connection, ClientMessage message){
        /*
        It manages SetNickname message, if the nickname is available it registers the player on the server.
         */
        if(message instanceof SetNickname) {
            /*
            Already exists a player with this nickname, it sends an InvalidNickname message.
             */
            if(connectionMap.containsKey(connection.getNickname())){
                connection.sendToClient(new InvalidNickname());
            }
            /*
            This nickname is available, it registers the player with the selected nickname and sends a ValidNickname message.
             */
            else{
                registerPlayer(((SetNickname) message).getNickname(), connection);
                connection.sendToClient(new ValidNickname());
            }
        }

        /*
        It manages GetLobbies message and sends to the client the list of lobbies.
         */
        else if(message instanceof GetLobbies) {
            connection.sendToClient(new SendLobbies(lobbyToNumPlayers));
        }

        /*
        It manages CreateLobby message.
        If there is no lobby associated to that id it creates a new one.
        Finally it associates the client connection to the lobby he just created.
         */
        else if(message instanceof CreateLobby) {
            String lobbyID = ((CreateLobby) message).getLobbyID();
            /*
            Doesn't exist a lobby with this id, it creates a new one.
             */
            if(!(lobbies.containsKey(lobbyID))){
                createLobby(lobbyID);
                lobbyToNumPlayers.put(lobbyID, ((CreateLobby) message).getNumPlayers());
                connectionToLobby.put(connection, lobbyID);
                connection.sendToClient(new LobbyJoined());
            }
            /*
            Already exists a lobby with this id.
             */
            else {
                connection.sendToClient(new TextMessage("Already exists a lobby with this id! Try again."));
            }
        }

        /*
        It manages JoinLobby message, if the lobby is not full it associates the client connection to the lobby he just joined.
         */
        else if(message instanceof JoinLobby) {
            String lobbyID = ((JoinLobby) message).getLobbyID();
            /*
            Checks if the selected lobby exists.
             */
            if(lobbies.containsKey(lobbyID)){
                /*
                Lobby is full, it sends a LobbyFull message to the player.
                 */
                if(isFull(((JoinLobby) message).getLobbyID())){
                    connection.sendToClient(new LobbyFull());
                }
                /*
                Lobby is not full, it adds the player to the lobby and sends a LobbyJoined message.
                 */
                else {
                    connectionToLobby.put(connection, ((JoinLobby) message).getLobbyID());
                    connection.sendToClient(new LobbyJoined());
                }
            }
            /*
            Can't join a lobby that doesn't exist.
             */
            else {
                connection.sendToClient(new TextMessage("Error, this lobby doesn't exist."));
            }
        }

        else if(message instanceof Disconnection){
            //to implement
        }

        /*
        The other messages are user actions and modify the model, these are handled by the GameHandler.
         */
        else getGameHandler(connection.getNickname()).process(connection.getNickname(), message);
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
