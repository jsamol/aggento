package pl.edu.agh.szia.server.agent;

import jade.core.Agent;
import pl.edu.agh.szia.server.auction.Auction;
import pl.edu.agh.szia.data.User;

public abstract class UserAgent extends Agent {

    private User user;
    private Auction targetAuction;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Auction getTargetAuction() {
        return targetAuction;
    }

    public void setTargetAuction(Auction targetAuction) {
        this.targetAuction = targetAuction;
    }
}
