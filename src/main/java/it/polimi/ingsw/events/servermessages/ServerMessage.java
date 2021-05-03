package it.polimi.ingsw.events.servermessages;

import java.io.Serializable;

/**
 * ServerMessage interface defines a generic message sent by the server to the client.
 * It implements Serializable interface that allows the message to be sent over the network.
 * Every message that implements this interface represents a specific server action.
 *
 * @author Riccardo Izzo
 */
public interface ServerMessage extends Serializable {}
