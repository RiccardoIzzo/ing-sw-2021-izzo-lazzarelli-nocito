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
    Boolean[] tilesUncovered;
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Constructor FaithTrack creates a new FaithTrack instance.
     */
    public FaithTrack() {
        posFaithMarker = 0;
        popesFavorTiles = new Boolean[3];
        Arrays.fill(popesFavorTiles, false);

    }

    /**
     * Method getPlayerPosition returns the player's position on the faith track.
     * @return the player's position.
     */
    public int getPlayerPosition() {
        return posFaithMarker;
    }

    /**
     * Method getTilesUncovered returns an array of Boolean that indicates which tiles have been uncovered.
     * @return the array of Boolean.
     */
    public Boolean[] getTilesUncovered() {
        return tilesUncovered;
    }

    /**
     * Method getPopesFavorTiles returns an array of Boolean that indicates which Pope's favor tiles the player has obtained.
     * @return the array of Boolean.
     */
    public Boolean[] getPopesFavorTiles() {
        return popesFavorTiles;
    }

    /**
     * Method setTilesUncovered sets tilesUncovered, the array of Boolean.
     * @param tilesUncovered array of Boolean.
     */
    public void setTilesUncovered(Boolean[] tilesUncovered) {
        this.tilesUncovered = tilesUncovered;
    }

    /**
     * Method moveForward increments the faith marker and calls popeTilePass if the player has reached a Pope space.
     * @return false if the player hasn't reached a Pope space, otherwise returns the result of popeTilePass method.
     */
    public boolean moveForward() {
        if (posFaithMarker < END_TILE) {
            posFaithMarker++;
            pcs.firePropertyChange(FAITH_MARKER_POSITION, null, posFaithMarker);
        }
        if(TILE_POS.contains(posFaithMarker)) return popeTilePass();
        else return false;
    }

    /**
     * Method isInVaticanSpace checks if the position is eligible for the pope tile points.
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
     * Method popeTilePass uncovers the tile related to the player position, if this tile has already been uncovered.
     * @return true if the tile has been uncovered successfully, false otherwise.
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
     * Method getPointsForTiles returns the number of victory points earned with the tiles.
     * @return the number of victory points.
     */
    public int getPointsForTiles() {
        int counter = 0;
        for (int i = 0; i < popesFavorTiles.length; i++) {
            if(popesFavorTiles[i] != null) counter += popesFavorTiles[i] ? POINTS_FOR_TILE[i] : 0;
        }
        return counter;
    }

    /**
     * Method getPosVictoryPoints returns the number of victory points based on the faith marker position.
     * @return the number of victory points.
     */
    public int getPosVictoryPoints() {
        int victoryPoints = 0;
        int i = 0;
        while (i < WINNING_TILES.length && posFaithMarker >= WINNING_TILES[i]) {
            victoryPoints = WINNING_VALUES[i];
            i++;
        }
        return victoryPoints;
    }

    /**
     * Method setPropertyChangeSupport sets a PropertyChangeSupport for this FaithTrack,
     * it is called by the Dashboard class which passes its own PropertyChangeSupport
     * @param pcs, the PropertyChangeSupport of the Dashboard who owns this FaithTrack
     */
    public void setPropertyChangeSupport(PropertyChangeSupport pcs) {
        this.pcs = pcs;
    }

    /**
     * Method addPropertyListener register a FaithTrackListener to the PropertyChangeSupport of this class.
     * @param virtualView the VirtualView used to forward messages to the players.
     */
    public void addPropertyListener(VirtualView virtualView){
        FaithTrackListener faithTrackListener = new FaithTrackListener(virtualView);
        pcs.addPropertyChangeListener(FAITH_MARKER_POSITION, faithTrackListener);
        pcs.addPropertyChangeListener(BLACK_MARKER_POSITION, faithTrackListener);
        pcs.addPropertyChangeListener(POPES_TILES_CHANGE, faithTrackListener);
    }
}