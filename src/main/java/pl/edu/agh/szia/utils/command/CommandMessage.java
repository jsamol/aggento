package pl.edu.agh.szia.utils.command;

import java.io.Serializable;

public class CommandMessage implements Serializable {
    private final CommandType type;
    private final String[] arguments;

    public CommandMessage(CommandType type, String... arguments) {
        this.type = type;
        this.arguments = arguments;
    }

    public CommandType getType() {
        return type;
    }

    public String[] getArguments() {
        return arguments;
    }
}
