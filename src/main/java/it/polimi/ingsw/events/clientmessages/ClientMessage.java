package it.polimi.ingsw.events.clientmessages;

import java.io.Serializable;

/**
 * ClientMessage interface defines a generic message sent by the client to the server.
 * It implements Serializable interface that allows the message to be sent over the network.
 * Every message that implements this interface represents a specific client action.
 *
 * @author Riccardo Izzo
 */
public interface ClientMessage extends Serializable {}
