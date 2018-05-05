package pl.edu.agh.szia.agent;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.edu.agh.szia.auction.Auction;
import pl.edu.agh.szia.data.Product;
import pl.edu.agh.szia.data.User;

import javax.print.attribute.standard.MediaSize;
import java.math.BigDecimal;
import java.math.BigInteger;

public class SellerAgent extends UserAgent {

    private String itemName;

    @Override
    protected void setup() {
        Object[] args = getArguments();
        setUser((User)args[0]);

        itemName = ((String)args[1]);
        System.out.println("Auctioneer " + getAID().getName() + " is ready.");

        System.out.println("It is selling " + getUser().getUsername() + "'s: " + itemName + " item.");
        setTargetAuction((Auction)args[2]);
        getTargetAuction().setOwnerAID(getAID());

        addBehaviour(new UpdateBestBid(this));
    }

    @Override
    protected void takeDown(){
        System.out.println("Auctioneer " + getAID().getName() + " terminating.");
    }
}

class UpdateBestBid extends CyclicBehaviour {
    UserAgent myAgent;
    private MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);

    public UpdateBestBid(UserAgent userAgent){
        super(userAgent);
        this.myAgent = userAgent;
    }

    public void action(){
        ACLMessage msg = myAgent.receive(mt);
        if(msg != null){
            BigDecimal bid = new BigDecimal(msg.getContent());
            ACLMessage reply = msg.createReply();

            if(bid.compareTo(myAgent.getTargetAuction().getCurrentPrice()) > 0){
                System.out.println("Raising current price for: " + myAgent.getTargetAuction().getProduct().getName());
                System.out.println("Old price: " + myAgent.getTargetAuction().getCurrentPrice());
                myAgent.getTargetAuction().raisePrice(bid);
                System.out.println("New price: " + myAgent.getTargetAuction().getCurrentPrice());
                myAgent.getTargetAuction().setWinningBidder(msg.getSender().getName());
                System.out.println("Winning bidder: "  + myAgent.getTargetAuction().getWinningBidder());
                reply.setPerformative(ACLMessage.AGREE);
            }

            reply.setPerformative(ACLMessage.CANCEL);
            myAgent.send(reply);
        }
        else
            block();
    }
}
