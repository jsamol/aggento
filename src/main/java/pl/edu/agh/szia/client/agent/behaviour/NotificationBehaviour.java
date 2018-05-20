package pl.edu.agh.szia.client.agent.behaviour;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import pl.edu.agh.szia.utils.PrimitivePatterns;
import pl.edu.agh.szia.utils.command.NotificationMessage;

public class NotificationBehaviour extends CyclicBehaviour implements PrimitivePatterns {

    @Override
    public void action() {
        try {
            ACLMessage message = myAgent.blockingReceive();
            if (message != null) {
                handle(this::getMessageOf, this::handleNotification).apply(message);
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("Failed to handle incoming message" + e.getMessage());
        }
    }

    private NotificationMessage getMessageOf(ACLMessage message) {
        try {
            return (NotificationMessage) message.getContentObject();
        } catch (UnreadableException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleNotification(NotificationMessage msg) {
        System.out.println(msg.getType() + ":" + msg.getMessage());
    }
}
