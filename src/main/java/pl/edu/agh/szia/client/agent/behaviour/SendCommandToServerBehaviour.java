package pl.edu.agh.szia.client.agent.behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import pl.edu.agh.szia.utils.Configuration;

public class SendCommandToServerBehaviour extends Behaviour {
    private AID serverAid = new AID(Configuration.getServerAgentGuid(), AID.ISGUID);
    private final ACLMessage message;

    public SendCommandToServerBehaviour(ACLMessage message) {
        this.message = message;
    }

    @Override
    public void action() {
        message.addReceiver(serverAid);
        myAgent.send(message);
    }

    @Override
    public boolean done() {
        return true;
    }
}
