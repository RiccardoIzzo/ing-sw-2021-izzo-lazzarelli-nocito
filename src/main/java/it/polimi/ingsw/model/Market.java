package it.polimi.ingsw.model;

import it.polimi.ingsw.constants.GameConstants;
import it.polimi.ingsw.listeners.MarketListener;
import it.polimi.ingsw.network.VirtualView;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;

import static it.polimi.ingsw.constants.GameConstants.MARKET_CHANGE;
import static it.polimi.ingsw.constants.GameConstants.SLIDE_MARBLE;

/**
 * Market class represents the market board where the player can buy resources.
 *
 * @author Riccardo Izzo
 */
public class Market {
    private final ArrayList<MarbleColor> marketTray;
    private MarbleColor slideMarble;
    private MarbleColor specialMarble;
    private final ResourceMap outputMarket;
    private boolean foundFaith;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Constructor Market creates a new Market instance.
     */
    public Market(){
        marketTray = new ArrayList<>();
        specialMarble = null;
        outputMarket = new ResourceMap();
    }

    /**
     * Method getSlideMarble returns the marble that can slide, the one outside the market tray.
     * @return the marble.
     */
    public MarbleColor getSlideMarble(){
        return slideMarble;
    }

    /**
     * Method getMarble returns the marble of the the market tray at the specific index.
     * @param index marble position in the market tray.
     * @return the marble.
     */
    public MarbleColor getMarble(int index){
        return marketTray.get(index);
    }

    /**
     * Method getFaith returns a boolean that indicates if in the last turn a red marble was collected.
     * @return true if faith was collected in the last turn.
     */
    public boolean findFaith(){
        return foundFaith;
    }

    /**
     * Method setSpecialMarble updates the variable "specialMarble".
     * @param marble the new specialMarble.
     */
    public void setSpecialMarble(MarbleColor marble){
        this.specialMarble = marble;
    }

    /**
     * Method getSpecialMarble returns the marble into which the white marbles will be converted.
     * @return the specialMarble.
     */
    public MarbleColor getSpecialMarble(){
        return specialMarble;
    }

    /**
     * Method generateTray creates the market tray with 12 marbles randomly generated and select the marble that can slide.
     */
    public void generateTray(){
        for(int n_white = 0; n_white < GameConstants.NUM_WHITE_MARBLES; n_white++){
            marketTray.add(MarbleColor.WHITE);
        }
        for(int n_color = 0; n_color < GameConstants.NUM_MARBLES; n_color++){
            marketTray.add(MarbleColor.BLUE);
            marketTray.add(MarbleColor.GRAY);
            marketTray.add(MarbleColor.YELLOW);
            marketTray.add(MarbleColor.PURPLE);
        }
        marketTray.add(MarbleColor.RED);

        Collections.shuffle(marketTray);
        slideMarble = marketTray.remove(0);
        pcs.firePropertyChange(MARKET_CHANGE, null, marketTray);
        pcs.firePropertyChange(SLIDE_MARBLE, null, slideMarble);
    }

    /**
     * Method reset flush the outputMarket and sets the default values to specialMarble and foundFaith.
     */
    public void reset(){
        outputMarket.flush();
        specialMarble = null;
        foundFaith = false;
    }

    /**
     * Method insertMarble takes all the resources of the selected row/column.
     * @param pos row/column index.
     * @param type represent the user choice: 1 = row, 2 = column.
     */
    public void insertMarble(int pos, int type){
        if(type == 1) selectRow(pos - 1);
        else if(type == 2) selectCol(pos - 1);

        pcs.firePropertyChange(MARKET_CHANGE, null, marketTray);
        pcs.firePropertyChange(SLIDE_MARBLE, null, slideMarble);
    }

    /**
     * Method selectRow push the row in order and insert the slideMarble in the market tray.
     * @param n row index.
     */
    public void selectRow(int n){
        MarbleColor temp = slideMarble;
        int offset = n * 4;

        for(int i = 0; i < 4; i++){
            resourceConverter(getMarble(offset + i));
        }

        slideMarble = getMarble(offset);
        marketTray.set(offset, getMarble(offset + 1));
        marketTray.set(offset + 1, getMarble(offset + 2));
        marketTray.set(offset + 2, getMarble(offset + 3));
        marketTray.set(offset + 3, temp);
    }

    /**
     * Method selectRow push the column in order and insert the slideMarble in the market tray.
     * @param n column index.
     */
    public void selectCol(int n){
        MarbleColor temp = slideMarble;

        for(int i = 0; i < 3; i++){
            resourceConverter(getMarble(n + (i * 4)));
        }

        slideMarble = getMarble(n + 8);
        marketTray.set(n + 8, getMarble(n + 4));
        marketTray.set(n + 4, getMarble(n));
        marketTray.set(n, temp);
    }

    /**
     * Method resourceOutput returns a ResourceMap with the amount of taken resources.
     * @return a ResourceMap.
     */
    public ResourceMap resourceOutput(){
        return outputMarket;
    }

    /**
     * Method resourceConverter converts the color of the marble to the corresponding resource type and adds it to the ResourceMap.
     * @param marble marble color.
     */
    public void resourceConverter(MarbleColor marble){
        if(marble == MarbleColor.BLUE) outputMarket.modifyResource(Resource.SHIELD, 1);
        else if(marble == MarbleColor.GRAY) outputMarket.modifyResource(Resource.STONE, 1);
        else if(marble == MarbleColor.YELLOW) outputMarket.modifyResource(Resource.COIN, 1);
        else if(marble == MarbleColor.PURPLE) outputMarket.modifyResource(Resource.SERVANT, 1);
        else if(marble == MarbleColor.RED) foundFaith = true;
        else if(marble == MarbleColor.WHITE) {
            if(getSpecialMarble() != null){
                resourceConverter(getSpecialMarble());
            }
        }
    }

    public void addPropertyListener(VirtualView virtualView) {
        pcs.addPropertyChangeListener(new MarketListener(virtualView));
    }
}
