package it.polimi.ingsw.events.servermessages;

/**
 * TextMessage message is used to send a generic text message.
 */
public class TextMessage implements ServerMessage{
    private final String text;

    /**
     * Constructor TextMessage creates a new TextMessage instance.
     * @param text text message.
     */
    public TextMessage(String text){
        this.text = text;
    }

    /**
     * Method getText returns the text field.
     * @return the text field.
     */
    public String getText(){
        return text;
    }
}
