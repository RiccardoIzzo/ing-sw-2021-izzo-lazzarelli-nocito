package it.polimi.ingsw.listeners;
import it.polimi.ingsw.network.VirtualView;
import java.beans.PropertyChangeEvent;

public class StrongboxListener extends Listener{
    public StrongboxListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //new message
        //send message
    }
}