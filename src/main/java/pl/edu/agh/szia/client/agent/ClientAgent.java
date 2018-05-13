package pl.edu.agh.szia.client.agent;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import pl.edu.agh.szia.client.agent.behaviour.ConsoleCyclicBehaviour;
import pl.edu.agh.szia.client.agent.behaviour.SendCommandToServerBehaviour;
import pl.edu.agh.szia.utils.command.CommandMessage;
import pl.edu.agh.szia.utils.command.CommandType;

import java.io.IOException;

public class ClientAgent extends Agent {

    private String username;

    @Override
    protected void setup() {
        Object[] args = getArguments();

        if (args.length > 0 && args[0] instanceof String) {
            username = (String) args[0];
        }

        configureBehaviours();
    }

    private void configureBehaviours() {
        try {
            final ACLMessage signInMessage = signInWithUsername(username);

            addBehaviour(new SendCommandToServerBehaviour(signInMessage));
            addBehaviour(new ConsoleCyclicBehaviour());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ACLMessage signInWithUsername(String username) throws IOException {
        final ACLMessage message = new ACLMessage(ACLMessage.REQUEST);

        CommandMessage commandMessage = new CommandMessage(CommandType.SIGN_IN, username);
        message.setContentObject(commandMessage);
        return message;
    }
}
