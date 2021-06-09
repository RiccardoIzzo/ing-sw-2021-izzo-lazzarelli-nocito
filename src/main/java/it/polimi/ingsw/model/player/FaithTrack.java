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
    private Boolean[] tilesUncovered;
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);


    public FaithTrack() {
        posFaithMarker = 0;
        popesFavorTiles = new Boolean[3];
        Arrays.fill(popesFavorTiles, false);

    }
    public Boolean[] getTilesUncovered() {
        return tilesUncovered;
    }

    public Boolean[] getPopesFavorTiles() {
        return popesFavorTiles;
    }

    public void setTilesUncovered(Boolean[] tilesUncovered) {
        this.tilesUncovered = tilesUncovered;
    }

    /**
     * Method moveForward moves the faith marker and calls popeTilePass
     */
    public boolean moveForward() {
        posFaithMarker++;
        pcs.firePropertyChange(FAITH_MARKER_POSITION, null, posFaithMarker);
        if(TILE_POS.contains(posFaithMarker)) return popeTilePass();
        else return false;
    }

    /**
     * Method isInVaticanSpace checks if the position is eligible for the pope tile points
     */
    public void isInVaticanSpace() {
        int lastTileUncovered = 0;
        while (tilesUncovered[lastTileUncovered + 1]) {
            lastTileUncovered++;
            if(lastTileUncovered == 2) break;
        }
        switch (lastTileUncovered){
            case 0 -> {
                if(posFaithMarker >= 5) popesFavorTiles[lastTileUncovered] = true;
                else popesFavorTiles[lastTileUncovered] = null;
            }
            case 1 -> {
                if(posFaithMarker >= 12) popesFavorTiles[lastTileUncovered] = true;
                else popesFavorTiles[lastTileUncovered] = null;
            }
            case 2 -> {
                if(posFaithMarker >= 19) popesFavorTiles[lastTileUncovered] = true;
                else popesFavorTiles[lastTileUncovered] = null;
            }
        }
        pcs.firePropertyChange(POPES_TILES_CHANGE, null, popesFavorTiles.clone());
    }

    /**
     * Method popeTilePass checks if the position is the same as a tile pass
     */
    public boolean popeTilePass() {
        int index = TILE_POS.indexOf(posFaithMarker);
        if (!tilesUncovered[index]) {
            tilesUncovered[index] = true;
            pcs.firePropertyChange(TILES_UNCOVERED_CHANGE, null, tilesUncovered.clone());
            return true;
        }
        return false;
    }

    /**
     * Method getPointsForTiles
     * @return the number of points earned by the unlocked pope tiles
     */
    public int getPointsForTiles() {
        int counter = 0;
        for (int i = 0; i < popesFavorTiles.length; i++) {
            if(popesFavorTiles[i] != null) counter += popesFavorTiles[i] ? POINTS_FOR_TILE[i] : 0;
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
        while (i < WINNING_TILES.length && posFaithMarker >= WINNING_TILES[i]) {
            victoryPoints += WINNING_VALUES[i];
            i++;
        }
        return victoryPoints;
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