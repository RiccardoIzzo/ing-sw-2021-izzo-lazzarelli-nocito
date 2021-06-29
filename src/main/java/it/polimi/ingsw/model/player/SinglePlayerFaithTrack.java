package it.polimi.ingsw.model.player;

import it.polimi.ingsw.network.VirtualView;

import static it.polimi.ingsw.constants.PlayerConstants.*;

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
    public boolean moveBlackFaithMarker() {
        if (blackFaithMarker < END_TILE) {
            blackFaithMarker++;
            pcs.firePropertyChange(BLACK_MARKER_POSITION, null, blackFaithMarker);
        }
        if(TILE_POS.contains(blackFaithMarker)) return blackPopeTilePass();
        else return false;
    }

    /**
     * Method blackPopeTilePass uncovers the tile related to Lorenzo's position, if this tile has already been uncovered.
     * @return true if the tile has been uncovered successfully, false otherwise.
     */
    public boolean blackPopeTilePass() {
        int index = TILE_POS.indexOf(blackFaithMarker);
        if (!tilesUncovered[index]) {
            tilesUncovered[index] = true;
            pcs.firePropertyChange(TILES_UNCOVERED_CHANGE, null, tilesUncovered.clone());
            return true;
        }
        return false;
    }

    /**
     * Method addPropertyListener calls the addPropertyListener of the FaithTrack superclass.
     * @param virtualView the VirtualView used to forward messages to the players.
     */
    @Override
    public void addPropertyListener(VirtualView virtualView) {
        super.addPropertyListener(virtualView);
    }
}

