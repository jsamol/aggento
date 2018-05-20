package pl.edu.agh.szia.server.agent.server.behaviour;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import pl.edu.agh.szia.server.AuctionSystem;
import pl.edu.agh.szia.utils.command.CommandMessage;

public class ListenCyclicBehaviour extends CyclicBehaviour {

    private static final String DEFAULT_MESSAGE = "Command unrecognized.";

    private AuctionSystem auctionSystem;

    public ListenCyclicBehaviour(AuctionSystem auctionSystem) {
        super();
        this.auctionSystem = auctionSystem;
    }

    @Override
    public void action() {
        ACLMessage message = myAgent.blockingReceive();
        if (message != null) {
            final ACLMessage response = handleCommandMessage(getCommandMessageFromACLMessage(message));
            response.addReceiver(message.getSender());
            myAgent.send(response);
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

    private ACLMessage handleCommandMessage(CommandMessage commandMessage) {
        if (commandMessage != null) {
            String[] args = commandMessage.getArguments();
            switch (commandMessage.getType()) {
                case SIGN_IN:
                    if (args.length == 0) {
                        break;
                    }
                    return auctionSystem.registerUser(args[0]);
                case CREATE_AUCTION:
                    if (args.length < 3) {
                        break;
                    }
                    return auctionSystem.createAuction(args[0], args[1], args[2]);
                case BID:
                    if (args.length < 2) {
                        break;
                    }
                    return auctionSystem.bid(args[0], args[1]);
                case LIST_AUCTIONS:
                    return auctionSystem.listAuctions();
                case SET_ACTIVE_AUCTION:
                    if (args.length < 2) {
                        break;
                    }
                    return auctionSystem.setActiveAuction(args[0], args[1]);
                case SUBSCRIBE:
                    if (args.length < 1) {
                        break;
                    }
                    return auctionSystem.addSubscriber(args[0]);
                case UNSUBSCRIBE:
                    if (args.length < 1) {
                        break;
                    }
                    return auctionSystem.removeSubscriber(args[0]);
            }
        }

        return createDefaultMessage();
    }

    private ACLMessage createDefaultMessage() {
        final ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setContent(DEFAULT_MESSAGE);
        return message;
    }
}
