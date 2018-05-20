package pl.edu.agh.szia.utils.command;

import java.io.Serializable;

public class NotificationMessage implements Serializable {
    private final NotificationType type;
    private final String message;

    public NotificationMessage(NotificationType type, String message) {
        this.type = type;
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
