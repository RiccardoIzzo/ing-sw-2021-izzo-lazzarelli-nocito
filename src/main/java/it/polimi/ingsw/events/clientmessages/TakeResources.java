package it.polimi.ingsw.events.clientmessages;

import it.polimi.ingsw.model.JsonCardsCreator;
import it.polimi.ingsw.model.MarbleColor;
import it.polimi.ingsw.model.card.WhiteMarbleLeaderCard;

/**
 * TakeResources message is used by the player to take resources from the market inserting the marble at the specific index.
 */
public class TakeResources implements ClientMessage{
    private final int index;
    private final int type;
    private MarbleColor whiteMarbleExchange = MarbleColor.WHITE;

    /**
     * Constructor TakeResources creates a new TakeResources instance.
     * @param index row/column index.
     * @param type user choice: 1 = row, 2 = column.
     */
    public TakeResources(int index, int type){
        this.index = index;
        this.type = type;
    }

    public TakeResources(int index, int type, int whiteMarbleLeaderCard) {
        this.index = index;
        this.type = type;
        this.whiteMarbleExchange = (MarbleColor) ((WhiteMarbleLeaderCard) JsonCardsCreator.generateLeaderCard(whiteMarbleLeaderCard)).getExchange().toArray()[0];
    }

    /**
     * Method getIndex returns the index of the selected row/column:
     * @return the row/column index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Method getType returns the type of user choice.
     * @return the user choice.
     */
    public int getType() {
        return type;
    }

    public MarbleColor getWhiteMarbleExchange() {
        return whiteMarbleExchange;
    }
}
