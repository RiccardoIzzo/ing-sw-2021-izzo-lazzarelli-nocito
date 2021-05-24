package it.polimi.ingsw.model.player;

import it.polimi.ingsw.listeners.FaithTrackListener;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeSupport;
import java.util.Arrays;

import static it.polimi.ingsw.constants.PlayerConstants.*;

/**
 * FaithTrack Class represents a player's faith track
 *
 * @author Andrea Nocito
 */
public class FaithTrack {
    private int posFaithMarker;
    private final Boolean[] popesFavorTiles;
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    // tilesUncovered: Value that states if the pope tiles have been uncovered
    private boolean[] tilesUncovered = {false, false, false};

    public FaithTrack() {
        posFaithMarker = 0;
        popesFavorTiles = new Boolean[3];
        Arrays.fill(popesFavorTiles, false);

    }
    public boolean getTilesUncovered(int index) {
        if (index < tilesUncovered.length) {
            return tilesUncovered[index];
        }
        return false;
    }

    public boolean setTilesUncovered(int index, boolean value) {
        if (index < tilesUncovered.length) {
            tilesUncovered[index] = value;
            return true;
        }
        return false;
    }

    /**
     * Method moveForward moves the faith marker and calls popeTilePass
     */
    public void moveForward() {
        posFaithMarker++;
        pcs.firePropertyChange(FAITH_MARKER_POSITION, posFaithMarker-1, posFaithMarker);
        popeTilePass();
    }

    /**
     * Method isInVaticanSpace checks if the position is eligible for the pope tile points
     */
    public void isInVaticanSpace(Integer space) {
        if (space < popesFavorTiles.length) {
            popesFavorTiles[space] = posFaithMarker <= TILE_POS[space] && posFaithMarker >= TILE_POS[space] - INITIAL_OFFSET - space;
            pcs.firePropertyChange(POPES_TILES_CHANGE, null, popesFavorTiles.clone());
        }
    }

    /**
     * Method popeTilePass checks if the position is the same as a tile pass
     */
    public void popeTilePass() {
        for(int i = 0; i < TILE_POS.length; i++) {
            if ( posFaithMarker == TILE_POS[i] && !tilesUncovered[i] ) {
                popesFavorTiles[i] = true;
                tilesUncovered[i] = true;
            }
            pcs.firePropertyChange(POPES_TILES_CHANGE, null, popesFavorTiles.clone());
        }
    }

    /**
     * Method getPointsForTiles
     * @return the number of points earned by the unlocked pope tiles
     */
    public int getPointsForTiles() {
        int counter = 0;
        for (int i = 0; i < popesFavorTiles.length; i++) {
            counter += popesFavorTiles[i] ? POINTS_FOR_TILE[i] : 0;
        }
        return counter;
    }

    /**
     * Method getPosVictoryPoints
     * @return the number of points earned by the player's position on the faith track and the unlocked pope tiles
     */
    public int getPosVictoryPoints() {
        int victoryPoints = 0;
        int i = 0;
        while (i< WINNING_TILES.length && posFaithMarker >= WINNING_TILES[i]) {
            victoryPoints += WINNING_VALUES[i];
            i++;
        }
        return victoryPoints+getPointsForTiles();
    }

    public int getPlayerPos() {
        return posFaithMarker;
    }

    /**
     * Method setPropertyChangeSupport sets a PropertyChangeSupport for this FaithTrack,
     * it is called by the Dashboard class which passes its own PropertyChangeSupport
     * @param pcs, the PropertyChangeSupport of the Dashboard who owns this FaithTrack
     */
    public void setPropertyChangeSupport(PropertyChangeSupport pcs) {
        this.pcs = pcs;
    }

    public void addPropertyListener(VirtualView virtualView){
        FaithTrackListener faithTrackListener = new FaithTrackListener(virtualView);
        pcs.addPropertyChangeListener(FAITH_MARKER_POSITION, faithTrackListener);
        pcs.addPropertyChangeListener(BLACK_MARKER_POSITION, faithTrackListener);
        pcs.addPropertyChangeListener(POPES_TILES_CHANGE, faithTrackListener);
    }
}