package it.polimi.ingsw.model.player;

import it.polimi.ingsw.constants.FaithTrackConstants;
import it.polimi.ingsw.listeners.FaithTrackListener;
import it.polimi.ingsw.listeners.LeaderCardListener;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeSupport;
import java.util.Arrays;

/**
 * FaithTrack Class represents a player's faith track
 *
 * @author Andrea Nocito
 */
public class FaithTrack {
    private int posFaithMarker;
    private final Boolean[] popesFavorTiles;

    private String POS_FAITH_MARKER = "posFaithMarker"; //property name
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);


    // tilesUncovered: Value that states if the pope tiles have been uncovered
    private boolean[] tilesUncovered = {false, false, false};

    public void addListener(VirtualView virtualView){
        pcs.addPropertyChangeListener(POS_FAITH_MARKER, new FaithTrackListener(virtualView));
    }

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
    public int getEnd() {
        return FaithTrackConstants.END;
    }
    /**
     * Method moveForward moves the faith marker and calls popeTilePass
     */
    public void moveForward() {
        posFaithMarker++;
//        pcs.firePropertyChange(POS_FAITH_MARKER, posFaithMarker-1, posFaithMarker);
        popeTilePass();
    }

    /**
     * Method isInVaticanSpace checks if the position is eligible for the pope tile points
     */
    public void isInVaticanSpace(Integer space) {
        if (space < popesFavorTiles.length) {
            popesFavorTiles[space] = posFaithMarker <= FaithTrackConstants.TILE_POS[space] && posFaithMarker >= FaithTrackConstants.TILE_POS[space] - FaithTrackConstants.INITIAL_OFFSET - space;
        }
    }

    /**
     * Method popeTilePass checks if the position is the same as a tile pass
     */
    public void popeTilePass() {
        for(int i = 0; i < FaithTrackConstants.TILE_POS.length; i++) {
            if ( posFaithMarker == FaithTrackConstants.TILE_POS[i] && !tilesUncovered[i] ) {
                popesFavorTiles[i] = true;
                tilesUncovered[i] = true;
            }
        }
    }

    /**
     * Method getPointsForTiles
     * @return the number of points earned by the unlocked pope tiles
     */
    public int getPointsForTiles() {
        int counter = 0;
        for (int i = 0; i < popesFavorTiles.length; i++) {
            counter += popesFavorTiles[i] ? FaithTrackConstants.POINTS_FOR_TILE[i] : 0;
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
        while (i< FaithTrackConstants.WINNING_TILES.length && posFaithMarker >= FaithTrackConstants.WINNING_TILES[i]) {
            victoryPoints += FaithTrackConstants.WINNING_VALUES[i];
            i++;
        }
        return victoryPoints+getPointsForTiles();
    }

    public int getPlayerPos() {
        return posFaithMarker;
    }

}