package pl.edu.agh.szia.server.agent.user;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.edu.agh.szia.server.auction.Auction;
import pl.edu.agh.szia.data.User;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class SellerAgent extends UserAgent {

    private String itemName;

    @Override
    protected void setup() {
        Object[] args = getArguments();
        setUser((User)args[0]);

        itemName = ((String)args[1]);
        System.out.println("Auctioneer " + getAID().getName() + " is ready.");

        System.out.println("It is selling " + getUser().getUsername() + "'s: " + itemName + " item.");
        setTargetAuction(user.getActiveAuction());
        getTargetAuction().setOwnerAID(getAID());

        addBehaviour(new UpdateBestBid());
        addBehaviour(new AddBidder());
        addBehaviour(new EndAuction(this, getTargetAuction().getEndTime() - new Timestamp(System.currentTimeMillis()).getTime()));
    }

    @Override
    protected void takeDown(){
        System.out.println("Auctioneer " + getAID().getName() + " terminating.");
    }

    class AddBidder extends CyclicBehaviour {
        private MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

        public void action() {
            ACLMessage msg = myAgent.receive(mt);
            if(msg != null){
                System.out.println("Adding participant " + msg.getSender().getName());
                getTargetAuction().addPartticipant(msg.getSender());
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                reply.setContent(getTargetAuction().getCurrentPrice().toString());
                myAgent.send(reply);
            }
            else {
                block();
            }
        }
    }

    class NotifyBidders extends OneShotBehaviour {
        public void action(){
            ACLMessage notification = new ACLMessage(ACLMessage.INFORM);
            notification.setContent(getTargetAuction().getCurrentPrice().toString());
            for(AID participantAID: getTargetAuction().getParticipants()){
                notification.addReceiver(participantAID);
            }
            myAgent.send(notification);
        }
    }

    class UpdateBestBid extends CyclicBehaviour {
        private MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);

        public void action(){
            ACLMessage msg = myAgent.receive(mt);
            if(msg != null){
                BigDecimal bid = new BigDecimal(msg.getContent());
                if(bid.compareTo(getTargetAuction().getCurrentPrice()) > 0){
                    System.out.println("Raising current price for: " + getTargetAuction().getProduct().getName());
                    System.out.println("Old price: " + getTargetAuction().getCurrentPrice());
                    getTargetAuction().raisePrice(bid);
                    System.out.println("New price: " + getTargetAuction().getCurrentPrice());
                    getTargetAuction().setWinningBidder(msg.getSender().getName());
                    System.out.println("Winning bidder: "  + getTargetAuction().getWinningBidder());
                    myAgent.addBehaviour(new NotifyBidders());
                }
            }
            else
                block();
        }
    }

    class EndAuction extends WakerBehaviour{
        public EndAuction(Agent agent, long timeout){
            super(agent, timeout);
        }
        protected void onWake() {
            System.out.println("Auction ended");
            System.out.println("The winner is " + getTargetAuction().getWinningBidder());
        }
    }
}

