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
     * List of player usernames that are registered on the server.
     */
    private final List<String> users;

    /**
     * Map that associates the player's name with the connection established with his client.
     */
    private final Map<String, ClientConnection> connectionMap;

    /**
     * Map that associates the player's name with the lobby id of the game to which it is registered.
     */
    private final Map<String, String> playerToLobby;

    /**
     * Map that associates the lobby id with the GameHandler that manages the corresponding game.
     */
    private final Map<String, GameHandler> lobbies;

    /**
     * Map that associates the lobby id with the maximum number of players for that lobby.
     */
    private final Map<String, Integer> lobbyToNumPlayers;

    /**
     * Constructor Server creates a new Server instance.
     * @param port server port that will be managed by the serverHandler.
     */
    public Server(int port){
        serverHandler = new ServerHandler(this, port);
        users = new ArrayList<>();
        connectionMap = new HashMap<>();
        playerToLobby = new HashMap<>();
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
     * Method getGameHandler returns the GameHandler relying on the player's name.
     * @param nickname player's name.
     * @return the GameHandler.
     */
    public synchronized GameHandler getGameHandler(String nickname){
        return lobbies.get(playerToLobby.get(nickname));
    }

    /**
     * Method getLobbyID returns the lobby id relying on the gameHandler.
     * @param gameHandler is the GameHandler of that lobby.
     * @return the lobby id or null.
     */
    public String getLobbyID(GameHandler gameHandler){
        for (String lobbyID : lobbies.keySet()) {
            if(lobbies.get(lobbyID).equals(gameHandler)) return lobbyID;
        }
        return null;
    }

    /**
     * Method getLobbyIDByPlayer returns the lobby id of the game the player participates.
     * @param nickname player's name.
     * @return the lobby id.
     */
    public String getLobbyIDByPlayerName(String nickname) {
        return playerToLobby.get(nickname);
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
        for(String nickname : playerToLobby.keySet()){
            if(getLobbyIDByPlayerName(nickname).equals(lobbyID)){
                players.add(nickname);
            }
        }
        return players;
    }

    /**
     * Method getActivePlayersByLobby returns the list of active players that have joined the selected lobby.
     * @param lobbyID the lobby id.
     * @return a list of player's nickname.
     */
    public synchronized ArrayList<String> getActivePlayersByLobby(String lobbyID){
        ArrayList<String> players = new ArrayList<>();
        for(String nickname : connectionMap.keySet()){
            if(playerToLobby.get(nickname).equals(lobbyID)){
                players.add(nickname);
            }
        }
        return players;
    }

    /**
     * Method createLobby creates a new lobby (managed by a GameHandler) and associates the lobby id with the new GameHandler instance.
     * @param lobbyID lobby id that represents a game.
     * @param numPlayers maximum number of players for that lobby.
     */
    public synchronized void createLobby(String lobbyID, int numPlayers){
        lobbies.put(lobbyID, new GameHandler(this, lobbyID));
        lobbyToNumPlayers.put(lobbyID, numPlayers);
    }

    /**
     * Method isConnected checks if the connection associated with that nickname is currently active.
     * @param nickname player's nickname.
     * @return true if the player's connection is active, false otherwise.
     */
    public boolean isConnected(String nickname){
        return connectionMap.containsKey(nickname);
    }

    /**
     * Method isFull checks if the lobby associated to that id is full.
     * @param lobbyID the lobby id that represents the lobby to check.
     * @return true if the lobby is full, false otherwise.
     */
    public boolean isFull(String lobbyID){
        int numPlayers = 0;
        for(String nickname : playerToLobby.keySet()){
            if(playerToLobby.get(nickname).equals(lobbyID)) numPlayers++;
        }
        return numPlayers == lobbyToNumPlayers.get(lobbyID);
    }

    /**
     * Method registerPlayer adds a new connection to the map by associating it with the player's name.
     * @param name the player's nickname.
     * @param connection the connection associated with that player nickname.
     */
    public synchronized void registerPlayer(String name, ClientConnection connection){
        users.add(name);
        connectionMap.put(name, connection);
    }

    /**
     * Method sendEveryone sends a message to every active client connection.
     * @param message the message to send.
     */
    public void sendEveryone(ServerMessage message, String lobbyID){
        for(String nickname : getActivePlayersByLobby(lobbyID)){
                getConnectionByPlayerName(nickname).sendToClient(message);
        }
    }

    /**
     * Method handleMessage handles messages coming from the client.
     * @param connection connection associated to that player.
     * @param message ClientMessage received from the client that needs to be processed.
     */
    public void handleMessage(ClientConnection connection, ClientMessage message){
        String nickname = connection.getNickname();
        /*
        It manages SetNickname message, if the nickname is available it registers the player on the server.
         */
        if(message instanceof SetNickname) {
            /*
            Already exists a player with this nickname and is actively playing, it sends an InvalidNickname message.
             */
            if(users.contains(nickname) && connectionMap.containsKey(nickname)){
                connection.sendToClient(new InvalidNickname());
            }
            /*
            Reconnection, the player is registered but is not actively playing.
             */
            else if(users.contains(nickname) && !connectionMap.containsKey(nickname)){
                connectionMap.put(nickname, connection);
                //to implement, reload the game for that player
            }
            /*
            This nickname is available, it registers the player with the selected nickname and sends a ValidNickname message.
             */
            else{
                registerPlayer(nickname, connection);
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
            int numPlayers = ((CreateLobby) message).getNumPlayers();
            /*
            Doesn't exist a lobby with this id, it creates a new one.
             */
            if(!(lobbies.containsKey(lobbyID))){
                createLobby(lobbyID, numPlayers);
                playerToLobby.put(nickname, lobbyID);
                lobbies.get(lobbyID).setGameMode(numPlayers);
                connection.sendToClient(new LobbyJoined());
                //to implement: single player game
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
                    playerToLobby.put(nickname, lobbyID);
                    connection.sendToClient(new LobbyJoined());
                    if(isFull(lobbyID)) getGameHandler(nickname).start();
                }
            }
        }

        /*
        It manages Disconnection message.
        When an error occurs in the receiveFromClient method in ClientConnection it means that a disconnection has occurred,
        the connection is removed from the list of active connections.
         */
        else if(message instanceof Disconnection){
            connectionMap.remove(nickname);
        }

        /*
        The other messages are user actions and modify the model, these are handled by the GameHandler.
         */
        else getGameHandler(nickname).process(connection.getNickname(), message);
    }

    /**
     * Server main method.
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
        (new Thread(server.serverHandler)).start();
    }
}
