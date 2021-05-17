package it.polimi.ingsw.network;

import it.polimi.ingsw.events.servermessages.ServerMessage;

/**
 * VirtualView class is a representation of a virtual client.
 * It manages the ServerMessage prepared by the listeners and sends it to the client.
 *
 * @author Riccardo Izzo, Gabriele Lazzarelli
 */
public class VirtualView{
    private final Server server;
    private final String lobbyID;

    /**
     * Constructor VirtualView creates a new VirtualView instance.
     * @param server server reference.
     * @param lobbyID is the name of the lobby
     */
    public VirtualView(Server server, String lobbyID) {
        this.server = server;
        this.lobbyID = lobbyID;
    }

    /**
     * Method sendToPlayer sends a ServerMessage to the client.
     * @param message ServerMessage to be sent to the client.
     */
    public void sendToPlayer(String nickname, ServerMessage message){
        server.getConnectionByPlayerName(nickname).sendToClient(message);
    }

    /**
     * Method sendToEveryone sends a ServerMessage to every active client of the lobby.
     * @param message ServerMessage to be sent to the client.
     */
    public void sendToEveryone(ServerMessage message){
        server.sendEveryone(message, lobbyID);
    }
}
