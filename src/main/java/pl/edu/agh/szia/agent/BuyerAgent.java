package pl.edu.agh.szia.agent;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import pl.edu.agh.szia.auction.Auction;
import pl.edu.agh.szia.data.Product;
import pl.edu.agh.szia.data.User;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class BuyerAgent extends UserAgent {
    private BigDecimal limit;

    @Override
    protected void setup() {
        Object[] args = getArguments();
        setTargetAuction((Auction)args[0]);
        limit = (BigDecimal)args[1];

        System.out.println("Bidder " + getAID().getName() + " is ready.");

        setTargetAuction(getTargetAuction());

        addBehaviour(new Bid(this));
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
}

class Bid extends CyclicBehaviour{
    BuyerAgent myAgent;

    public Bid(BuyerAgent userAgent){
        super(userAgent);
        this.myAgent = userAgent;
    }

    public void action(){
        if(myAgent.getTargetAuction().getCurrentPrice().compareTo(myAgent.getLimit()) < 1 &&
                (myAgent.getTargetAuction().getWinningBidder() == null ||
                        !myAgent.getTargetAuction().getWinningBidder().equals(myAgent.getName()))){
            System.out.println("Sending bid from " + myAgent.getName());
            ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
            msg.addReceiver(myAgent.getTargetAuction().getOwnerAID());
            BigDecimal offer = BigDecimal.valueOf(myAgent.getTargetAuction().getCurrentPrice().intValue() + 5);
            msg.setContent(offer.toString());
            myAgent.send(msg);

            ACLMessage reply = myAgent.receive();
            if(reply != null){
                System.out.println("Accepted");
            }
            else{
                block();
            }
        }
    }
}
