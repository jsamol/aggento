package pl.edu.agh.szia.server.agent.server.behaviour;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import pl.edu.agh.szia.server.AuctionSystem;
import pl.edu.agh.szia.utils.command.CommandMessage;

public class ListenCyclicBehaviour extends CyclicBehaviour {

    private AuctionSystem auctionSystem;

    public ListenCyclicBehaviour(AuctionSystem auctionSystem) {
        super();
        this.auctionSystem = auctionSystem;
    }

    @Override
    public void action() {
        ACLMessage message = myAgent.blockingReceive();
        if (message != null) {
            handleCommandMessage(getCommandMessageFromACLMessage(message));
        }
    }

    private CommandMessage getCommandMessageFromACLMessage(ACLMessage message) {
        try {
            return (CommandMessage) message.getContentObject();
        } catch (UnreadableException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void handleCommandMessage(CommandMessage commandMessage) {
        if (commandMessage != null) {
            switch (commandMessage.getType()) {
                case SIGN_IN:
                    break;
                case CREATE_AUCTION:
                    break;
                case BID:
                    break;
                case LIST_AUCTIONS:
                    break;
                case SET_CURRENT_AUCTION:
                    break;
                default:
                    break;
            }
        }
    }
}
