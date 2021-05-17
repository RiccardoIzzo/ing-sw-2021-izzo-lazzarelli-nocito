package it.polimi.ingsw.events.servermessages;

public class UpdateView implements ServerMessage {
    private String sourcePlayer;
    private String propertyName;
    private Object oldValue;
    private Object newValue;

    public UpdateView(String sourcePlayer, String propertyName, Object oldValue, Object newValue) {
        this.sourcePlayer = sourcePlayer;
        this.propertyName = propertyName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getSourcePlayer() {
        return sourcePlayer;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }
}
