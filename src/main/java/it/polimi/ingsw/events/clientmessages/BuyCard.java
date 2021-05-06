package it.polimi.ingsw.events.clientmessages;

/**
 * BuyCard message is used by the player to buy a development card.
 */
public class BuyCard implements ClientMessage{
    private final int row;
    private final int column;

    /**
     * Constructor BuyCard creates a new BuyCard instance.
     * @param row row index of the card.
     * @param column column index of the card.
     */
    public BuyCard(int row, int column){
        this.row = row;
        this.column = column;
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
}
