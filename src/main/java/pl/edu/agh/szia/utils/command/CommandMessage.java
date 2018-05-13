package pl.edu.agh.szia.utils.command;

import java.io.Serializable;

public class CommandMessage implements Serializable {
    private final CommandType type;
    private final String argument;

    public CommandMessage(CommandType type) {
        this(type, null);
    }

    public CommandMessage(CommandType type, String argument) {
        this.type = type;
        this.argument = argument;
    }

    public CommandType getType() {
        return type;
    }

    public String getArguments() {
        return argument;
    }
}
