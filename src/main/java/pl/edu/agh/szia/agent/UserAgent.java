package pl.edu.agh.szia.agent;

import jade.core.Agent;
import pl.edu.agh.szia.auction.Auction;
import pl.edu.agh.szia.data.User;

public abstract class UserAgent extends Agent {

    private User user;
    private Auction targetAuction;

    UserAgent(User user) {
        this.user = user;
    }

    public void setTargetAuction(Auction targetAuction) {
        this.targetAuction = targetAuction;
    }
}
