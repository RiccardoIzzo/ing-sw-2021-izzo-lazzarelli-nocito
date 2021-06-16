package it.polimi.ingsw.model.player;

import it.polimi.ingsw.network.VirtualView;

import static it.polimi.ingsw.constants.PlayerConstants.BLACK_MARKER_POSITION;

/**
 * SinglePlayerFaithTrack class represents a player's faith track in single player mode.
 *
 * @author Andrea Nocito
 */
public class SinglePlayerFaithTrack extends FaithTrack {
    private int blackFaithMarker;

    /**
     * Constructor SinglePlayerFaithTrack creates a new SinglePlayerFaithTrack instance.
     */
    public SinglePlayerFaithTrack() {
        super();
        blackFaithMarker = 0;
    }

    /**
     * Method getBlackFaithMarker returns the position of the black faith marker on the faith track.
     * @return the position.
     */
    public int getBlackFaithMarker() {
        return blackFaithMarker;
    }

    /**
     * Method moveBlackFaithMarker increments the position of the black faith marker.
     */
    public void moveBlackFaithMarker() {
        blackFaithMarker++;
        pcs.firePropertyChange(BLACK_MARKER_POSITION, null, blackFaithMarker);
    }

    @Override
    public void addPropertyListener(VirtualView virtualView) {
        super.addPropertyListener(virtualView);
    }
}

