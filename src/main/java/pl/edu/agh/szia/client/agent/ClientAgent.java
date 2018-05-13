package pl.edu.agh.szia.client.agent;

import jade.core.Agent;
import pl.edu.agh.szia.client.agent.behaviour.ConsoleCyclicBehaviour;

public class ClientAgent extends Agent {

    private String username;

    @Override
    protected void setup() {
        Object[] args = getArguments();

        if (args.length > 0 && args[0] instanceof String) {
            username = (String) args[0];
        }

        addBehaviour(new ConsoleCyclicBehaviour(username));
    }
}
