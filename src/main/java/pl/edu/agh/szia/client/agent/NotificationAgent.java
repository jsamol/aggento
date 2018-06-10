package pl.edu.agh.szia.client.agent;

import jade.core.Agent;
import pl.edu.agh.szia.client.agent.behaviour.NotificationBehaviour;

public class NotificationAgent extends Agent {

    @Override
    protected void setup() {
        addBehaviour(new NotificationBehaviour());
    }
}
