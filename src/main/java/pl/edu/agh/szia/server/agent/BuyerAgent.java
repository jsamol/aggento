package pl.edu.agh.szia.server.agent;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.edu.agh.szia.server.auction.Auction;

import java.math.BigDecimal;


public class BuyerAgent extends UserAgent {
    private BigDecimal limit;
    private static final int RAISE_OFFER_VAL = 1;

    @Override
    protected void setup() {
        Object[] args = getArguments();
        setTargetAuction((Auction)args[0]);
        limit = (BigDecimal)args[1];

        System.out.println("Bidder " + getAID().getName() + " is ready.");

        setTargetAuction(getTargetAuction());
        getTargetAuction().addPartticipant(getAID());

        addBehaviour(new Bid());
        addBehaviour(new SignUpToAuction());
    }

    @Override
    protected void takeDown(){
        System.out.println("Auctioneer " + getAID().getName() + " terminating.");
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    class SignUpToAuction extends OneShotBehaviour{
        public void action(){
            System.out.println(getAID().getName() + " singing up for auction");
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(getTargetAuction().getOwnerAID());
            myAgent.send(msg);
        }
    }

    class Bid extends CyclicBehaviour{
        private MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

        public void action(){
            ACLMessage msg = myAgent.receive(mt);
            if(msg != null) {
                BigDecimal currentPrice = new BigDecimal(msg.getContent());
                if (currentPrice.compareTo(getLimit()) < 1 &&
                        !getTargetAuction().getWinningBidder().equals(getName())) {
                    BigDecimal offer = BigDecimal.valueOf(
                            Integer.min(currentPrice.intValue() + RAISE_OFFER_VAL, getLimit().intValue()));

                    System.out.println(getName() + " bid " + offer +
                            " for " + getTargetAuction().getProduct().getName());

                    ACLMessage offerMsg = new ACLMessage(ACLMessage.PROPOSE);
                    offerMsg.addReceiver(msg.getSender());
                    offerMsg.setContent(offer.toString());

                    myAgent.send(offerMsg);

                }
            }
            else
                block();
        }
    }
}
