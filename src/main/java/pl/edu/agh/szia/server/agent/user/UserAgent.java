package pl.edu.agh.szia.server.agent.user;

import jade.core.Agent;
import pl.edu.agh.szia.server.auction.Auction;
import pl.edu.agh.szia.data.User;

abstract class UserAgent extends Agent {

    protected User user;
    protected Auction targetAuction;

    User getUser() {
        return user;
    }

    void setUser(User user) {
        this.user = user;
    }

    Auction getTargetAuction() {
        return targetAuction;
    }

    void setTargetAuction(Auction targetAuction) {
        this.targetAuction = targetAuction;
    }
}
