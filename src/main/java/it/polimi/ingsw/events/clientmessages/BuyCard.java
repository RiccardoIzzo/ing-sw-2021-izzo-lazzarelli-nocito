package it.polimi.ingsw.events.clientmessages;

/**
 * BuyCard message is used by the player to buy a development card.
 */
public class BuyCard implements ClientMessage{
    private final int row;
    private final int column;
    private final int index;

    /**
     * Constructor BuyCard creates a new BuyCard instance.
     * @param row row index of the card.
     * @param column column index of the card.
     * @param index slot index in activeDevelopments.
     */
    public BuyCard(int row, int column, int index){
        this.row = row;
        this.column = column;
        this.index = index;
    }

    /**
     * Method getRow returns the row index of the card.
     * @return the row index.
     */
    public int getRow() {
        return row;
    }

    /**
     * Method getColumn returns the column index of the card.
     * @return the column index.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Method getIndex returns the slot index in activeDevelopments.
     * @return the slot index where you want to place the card just bought.
     */
    public int getIndex() {
        return index;
    }
}
