package it.polimi.ingsw.model.player;

import it.polimi.ingsw.network.VirtualView;

import static it.polimi.ingsw.constants.PlayerConstants.BLACK_MARKER_POSITION;

/**
 * SinglePlayerFaithTrack Class represents a player's faith track in single player mode
 *
 * @author Andrea Nocito
 */
public class SinglePlayerFaithTrack extends FaithTrack {
    private int blackFaithMarker;

    public SinglePlayerFaithTrack() {
        super();
        blackFaithMarker = 0;
    }

    public int getBlackFaithMarker() {
        return blackFaithMarker;
    }

    public void moveBlack() {
        blackFaithMarker++;
        pcs.firePropertyChange(BLACK_MARKER_POSITION, null, blackFaithMarker);
    }

    @Override
    public void addPropertyListener(VirtualView virtualView) {
        super.addPropertyListener(virtualView);
    }
}

