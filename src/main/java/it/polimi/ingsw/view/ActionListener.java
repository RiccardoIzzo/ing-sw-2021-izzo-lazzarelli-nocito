package it.polimi.ingsw.view;

import it.polimi.ingsw.network.NetworkHandler;

/**
 * ActionListener class manages the user actions.
 */
public class ActionListener {
    private final NetworkHandler network;

    public ActionListener(NetworkHandler network){
        this.network = network;
    }
}
