package it.polimi.ingsw.events.servermessages;

/**
 * UpdateView message is used to update the ModelView, a client-side simplified representation of the model.
 * This message is created by the listeners and sent by the VirtualView to the client where it is managed by the ModelViewUpdater.
 */
public class UpdateView implements ServerMessage {
    private final String sourcePlayer;
    private final String propertyName;
    private final Object oldValue;
    private final Object newValue;

    /**
     * Constructor UpdateView creates a new UpdateView instance.
     * @param sourcePlayer message source, the player who started the UpdateView message.
     * @param propertyName label which uniquely identifies the type of update.
     * @param oldValue old value of the property.
     * @param newValue new value of the property.
     */
    public UpdateView(String sourcePlayer, String propertyName, Object oldValue, Object newValue) {
        this.sourcePlayer = sourcePlayer;
        this.propertyName = propertyName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * Method getSourcePlayer returns the player source
     * @return the player source.
     */
    public String getSourcePlayer() {
        return sourcePlayer;
    }

    /**
     * Method getPropertyName returns the property name.
     * @return the property name.
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Method getOldValue returns the old value of the property.
     * @return the old value of the property.
     */
    public Object getOldValue() {
        return oldValue;
    }

    /**
     * Method getNewValue returns the new value of the property.
     * @return the new value of the property.
     */
    public Object getNewValue() {
        return newValue;
    }
}
